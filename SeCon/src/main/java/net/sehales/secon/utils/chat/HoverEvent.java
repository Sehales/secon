
package net.sehales.secon.utils.chat;

import org.apache.commons.lang.Validate;
import org.json.simple.JSONObject;

public class HoverEvent {
    
    public enum Type {
        SHOW_TEXT, SHOW_ACHIEVEMENT, SHOW_ITEM
    }
    
    private Type   type = Type.SHOW_TEXT;
    private String value;
    
    // private ItemStack item;
    
    public HoverEvent() {
        
    }
    
    // public HoverEvent(Type type, ItemStack item) {
    // Validate.isTrue(type == Type.ITEM);
    // Validate.notNull(item);
    //
    // this.type = type;
    // this.item = item;
    // }
    
    public HoverEvent(Type type, String value) {
        Validate.notNull(value);
        
        this.type = type;
        this.value = value;
    }
    
    // public ItemStack getItem() {
    // return item;
    // }
    
    public String getText() {
        return value;
    }
    
    public Type getType() {
        return type;
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        obj.put("action", type.toString().toLowerCase());
        switch (type) {
        // case SHOW_ITEM: {
        // String nbtString = "";
        // obj.put("value", nbtString)
        // break;
        // }
            default: {
                obj.put("value", value);
                break;
            }
        }
        return obj;
    }
    
    public String toJsonString() {
        return toJson().toJSONString();
    }
    
}
