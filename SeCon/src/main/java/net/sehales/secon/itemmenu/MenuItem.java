
package net.sehales.secon.itemmenu;

import java.util.LinkedList;
import java.util.List;

import net.sehales.secon.utils.mc.ItemUtils;

import org.bukkit.inventory.ItemStack;

public class MenuItem {
    
    private ItemStack          icon;
    private String             label;
    private LinkedList<String> text = new LinkedList<String>();
    
    public MenuItem(ItemStack icon, String label, List<String> text) {
        setIcon(icon);
        setLabel(label);
        setText(new LinkedList<String>(text));
    }
    
    public void addFirstTextLine(String line) {
        text.addFirst(line);
        setText(text);
    }
    
    public void addLastTextLine(String line) {
        text.addLast(line);
        setText(text);
    }
    
    public void addTextLine(String line) {
        text.add(line);
        setText(text);
    }
    
    public ItemStack getIcon() {
        return icon;
    }
    
    public String getLabel() {
        return label;
    }
    
    public List<String> getText() {
        return text;
    }
    
    public void removeTextLine(int lineNumber) {
        text.remove(lineNumber);
        setText(text);
    }
    
    public void removeTextLine(String line) {
        text.remove(line);
        setText(text);
    }
    
    public void setIcon(ItemStack icon) {
        this.icon = icon;
        setText(this.text);
        setLabel(this.label);
    }
    
    public void setLabel(String label) {
        this.label = label;
        this.icon = ItemUtils.setItemName(icon, label);
    }
    
    public void setText(List<String> text) {
        this.text = new LinkedList<String>(text);
        this.icon = ItemUtils.setItemLore(icon, text);
    }
}
