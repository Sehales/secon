
package net.sehales.secon.utils.mc;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

public class ItemUtils {
    
    /**
     * add an enchantment to this item
     * 
     * @param item
     * @param enchantment
     * @param level
     * @param ignoreRestrictions
     * @return the enchanted item
     */
    public static ItemStack addItemEnchantment(ItemStack item, Enchantment enchantment, int level, boolean ignoreRestrictions) {
        ItemMeta metaData = item.getItemMeta();
        metaData.addEnchant(enchantment, level, ignoreRestrictions);
        item.setItemMeta(metaData);
        return item;
    }
    
    public static ItemStack addStoredItemEnchantment(ItemStack item, Enchantment enchantment, int level, boolean ignoreRestrictions) {
        EnchantmentStorageMeta metaData = (EnchantmentStorageMeta) item.getItemMeta();
        metaData.addStoredEnchant(enchantment, level, ignoreRestrictions);
        item.setItemMeta(metaData);
        return item;
    }
    
    /**
     * get the head of a player
     * 
     * @param playerName
     * @return the player's head
     */
    public static ItemStack getPlayerHead(String playerName) {
        ItemStack item = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        SkullMeta metaData = (SkullMeta) item.getItemMeta();
        metaData.setOwner(playerName);
        item.setItemMeta(metaData);
        return item;
    }
    
    /**
     * set the lore(description) of an item
     * 
     * @param item
     * @param test
     * @return the changed item
     */
    public static ItemStack setItemLore(ItemStack item, List<String> loreLines) {
        ItemMeta metaData = item.getItemMeta();
        metaData.setLore(loreLines);
        item.setItemMeta(metaData);
        return item;
    }
    
    /**
     * set the display name of an item
     * 
     * @param item
     * @param name
     * @return the changed item
     */
    public static ItemStack setItemName(ItemStack item, String name) {
        ItemMeta metaData = item.getItemMeta();
        metaData.setDisplayName(name);
        item.setItemMeta(metaData);
        return item;
    }
    
}
