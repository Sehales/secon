
package net.sehales.secon.events;

import net.sehales.secon.player.SCPlayer;

import org.bukkit.event.HandlerList;

public class PlayerOnlineEvent extends SeConPlayerEvent {
    
    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public PlayerOnlineEvent(SCPlayer player) {
        super(player);
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
}
