
package net.sehales.secon.utils.chat;

import org.apache.commons.lang.Validate;
import org.json.simple.JSONObject;

public class ClickEvent {
    
    public enum Type {
        SUGGEST_COMMAND, RUN_COMMAND, OPEN_URL
    }
    
    public static final int VALUE_JSON  = 0;
    public static final int VALUE_PLAIN = 1;
    
    private Type            type        = Type.SUGGEST_COMMAND;
    private String          value;
    private int             valueType   = VALUE_PLAIN;
    private MsgPart         valuePart;
    
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
    
    public MsgPart getValuePart() {
        return valuePart;
    }
    
    public int getValueType() {
        return valueType;
    }
    
    public ClickEvent setType(Type type) {
        this.type = type;
        return this;
    }
    
    public ClickEvent setValue(MsgPart part) {
        Validate.notNull(part);
        
        this.valueType = VALUE_JSON;
        this.valuePart = part;
        part.setClickEvent(null);
        part.setHoverEvent(null);
        return this;
    }
    
    public ClickEvent setValue(String value) {
        Validate.notNull(value);
        
        this.valueType = VALUE_PLAIN;
        this.value = value;
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("action", type.toString().toLowerCase());
        switch (valueType) {
            case VALUE_PLAIN: {
                obj.put("value", value);
                break;
            }
            case VALUE_JSON: {
                obj.put("value", valuePart.toJson());
                break;
            }
        }
        return obj;
    }
}
