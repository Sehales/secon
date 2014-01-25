
package net.sehales.secon.addon;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

public class AddonDescription {
    
    private static final Yaml yaml = new Yaml(new SafeConstructor());
    private String            addonName;
    private String            className;
    private String            version;
    
    public AddonDescription(File file) throws InvalidAddonDescriptionException {
        InputStream is = null;
        try (JarFile jarFile = new JarFile(file);) {
            JarEntry entry = jarFile.getJarEntry("addon.yml");
            if (entry == null) {
                throw new InvalidAddonDescriptionException(new FileNotFoundException("Jar does not contain addon.yml"));
            }
            is = jarFile.getInputStream(entry);
            if (is != null) {
                Configuration config = YamlConfiguration.loadConfiguration(is);
                // Map<?, ?> map = (Map<?, ?>) yaml.load(is);
                String name = config.getString("name").replace(" ", "_");
                if (name == null || name.isEmpty()) {
                    throw new InvalidAddonDescriptionException("invalid name or entry not found");
                }
                
                String main = config.getString("main-class");
                if (main == null || main.isEmpty()) {
                    throw new InvalidAddonDescriptionException("invalid main-class or entry not found");
                }
                
                String version = config.getString("version");
                if (version == null || version.isEmpty()) {
                    throw new InvalidAddonDescriptionException("invalid main-class or entry not found");
                }
                
                this.version = version;
                this.addonName = name;
                this.className = main;
            } else {
                throw new InvalidAddonDescriptionException("Can't load description");
            }
        } catch (IOException e) {
            throw new InvalidAddonDescriptionException(e);
        }
        
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
