
package net.sehales.secon.utils.chat;

import org.apache.commons.lang.Validate;
import org.json.simple.JSONObject;

public class HoverEvent {
    
    public enum Type {
        SHOW_TEXT, SHOW_ACHIEVEMENT, SHOW_ITEM
    }
    
    public static final int VALUE_JSON  = 0;
    public static final int VALUE_PLAIN = 1;
    
    private Type            type        = Type.SHOW_TEXT;
    private String          value;
    private int             valueType   = VALUE_PLAIN;
    private MsgPart         valuePart;
    
    // private ItemStack item;
    
    public HoverEvent() {
    }
    
    public HoverEvent(Type type, String value) {
        Validate.notNull(value);
        
        this.type = type;
        this.value = value;
    }
    
    public String getText() {
        return value;
    }
    
    public Type getType() {
        return type;
    }
    
    // public ItemStack getItem() {
    // return item;
    // }
    
    // public HoverEvent(Type type, ItemStack item) {
    // Validate.isTrue(type == Type.ITEM);
    // Validate.notNull(item);
    //
    // this.type = type;
    // this.item = item;
    // }
    public int getValueType() {
        return valueType;
    }
    
    public HoverEvent setType(Type type) {
        this.type = type;
        return this;
    }
    
    public HoverEvent setValue(MsgPart part) {
        Validate.notNull(part);
        
        this.valueType = VALUE_JSON;
        this.valuePart = part;
        part.setClickEvent(null);
        part.setHoverEvent(null);
        return this;
    }
    
    public HoverEvent setValue(String value) {
        this.value = value;
        this.valueType = VALUE_PLAIN;
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
    
    public String toJsonString() {
        return toJson().toJSONString();
    }
    
}
