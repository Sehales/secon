
package net.sehales.secon.config;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import net.sehales.secon.SeCon;
import net.sehales.secon.utils.mc.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
    
    protected File              configFile;
    protected YamlConfiguration config;
    protected Configuration     defaultConfig;
    
    public Config(File configFile) {
        this.configFile = configFile;
    }
    
    public void add(String path, Object obj) {
        if (!contains(path)) {
            set(path, obj);
        }
    }
    
    public boolean contains(String path) {
        return config.contains(path);
    }
    
    public Object get(String path) {
        return config.get(path);
    }
    
    public Object get(String path, Object def) {
        return config.get(path, def);
    }
    
    public <T> Object get(String path, Object def, Class<T> clazz) {
        return config.get(path, def);
    }
    
    public boolean getBoolean(String path) {
        return config.getBoolean(path);
    }
    
    public boolean getBoolean(String path, boolean def) {
        return config.getBoolean(path, def);
    }
    
    public Configuration getConfig() {
        return config;
    }
    
    public File getConfigFile() {
        return configFile;
    }
    
    public int getConfigVersion() {
        return getInt("config-file-version", 1);
    }
    
    public Configuration getDefaultConfig() {
        return defaultConfig;
    }
    
    public double getDouble(String path) {
        return config.getDouble(path);
    }
    
    public double getDouble(String path, double def) {
        return config.getDouble(path, def);
    }
    
    public int getInt(String path) {
        return config.getInt(path);
    }
    
    public int getInt(String path, int def) {
        return config.getInt(path, def);
    }
    
    public short getShort(String path) {
        return (short) config.getInt(path);
    }
    
    public String getString(String path) {
        return config.getString(path);
    }
    
    public String getString(String path, String def) {
        return config.getString(path, def);
    }
    
    public List<String> getStringList(String path) {
        return config.getStringList(path);
    }
    
    public List<String> getStringList(String path, List<String> def) {
        return config.getStringList(path);
    }
    
    // could be overriden to load default values
    public void initValues() {
    }
    
    public boolean load() {
        config = YamlConfiguration.loadConfiguration(configFile);
        if (defaultConfig != null) {
            config.setDefaults(defaultConfig);
        }
        return config != null;
    }
    
    public boolean loadAndInform() {
        boolean loaded = load();
        if (loaded) {
            ChatUtils.sendFormattedMessage(Bukkit.getConsoleSender(), SeCon.getInstance().getLang().CONFIG_FILE_LOADED.replace("<filename>", configFile.getName()));
        }
        return loaded;
    }
    
    public void readDefaultValues(File file) {
        if (file != null && file.exists()) {
            defaultConfig = YamlConfiguration.loadConfiguration(file);
        }
    }
    
    public void readDefaultValues(InputStream is) {
        if (is != null) {
            defaultConfig = YamlConfiguration.loadConfiguration(is);
        }
    }
    
    public void reload() {
        save();
        load();
    }
    
    public void reloadAndInform() {
        saveAndInform();
        loadAndInform();
    }
    
    public boolean save() {
        if (config != null && configFile != null) {
            try {
                config.save(configFile);
                return true;
            } catch (IOException e) {
                System.out.println("Something went wrong while saving to file: " + configFile.getAbsolutePath());
                e.printStackTrace();
            }
        }
        return false;
    }
    
    public boolean saveAndInform() {
        boolean saved = save();
        if (saved) {
            ChatUtils.sendFormattedMessage(Bukkit.getConsoleSender(), SeCon.getInstance().getLang().CONFIG_FILE_SAVED.replace("<name>", configFile.getName()));
        }
        return saved;
    }
    
    public void set(String path, Object obj) {
        config.set(path, obj);
    }
    
    public void setConfigVersion(int version) {
        set("config-file-version", version);
    }
}
