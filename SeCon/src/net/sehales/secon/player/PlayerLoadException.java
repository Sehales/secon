
package net.sehales.secon.player;

public class PlayerLoadException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public PlayerLoadException(String msg) {
        super(msg);
    }
    
    public PlayerLoadException(String msg, Throwable throwable) {
        super(msg, throwable);
    }
    
}
