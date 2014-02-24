
package net.sehales.secon.utils.chat;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang.Validate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class MsgPart {
    
    public static final int TEXT_JSON  = 0;
    public static final int TEXT_PLAIN = 1;
    private int             textType   = TEXT_PLAIN;
    private String          text       = "";
    private MsgPart         textPart   = null;
    private MsgColor        color;
    private ClickEvent      clickEvent;
    private HoverEvent      hoverEvent;
    private boolean         bold, underlined, italic, strikethrough, obfuscated;
    private boolean         decorated  = true;
    private List<MsgPart>   parts      = new LinkedList<>();
    
    public MsgPart() {
    }
    
    public MsgPart(MsgPart text) {
        this.textPart = text;
        this.textType = TEXT_JSON;
    }
    
    public MsgPart(String text) {
        this.text = text;
    }
    
    public MsgPart addPart(MsgPart part) {
        parts.add(part);
        return this;
    }
    
    public ClickEvent getClickEvent() {
        return clickEvent;
    }
    
    public MsgColor getColor() {
        return color;
    }
    
    public HoverEvent getHoverEvent() {
        return hoverEvent;
    }
    
    public MsgPart getPart(int index) {
        return parts.get(index);
    }
    
    public List<MsgPart> getParts() {
        return parts;
    }
    
    public String getText() {
        return text;
    }
    
    public MsgPart getTextPart() {
        return textPart;
    }
    
    public int getTextType() {
        return textType;
    }
    
    public boolean isBold() {
        return bold;
    }
    
    public boolean isItalic() {
        return italic;
    }
    
    public boolean isObfuscated() {
        return obfuscated;
    }
    
    public boolean isStrikethrough() {
        return strikethrough;
    }
    
    public boolean isUnderlined() {
        return underlined;
    }
    
    public MsgPart setBold(boolean bold) {
        this.bold = bold;
        return this;
    }
    
    public MsgPart setClickEvent(ClickEvent clickEvent) {
        this.clickEvent = clickEvent;
        return this;
    }
    
    public MsgPart setColor(MsgColor color) {
        this.color = color;
        return this;
    }
    
    public MsgPart setDecorated(boolean decorated) {
        this.decorated = decorated;
        return this;
    }
    
    public MsgPart setHoverEvent(HoverEvent hoverEvent) {
        this.hoverEvent = hoverEvent;
        return this;
    }
    
    public MsgPart setItalic(boolean italic) {
        this.italic = italic;
        return this;
    }
    
    public MsgPart setObfuscated(boolean obfuscated) {
        this.obfuscated = obfuscated;
        return this;
    }
    
    public MsgPart setStrikethrough(boolean strikethrough) {
        this.strikethrough = strikethrough;
        return this;
    }
    
    public MsgPart setText(MsgPart part) {
        Validate.notNull(part);
        this.textPart = part;
        part.setClickEvent(null);
        part.setHoverEvent(null);
        return this;
    }
    
    public MsgPart setText(String text) {
        Validate.notNull(text);
        this.text = text;
        this.textType = TEXT_PLAIN;
        return this;
    }
    
    public MsgPart setUnderlined(boolean underlined) {
        this.underlined = underlined;
        return this;
    }
    
    @SuppressWarnings("unchecked")
    public JSONObject toJson() {
        JSONObject obj = new JSONObject();
        
        switch (textType) {
            case TEXT_JSON: {
                obj.put("text", textPart);
                break;
            }
            case TEXT_PLAIN: {
                obj.put("text", text);
                break;
            }
        }
        if (decorated) {
            obj.put("bold", bold);
            obj.put("italic", italic);
            obj.put("strikethrough", strikethrough);
            obj.put("obfuscated", obfuscated);
            obj.put("underlined", underlined);
        }
        
        if (color != null) {
            obj.put("color", color.toString().toLowerCase());
        }
        
        if (clickEvent != null) {
            obj.put("clickEvent", clickEvent.toJson());
        }
        
        if (hoverEvent != null) {
            obj.put("hoverEvent", hoverEvent.toJson());
        }
        
        if (parts.size() > 0) {
            JSONArray extra = new JSONArray();
            for (MsgPart part : parts) {
                extra.add(part.toJson());
            }
            obj.put("extra", extra);
        }
        return obj;
    }
    
    public String toJsonString() {
        return toJson().toJSONString();
    }
}
