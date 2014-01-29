
package net.sehales.secon.addon;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import net.sehales.secon.SeCon;
import net.sehales.secon.config.LanguageConfig;
import net.sehales.secon.utils.SimplePriorityList;
import net.sehales.secon.utils.SimplePriorityList.Priority;
import net.sehales.secon.utils.chat.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.util.FileUtil;

public class AddonManager {
    
    private static final String       JAR_FILE_SUFFIX = ".jar";
    private final File                addonFolder;
    private final File                tempFolder;
    private Map<String, Addon>        addonMap        = new HashMap<>();
    private AddonClassLoader          loader;
    private SimplePriorityList<Addon> priorityList    = new SimplePriorityList<>();
    private SeCon                     secon;
    private LanguageConfig            lang;
    
    public AddonManager(SeCon secon, File addonFolder) {
        this.secon = secon;
        this.addonFolder = addonFolder;
        this.tempFolder = new File(addonFolder.getAbsolutePath() + File.separator + "tmp");
        this.lang = secon.getLang();
        if (!addonFolder.exists()) {
            addonFolder.mkdirs();
        }
        if (!addonFolder.isDirectory()) {
            throw new IllegalArgumentException("Addon file is not a folder!");
        }
        
        tempFolder.mkdirs();
        
    }
    
    public boolean addonsLoaded() {
        return !addonMap.isEmpty();
    }
    
    private Addon createInstance(CommandSender infoReceiver, AddonDescription desc, File file) throws MalformedURLException, ClassNotFoundException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        URL url = file.toURI().toURL();
        if (loader == null) {
            loader = new AddonClassLoader(new URL[] { url }, this.getClass().getClassLoader());
        } else {
            loader.addURL(url);
        }
        
        Class<?> addonClass = loader.loadClass(desc.getMainClassName());
        if (addonClass.getSuperclass() != Addon.class) {
            sendMessage(infoReceiver, lang.INVALID_CLASS_TYPE.replace("<class>", addonClass.getSuperclass().getCanonicalName()).replace("<addonname>", desc.getName()));
            return null;
        }
        
        Priority priority = Priority.NORMAL;
        if (addonClass.isAnnotationPresent(SeConAddonHandler.class)) {
            priority = addonClass.getAnnotation(SeConAddonHandler.class).priority();
        }
        
        Addon addon = (Addon) addonClass.getConstructor().newInstance();
        addon.setDescription(desc);
        addon.setPriority(priority);
        return addon;
    }
    
    public synchronized boolean disableAddon(CommandSender infoReceiver, Addon addon) {
        if (addon == null) {
            sendMessage(infoReceiver, lang.ARGUMENT_CANT_BE_NULL);
            return false;
        }
        
        if (!addon.isEnabled()) {
            sendMessage(infoReceiver, lang.ADDON_ALREADY_DISABLED.replace("<name>", addon.getName()));
            return true;
        }
        
        addon.onDisable();
        addon.setEnabled(false);
        sendMessage(infoReceiver, lang.ADDON_DISABLED.replace("<name>", addon.getName()));
        return true;
    }
    
    public boolean disableAddon(CommandSender infoReceiver, String addonName) {
        return disableAddon(infoReceiver, getAddon(addonName));
    }
    
    public boolean enableAddon(CommandSender infoReceiver, Addon addon) {
        if (addon == null) {
            sendMessage(infoReceiver, lang.ARGUMENT_CANT_BE_NULL);
            return false;
        }
        
        if (addon.isEnabled()) {
            sendMessage(infoReceiver, lang.ADDON_ALREADY_ENABLED.replace("<name>", addon.getName()));
            return true;
        }
        
        addon.onEnable();
        addon.setEnabled(true);
        sendMessage(infoReceiver, lang.ADDON_ENABLED.replace("<name>", addon.getName()));
        return true;
    }
    
    public synchronized boolean enableAddon(CommandSender infoReceiver, String addonName) {
        return enableAddon(infoReceiver, getAddon(addonName));
    }
    
    public Addon getAddon(String addonName) {
        return addonMap.get(addonName);
    }
    
    public File getAddonFolder() {
        return addonFolder;
    }
    
    public synchronized Collection<Addon> getAddons() {
        return addonMap.values();
    }
    
    public synchronized Addon loadAddon(CommandSender infoReceiver, File rawFile) {
        if (!rawFile.exists()) {
            sendMessage(infoReceiver, lang.FILE_NOT_FOUND.replace("<file>", rawFile.getAbsolutePath()));
            return null;
        }
        
        if (!rawFile.canRead()) {
            sendMessage(infoReceiver, lang.CANT_READ_FILE.replace("<file>", rawFile.getAbsolutePath()));
            return null;
        }
        
        File addonFile = new File(tempFolder, rawFile.getName().replace(JAR_FILE_SUFFIX, "-" + System.currentTimeMillis() + JAR_FILE_SUFFIX));
        if (!FileUtil.copy(rawFile, addonFile)) {
            sendMessage(infoReceiver, lang.FILE_NOT_FOUND.replace("<file>", addonFile.getAbsolutePath()));
            return null;
        }
        
        try {
            // FileInputStream is = new FileInputStream(addonFile);
            // ZipInputStream zis = new ZipInputStream(is);
            // ZipEntry entry = zis.getNextEntry();
            //
            // while (entry != null) {
            // if (!entry.getName().equalsIgnoreCase("addon.yml")) {
            // entry = zis.getNextEntry();
            // } else {
            // break;
            // }
            // }
            
            AddonDescription desc = new AddonDescription(addonFile);
            
            if (getAddon(desc.getName()) != null) {
                sendMessage(infoReceiver, lang.ADDON_ALREADY_LOADED.replace("<name>", desc.getName()));
                return null;
            }
            
            Addon addon = createInstance(infoReceiver, desc, addonFile);
            addon.setRawFile(rawFile);
            addon.setAddonFile(addonFile);
            addon.setSeconInstance(secon);
            priorityList.add(addon, addon.getPriority());
            addonMap.put(addon.getName(), addon);
            
            if (addon.onLoad()) {
                sendMessage(infoReceiver, lang.ADDON_LOADED.replace("<name>", addon.getName()));
                return addon;
            } else {
                sendMessage(infoReceiver, lang.ADDON_FAILED_LOAD.replace("<name>", addon.getName()));
                return null;
            }
            
        } catch (IOException | InvalidAddonDescriptionException | ReflectiveOperationException | IllegalArgumentException | SecurityException e) {
            sendMessage(infoReceiver, lang.EXCEPTION_OCCURED.replace("<name>", e.getClass().getSimpleName()).replace("<message>", e.getLocalizedMessage()));
            e.printStackTrace();
            return null;
        }
    }
    
    public Addon loadAddon(CommandSender infoReceiver, String fileName) {
        if (!fileName.endsWith(JAR_FILE_SUFFIX)) {
            fileName = fileName + JAR_FILE_SUFFIX;
        }
        
        File rawFile = new File(addonFolder, fileName);
        
        return loadAddon(infoReceiver, rawFile);
    }
    
    public void loadAddons(CommandSender infoReceiver) {
        if (!tempFolder.exists()) {
            tempFolder.mkdirs();
        }
        
        // sendMessage(infoReceiver,
        // lang.ADDON_LOADING_ADDONS.replace("<folder>",
        // addonFolder.getAbsolutePath()));
        sendMessage(infoReceiver, lang.ADDON_LOADING_ADDONS);
        if (!addonFolder.canRead()) {
            sendMessage(infoReceiver, lang.CANT_READ_FILE.replace("<file>", addonFolder.getAbsolutePath()));
            return;
        }
        
        for (File f : addonFolder.listFiles()) {
            if (f.getName().endsWith(JAR_FILE_SUFFIX)) {
                loadAddon(infoReceiver, f);
            }
        }
        
        for (Addon addon : priorityList.getLowestElements()) {
            enableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : priorityList.getLowElements()) {
            enableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : priorityList.getNormalElements()) {
            enableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : priorityList.getHighElements()) {
            enableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : priorityList.getHighestElements()) {
            enableAddon(infoReceiver, addon);
        }
        
        // sendMessage(infoReceiver,
        // lang.ADDON_LOADED_ADDONS.replace("<folder>",
        // addonFolder.getAbsolutePath()));
        sendMessage(infoReceiver, lang.ADDON_LOADED_ADDONS);
    }
    
    private void sendMessage(CommandSender receiver, String message) {
        if (receiver == null) {
            receiver = Bukkit.getConsoleSender();
        }
        ChatUtils.sendFormattedMessage(Bukkit.getConsoleSender(), message);
        if (!(receiver instanceof ConsoleCommandSender)) {
            ChatUtils.sendFormattedMessage(receiver, message);
        }
    }
    
    public synchronized boolean unloadAddon(CommandSender infoReceiver, Addon addon) {
        if (addon == null) {
            sendMessage(infoReceiver, lang.ARGUMENT_CANT_BE_NULL);
            return false;
        }
        
        try {
            disableAddon(infoReceiver, addon);
        } catch (Exception e) {
            sendMessage(infoReceiver, lang.EXCEPTION_OCCURED.replace("<name>", e.getClass().getSimpleName()));
            e.printStackTrace();
            return false;
        }
        
        return unloadAddonOnly(infoReceiver, addon);
    }
    
    public boolean unloadAddon(CommandSender infoReceiver, String addonName) {
        return unloadAddon(infoReceiver, getAddon(addonName));
    }
    
    public boolean unloadAddonOnly(CommandSender infoReceiver, Addon addon) {
        if (addon == null) {
            sendMessage(infoReceiver, lang.ARGUMENT_CANT_BE_NULL);
            return false;
        }
        try {
            addon.onUnload();
        } catch (Exception e) {
            sendMessage(infoReceiver, lang.EXCEPTION_OCCURED.replace("<name>", e.getClass().getSimpleName()));
            e.printStackTrace();
        }
        priorityList.remove(addon);
        addonMap.remove(addon.getName());
        addon.getAddonFile().deleteOnExit();
        addon.getAddonFile().delete();
        sendMessage(infoReceiver, lang.ADDON_UNLOADED.replace("<name>", addon.getName()));
        return true;
    }
    
    public boolean unloadAddonOnly(CommandSender infoReceiver, String addonName) {
        return unloadAddonOnly(infoReceiver, getAddon(addonName));
    }
    
    public void unloadAddons(CommandSender infoReceiver) {
        sendMessage(infoReceiver, lang.ADDON_UNLOADING_ADDONS);
        for (Addon addon : priorityList.getHighestElements()) {
            disableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : priorityList.getHighElements()) {
            disableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : priorityList.getNormalElements()) {
            disableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : priorityList.getLowElements()) {
            disableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : priorityList.getLowestElements()) {
            disableAddon(infoReceiver, addon);
        }
        
        for (Addon addon : addonMap.values()) {
            unloadAddonOnly(infoReceiver, addon);
        }
        
        addonMap.clear();
        addonMap = new HashMap<>();
        try {
            if (loader != null) {
                loader.close();
                loader = null;
            }
        } catch (IOException e) {
            sendMessage(infoReceiver, lang.EXCEPTION_OCCURED.replace("<name>", e.getClass().getSimpleName()));
            e.printStackTrace();
        }
        
        System.gc();
        System.gc();
        addonFolder.delete();
        sendMessage(infoReceiver, lang.ADDON_UNLOADED_ADDONS);
    }
}
