
package net.sehales.secon.player;

public class PlayerSaveException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public PlayerSaveException(String msg) {
        super(msg);
    }
    
    public PlayerSaveException(String msg, Throwable t) {
        super(msg, t);
    }
}
