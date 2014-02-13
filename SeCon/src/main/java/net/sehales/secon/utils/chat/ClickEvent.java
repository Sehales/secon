
package net.sehales.secon.utils.chat;

import org.apache.commons.lang.Validate;
import org.json.simple.JSONObject;

public class ClickEvent {
    
    public enum Type {
        SUGGEST_COMMAND, RUN_COMMAND, OPEN_URL
    }
    
    private Type   type = Type.SUGGEST_COMMAND;
    private String value;
    
    public ClickEvent() {
        
    }
    
    public ClickEvent(Type type, String value) {
        Validate.notNull(value);
        
        this.type = type;
        this.value = value;
    }
    
    public Type getType() {
        return type;
    }
    
    public String getValue() {
        return value;
    }
    
    public void setText(String value) {
        Validate.notNull(value);
        
        this.value = value;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("action", type.toString().toLowerCase());
        obj.put("value", value);
        return obj;
    }
}
