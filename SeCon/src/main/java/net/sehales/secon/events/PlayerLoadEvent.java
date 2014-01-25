
package net.sehales.secon.events;

import net.sehales.secon.player.SCPlayer;

import org.bukkit.event.HandlerList;

/**
 * this event will be called direclty after all data of a player has been loaded
 * 
 * @author Sehales
 * 
 */
public class PlayerLoadEvent extends SeConPlayerEvent {
    
    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public PlayerLoadEvent(SCPlayer player) {
        super(player);
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
}
