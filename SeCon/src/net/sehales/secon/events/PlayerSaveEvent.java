
package net.sehales.secon.events;

import net.sehales.secon.player.SCPlayer;

import org.bukkit.event.HandlerList;

/**
 * this event will be called direclty after the player has been saved and before
 * the player will be removed from the player list
 * 
 * @author Sehales
 * 
 */
public class PlayerSaveEvent extends SeConPlayerEvent {
    
    private static final HandlerList handlers = new HandlerList();
    
    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    public PlayerSaveEvent(SCPlayer player) {
        super(player);
    }
    
    @Override
    public HandlerList getHandlers() {
        return handlers;
    }
    
}
