
package net.sehales.secon.utils.plugin;

import java.util.Set;

import net.sehales.secon.SeCon;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

public class TagAPIUtils {
    
    public class TagAPIListener implements Listener {
        
        private TagAPIListener() {
        }
        
        @EventHandler()
        public void onPlayerNameTagReceiveEvent(PlayerReceiveNameTagEvent e) {
            String namedPlayerName = e.getNamedPlayer().getName();
            
            // rework player api with those values
            String nametag = null;
            String prefix = null;
            
            e.setTag(prefix != null ? prefix : "" + nametag != null ? nametag : namedPlayerName);
        }
    }
    
    private TagAPIUtils() { // deny default constructor access
    }
    
    TagAPIUtils(SeCon secon) {
        Bukkit.getPluginManager().registerEvents(new TagAPIListener(), secon);
    }
    
    public void refreshPlayer(Player player) {
        TagAPI.refreshPlayer(player);
    }
    
    public void refreshPlayer(Player player, Player forPlayer) {
        TagAPI.refreshPlayer(player, forPlayer);
    }
    
    public void refreshPlayer(Player player, Set<Player> forPlayers) {
        TagAPI.refreshPlayer(player, forPlayers);
    }
}
