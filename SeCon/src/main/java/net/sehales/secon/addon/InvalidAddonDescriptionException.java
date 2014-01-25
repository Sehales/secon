
package net.sehales.secon.addon;

public class InvalidAddonDescriptionException extends Exception {
    
    private static final long serialVersionUID = 1L;
    
    public InvalidAddonDescriptionException(String msg) {
        super(msg);
    }
    
    public InvalidAddonDescriptionException(String msg, Throwable t) {
        super(msg, t);
    }
    
    public InvalidAddonDescriptionException(Throwable t) {
        super(t);
    }
    
}
