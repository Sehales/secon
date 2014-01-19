
package net.sehales.secon.utils.mc;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class TeleportUtils {
    
    public static void teleport(Player player, Location target, boolean fly, boolean creative) {
        if (!player.getWorld().equals(target.getWorld())) {
            if (creative) {
                
            } else if (fly) {
                
            } else {
                player.teleport(target, TeleportCause.COMMAND);
            }
        }
    }
    
    private TeleportUtils() {
    }
}
