
package net.sehales.secon.utils.plugin;

import net.milkbowl.vault.Vault;
import net.sehales.secon.SeCon;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.kitteh.tag.TagAPI;
import org.kitteh.vanish.VanishPlugin;

import com.palmergames.bukkit.towny.Towny;

public class PluginUtils {
    
    private class PluginListener implements Listener {
        
        @EventHandler()
        public void onPluginDisable(PluginDisableEvent e) {
            Plugin plugin = e.getPlugin();
            
            switch (plugin.getName()) {
                case "TagAPI": {
                    tagAPIUtils.disable();
                    tagAPIUtils = null;
                    break;
                }
                case "Towny": {
                    townyUtils = null;
                    break;
                }
                case "VanishNoPacket": {
                    vanishUtils = null;
                    break;
                }
                case "Vault": {
                    vaultUtils = null;
                    break;
                }
                default: {
                    break;
                }
            }
        }
        
        @EventHandler()
        public void onPluginEnable(PluginEnableEvent e) {
            Plugin plugin = e.getPlugin();
            
            switch (plugin.getName()) {
                case "TagAPI": {
                    tagAPIUtils = new TagAPIUtils(secon);
                    break;
                }
                case "Towny": {
                    townyUtils = new TownyUtils();
                    break;
                }
                case "VanishNoPacket": {
                    vanishUtils = new VanishUtils();
                    break;
                }
                case "Vault": {
                    vaultUtils = new VaultUtils();
                    break;
                }
                default: {
                    break;
                }
            }
        }
    }
    
    private SeCon         secon;
    private PluginManager pm;
    private VaultUtils    vaultUtils;
    private VanishUtils   vanishUtils;
    private TownyUtils    townyUtils;
    
    private TagAPIUtils   tagAPIUtils;
    
    @SuppressWarnings("unused")
    private PluginUtils() {// deny default constructor access
    }
    
    public PluginUtils(SeCon secon) {
        this.secon = secon;
        pm = secon.getServer().getPluginManager();
        
        if (isTownyEnabled()) {
            this.townyUtils = new TownyUtils();
        }
        
        if (isVanishEnabled()) {
            this.vanishUtils = new VanishUtils();
        }
        
        if (isVaultEnabled()) {
            this.vaultUtils = new VaultUtils();
        }
        
        if (isTagAPIEnabled()) {
            this.tagAPIUtils = new TagAPIUtils(secon);
        }
        
        pm.registerEvents(new PluginListener(), secon);
    }
    
    public Plugin getPlugin(String pluginName) {
        return pm.getPlugin(pluginName);
    }
    
    public TagAPIUtils getTagAPIUtils() {
        return tagAPIUtils;
    }
    
    public TownyUtils getTownyUtils() {
        return townyUtils;
    }
    
    public VanishUtils getVanishUtls() {
        return vanishUtils;
    }
    
    public VaultUtils getVaultUtils() {
        return vaultUtils;
    }
    
    public boolean isPluginEnabled(Plugin plugin) {
        return plugin != null && plugin.isEnabled();
    }
    
    public boolean isPluginEnabled(String pluginName) {
        Plugin plugin = getPlugin(pluginName);
        return plugin != null && plugin.isEnabled();
    }
    
    public boolean isTagAPIEnabled() {
        Plugin plugin = getPlugin("TagAPI");
        return isPluginEnabled(plugin) && plugin instanceof TagAPI;
    }
    
    public boolean isTownyEnabled() {
        Plugin plugin = getPlugin("Towny");
        return isPluginEnabled(plugin) && plugin instanceof Towny;
    }
    
    public boolean isVanishEnabled() {
        Plugin plugin = getPlugin("VanishNoPacket");
        return isPluginEnabled(plugin) && plugin instanceof VanishPlugin;
    }
    
    public boolean isVaultEnabled() {
        Plugin plugin = getPlugin("Vault");
        return isPluginEnabled(plugin) && plugin instanceof Vault;
    }
}
