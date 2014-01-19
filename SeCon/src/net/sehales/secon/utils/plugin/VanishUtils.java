
package net.sehales.secon.utils.plugin;

import org.bukkit.entity.Player;
import org.kitteh.vanish.VanishManager;
import org.kitteh.vanish.staticaccess.VanishNoPacket;
import org.kitteh.vanish.staticaccess.VanishNotLoadedException;

public class VanishUtils {
    
    VanishManager manager;
    
    VanishUtils() {
        try {
            manager = VanishNoPacket.getManager();
        } catch (VanishNotLoadedException e) {
            e.printStackTrace();
        }
    }
    
    public boolean isVanished(Player player) {
        return manager.isVanished(player);
    }
    
    public void toggleVanish(Player player) {
        manager.toggleVanish(player);
    }
    
    public void toggleVanishQuiet(Player player) {
        manager.toggleVanishQuiet(player, true);
    }
    
    public void toggleVanishQuietWithoutEffects(Player player) {
        manager.toggleVanishQuiet(player, false);
    }
}
