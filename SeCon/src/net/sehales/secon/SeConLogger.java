
package net.sehales.secon;

import java.util.logging.Logger;

public class SeConLogger {
    
    private Logger log;
    
    SeConLogger(SeCon plugin) {
        this.log = plugin.getLogger();
    }
    
    private String getMessage(Object option, String msg) {
        if (option != null) {
            return "[" + option + "] " + msg;
        } else {
            return msg;
        }
    }
    
    public void info(Object option, String msg) {
        log.info(getMessage(option, msg));
    }
    
    public void println(Object option, String msg) {
        System.out.println(getMessage(option, msg));
    }
    
    public void severe(Object option, String msg) {
        log.severe(getMessage(option, msg));
    }
    
    public void warning(Object option, String msg) {
        log.warning(getMessage(option, msg));
    }
}
