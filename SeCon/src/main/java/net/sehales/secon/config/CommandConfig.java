
package net.sehales.secon.config;

import java.io.File;

import net.sehales.secon.utils.java.ReflectionUtils;

import org.yaml.snakeyaml.DumperOptions;

public class CommandConfig extends Config {
    
    public CommandConfig(File configFile) {
        super(configFile);
    }
    
    @Override
    public boolean load() {
        boolean success = super.load();
        if (success) {
            try {
                Object obj = ReflectionUtils.getDeclaredFieldValue(config, "yamlOptions");
                DumperOptions options = (DumperOptions) obj;
                options.setWidth(200);
            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
                System.out.println("Can't change yaml line width for command config.");
                e.printStackTrace();
                // success = false;
            }
        }
        return success;
    }
}
