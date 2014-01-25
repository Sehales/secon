
package net.sehales.secon.addon;

import java.io.File;
import java.io.IOException;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sehales.secon.SeCon;
import net.sehales.secon.SeConLogger;
import net.sehales.secon.utils.MiscUtils;
import net.sehales.secon.utils.SimplePriorityList.Priority;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public abstract class Addon {
    
    private String                       name;
    private String                       version;
    private SeCon                        secon;
    private boolean                      enabled;
    private Priority                     priority;
    
    private FileConfiguration            config;
    private File                         configFile;
    
    private File                         addonFile;
    private File                         rawFile;
    
    private URLClassLoader               loader;
    
    protected Map<Object, List<Command>> objectCommandMap = new HashMap<>();
    protected List<Command>              commandList      = new ArrayList<>();
    protected List<Listener>             listenerList     = new ArrayList<>();
    
    /**
     * add a language node - adding the value on "info.[ADDON_NAME].[PATH}"
     * 
     * @param path
     * @param value
     */
    public void addLanguageNode(String path, String value) {
        SeCon.getInstance().getLang().add("addon." + getName() + "." + path, value);
        SeCon.getInstance().getLang().reload();
    }
    
    /**
     * check if the version of the addon is higher or the same as your given one
     * 
     * @param neededVersion
     *            the version to compare with
     * @return true if the version is equal or higher as the version to compare
     *         with
     */
    public boolean compareVersions(String neededVersion) {
        int i = MiscUtils.compareVersions(getVersion(), neededVersion);
        if (i == 0 || i == 1) {
            return true;
        } else {
            return false;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Addon other = (Addon) obj;
        if (addonFile == null) {
            if (other.addonFile != null) {
                return false;
            }
        } else if (!addonFile.equals(other.addonFile)) {
            return false;
        }
        if (commandList == null) {
            if (other.commandList != null) {
                return false;
            }
        } else if (!commandList.equals(other.commandList)) {
            return false;
        }
        if (config == null) {
            if (other.config != null) {
                return false;
            }
        } else if (!config.equals(other.config)) {
            return false;
        }
        if (configFile == null) {
            if (other.configFile != null) {
                return false;
            }
        } else if (!configFile.equals(other.configFile)) {
            return false;
        }
        if (enabled != other.enabled) {
            return false;
        }
        if (listenerList == null) {
            if (other.listenerList != null) {
                return false;
            }
        } else if (!listenerList.equals(other.listenerList)) {
            return false;
        }
        if (loader == null) {
            if (other.loader != null) {
                return false;
            }
        } else if (!loader.equals(other.loader)) {
            return false;
        }
        if (name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!name.equals(other.name)) {
            return false;
        }
        if (objectCommandMap == null) {
            if (other.objectCommandMap != null) {
                return false;
            }
        } else if (!objectCommandMap.equals(other.objectCommandMap)) {
            return false;
        }
        if (priority != other.priority) {
            return false;
        }
        if (rawFile == null) {
            if (other.rawFile != null) {
                return false;
            }
        } else if (!rawFile.equals(other.rawFile)) {
            return false;
        }
        if (version == null) {
            if (other.version != null) {
                return false;
            }
        } else if (!version.equals(other.version)) {
            return false;
        }
        return true;
    }
    
    /**
     * @return the addonFile
     */
    public File getAddonFile() {
        return addonFile;
    }
    
    /**
     * get the ClassLoader which is associated with that addon
     * 
     * @return the ClassLoader
     */
    public URLClassLoader getClassLoader() {
        return loader;
    }
    
    public List<Command> getCommands() {
        List<Command> cmdList = new ArrayList<>();
        cmdList.addAll(commandList);
        for (List<Command> cmds : objectCommandMap.values()) {
            cmdList.addAll(cmds);
        }
        
        return cmdList;
    }
    
    /**
     * get the config file from the addon config directory
     * 
     * @return the config
     */
    protected FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }
    
    /**
     * get a node from the language.yml in section 'info.[ADDON_NAME].[PATH}'
     * 
     * @param path
     * @return get the info node
     */
    public String getLanguageNode(String path) {
        return SeCon.getInstance().getLang().getString("addon." + getName() + "." + path);
    }
    
    public List<Listener> getListenerList() {
        return Arrays.asList(listenerList.toArray(new Listener[0]));
    }
    
    /**
     * get the standart SeConLogger
     * 
     * @return SeConLogger
     */
    protected SeConLogger getLogger() {
        return secon.log();
    }
    
    public String getName() {
        return name;
    }
    
    /**
     * @return the objectCommandMap
     */
    public Map<Object, List<Command>> getObjectCommandMap() {
        return objectCommandMap;
    }
    
    public Priority getPriority() {
        return priority;
    }
    
    /**
     * @return the rawFile
     */
    public File getRawFile() {
        return rawFile;
    }
    
    public String getVersion() {
        return version;
    }
    
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((addonFile == null) ? 0 : addonFile.hashCode());
        result = prime * result + ((commandList == null) ? 0 : commandList.hashCode());
        result = prime * result + ((config == null) ? 0 : config.hashCode());
        result = prime * result + ((configFile == null) ? 0 : configFile.hashCode());
        result = prime * result + (enabled ? 1231 : 1237);
        result = prime * result + ((listenerList == null) ? 0 : listenerList.hashCode());
        result = prime * result + ((loader == null) ? 0 : loader.hashCode());
        result = prime * result + ((name == null) ? 0 : name.hashCode());
        result = prime * result + ((objectCommandMap == null) ? 0 : objectCommandMap.hashCode());
        result = prime * result + ((priority == null) ? 0 : priority.hashCode());
        result = prime * result + ((rawFile == null) ? 0 : rawFile.hashCode());
        result = prime * result + ((version == null) ? 0 : version.hashCode());
        return result;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    protected void onDisable() {
    }
    
    protected abstract boolean onEnable();
    
    protected boolean onLoad() {
        return true;
    }
    
    protected void onUnload() {
    }
    
    protected synchronized boolean registerCommand(Command command) {
        if (commandList.contains(command)) {
            return false;
        }
        boolean result = secon.getCommandManager().registerCommand("/", command);
        if (result) {
            commandList.add(command);
        }
        return result;
    }
    
    protected synchronized List<Command> registerCommandsFromObject(Object obj) {
        if (objectCommandMap.containsKey(obj)) {
            return Collections.emptyList();
        }
        
        List<Command> commands = secon.getCommandManager().registerCommandsFromObject(getName(), obj);
        objectCommandMap.put(obj, commands);
        
        return commands;
    }
    
    /**
     * register a listener class
     * 
     * @param listener
     *            the listener class to register
     */
    protected void registerListener(Listener listener) {
        Bukkit.getPluginManager().registerEvents(listener, secon);
        if (!this.listenerList.contains(listener)) {
            this.listenerList.add(listener);
        }
    }
    
    /**
     * reload the config
     */
    protected void reloadConfig() {
        if (configFile == null) {
            configFile = new File(SeCon.addonConfigFolder + File.separator + name + ".yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }
    
    /**
     * save the config
     */
    protected void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            secon.log().severe(null, "Could not save config to " + configFile);
        }
    }
    
    /**
     * @param addonFile
     *            the addonFile to set
     */
    void setAddonFile(File addonFile) {
        this.addonFile = addonFile;
    }
    
    void setDescription(AddonDescription desc) {
        this.name = desc.getName();
        this.version = desc.getVersion();
    }
    
    void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
    
    void setName(String name) {
        this.name = name;
    }
    
    void setPriority(Priority priority) {
        this.priority = priority;
    }
    
    /**
     * @param rawFile
     *            the rawFile to set
     */
    void setRawFile(File rawFile) {
        this.rawFile = rawFile;
    }
    
    void setSeconInstance(SeCon secon) {
        this.secon = secon;
    }
    
    void setVersion(String version) {
        this.version = version;
    }
    
    protected synchronized boolean unregisterCommand(Command command) {
        if (commandList.contains(command)) {
            secon.getCommandManager().unregisterCommand(command);
            return true;
        }
        return false;
    }
    
    protected synchronized void unregisterCommands() {
        for (Command cmd : commandList.toArray(new Command[0])) {
            secon.getCommandManager().unregisterCommand(cmd);
        }
        
        for (Object obj : objectCommandMap.keySet().toArray(new Object[0])) {
            secon.getCommandManager().unregisterCommandsFromObject(obj);
        }
    }
    
    protected synchronized void unregisterCommandsFromObject(Object obj) {
        if (!objectCommandMap.containsKey(obj)) {
            return;
        }
        
        secon.getCommandManager().unregisterCommandsFromObject(obj);
        objectCommandMap.remove(obj);
    }
    
    /**
     * unregister the specific listener
     * 
     * @param l
     */
    protected void unregisterListener(Listener l) {
        HandlerList.unregisterAll(l);
        this.listenerList.remove(l);
    }
    
    /**
     * unregister all listeners registered by this addon
     */
    protected synchronized void unregisterListeners() {
        for (Listener listener : listenerList.toArray(new Listener[0])) {
            unregisterListener(listener);
        }
        
    }
}
