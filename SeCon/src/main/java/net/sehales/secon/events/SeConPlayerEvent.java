
package net.sehales.secon.events;

import net.sehales.secon.player.SCPlayer;

import org.bukkit.event.Event;

public abstract class SeConPlayerEvent extends Event {
    
    private SCPlayer player;
    
    public SeConPlayerEvent(SCPlayer player) {
        this.player = player;
    }
    
    public SCPlayer getPlayer() {
        return player;
    }
}
