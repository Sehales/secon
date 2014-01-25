
package net.sehales.secon.player;

public interface PlayerFactory {
    
    public SCPlayer loadPlayer(String name) throws PlayerLoadException;
    
    public void savePlayer(SCPlayer player) throws PlayerSaveException;
}
