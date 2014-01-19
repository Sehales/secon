
package net.sehales.secon.addon;

import java.io.InputStream;

import net.sehales.secon.addon.InvalidAddonDescriptionException.Reason;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class AddonDescription {
    
    private String addonName;
    private String className;
    private String version;
    
    public AddonDescription(InputStream stream) throws InvalidAddonDescriptionException {
        FileConfiguration config = YamlConfiguration.loadConfiguration(stream);
        String name = config.getString("name").replace(" ", "_");
        if (name == null || name.isEmpty()) {
            throw new InvalidAddonDescriptionException(Reason.INVALID_NAME);
        }
        
        String main = config.getString("main-class");
        if (main == null || main.isEmpty()) {
            throw new InvalidAddonDescriptionException(Reason.INVALID_CLASS_NAME);
        }
        
        String version = config.getString("version");
        if (version == null || version.isEmpty()) {
            throw new InvalidAddonDescriptionException(Reason.INVALID_VERSION);
        }
        
        this.version = version;
        this.addonName = name;
        this.className = main;
    }
    
    public String getMainClassName() {
        return className;
    }
    
    public String getName() {
        return addonName;
    }
    
    public String getVersion() {
        return version;
    }
}
