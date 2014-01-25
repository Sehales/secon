
package net.sehales.secon.utils.plugin;

import java.util.Set;

import net.sehales.secon.SeCon;
import net.sehales.secon.player.SCPlayer;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.kitteh.tag.PlayerReceiveNameTagEvent;
import org.kitteh.tag.TagAPI;

public class TagAPIUtils {
    
    public class TagAPIListener implements Listener {
        
        TagAPIListener() {
        }
        
        @EventHandler()
        public void onPlayerNameTagReceiveEvent(PlayerReceiveNameTagEvent e) {
            String namedPlayerName = e.getNamedPlayer().getName();
            
            SCPlayer scp = SeCon.getInstance().getPlayerManager().getPlayer(e.getPlayer().getName());
            String nametag = scp.hasData(SCPlayer.KEY_TAGAPI_NAMETAG) ? scp.getValue(SCPlayer.KEY_TAGAPI_NAMETAG) : null;
            String prefix = scp.hasData(SCPlayer.KEY_TAGAPI_PREFIX) ? scp.getValue(SCPlayer.KEY_TAGAPI_PREFIX) : null;
            
            e.setTag(prefix != null ? prefix : "" + nametag != null ? nametag : namedPlayerName);
        }
    }
    
    private TagAPIListener listener = new TagAPIListener();
    
    private TagAPIUtils() { // deny default constructor access
    }
    
    TagAPIUtils(SeCon secon) {
        Bukkit.getPluginManager().registerEvents(listener, secon);
    }
    
    void disable() {
        PlayerReceiveNameTagEvent.getHandlerList().unregister(listener);
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
