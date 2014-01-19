
package net.sehales.secon.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.sehales.secon.utils.mc.ChatUtils;
import net.sehales.secon.utils.mc.ItemUtils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemConfig extends Config {
    
    public ItemConfig(File configFile) {
        super(configFile);
    }
    
    /**
     * add a new item to items.yml
     * 
     * @param name
     * @param stack
     * @return false if and only if the given name is not available else true,
     */
    @SuppressWarnings("deprecation")
    public boolean addItem(String name, ItemStack stack) {
        if (contains(name)) {
            return false;
        }
        add(name + ".id", stack.getTypeId());
        add(name + ".damage", stack.getDurability());
        add(name + ".amount", stack.getAmount());
        ItemMeta meta = stack.getItemMeta();
        if (meta.hasDisplayName()) {
            add(name + ".name", meta.getDisplayName());
        }
        
        if (meta.hasLore()) {
            add(name + ".lore", meta.getLore());
        }
        
        if (meta.hasEnchants()) {
            List<String> enchants = new ArrayList<String>();
            Map<Enchantment, Integer> temp = meta.getEnchants();
            for (Enchantment e : meta.getEnchants().keySet()) {
                enchants.add(e.getName() + ":" + temp.get(e));
            }
            add(name + ".enchantments", enchants);
        }
        save();
        return true;
    }
    
    /**
     * get a common or custom defined itemstack
     * 
     * @param value
     * @return null if nothing could be found
     */
    @SuppressWarnings("deprecation")
    public ItemStack getItem(String value) {
        ItemStack stack = null;
        int id = 0;
        short damage = 0;
        
        if (contains(value)) {
            if (contains(value + ".id")) {
                id = getInt(value + ".id");
            }
            if (contains(value + ".damage")) {
                damage = getShort(value + ".damage");
            } else if (contains(value + ".durability")) {
                damage = getShort(value + ".durability");
            }
            
            int amount = 1;
            
            if (contains(value + ".amount")) {
                amount = getInt(value + ".amount");
            }
            stack = new ItemStack(id, amount, damage);
            
            if (contains(value + ".name")) {
                ItemUtils.setItemName(stack, ChatUtils.formatMessage(getString(value + ".name")));
            }
            if (contains(value + ".lore")) {
                List<String> lore = new ArrayList<String>();
                for (String s : getStringList(value + ".lore")) {
                    lore.add(ChatUtils.formatMessage(s));
                }
                stack = ItemUtils.setItemLore(stack, lore);
            }
            if (contains(value + ".enchantments")) {
                for (String s : getStringList(value + ".enchantments")) {
                    String[] args = s.split(":");
                    String name = args[0];
                    int lvl = Integer.parseInt(args[1]);
                    stack = ItemUtils.addItemEnchantment(stack, Enchantment.getByName(name), lvl, true);
                }
            }
        } else {
            try {
                id = Integer.parseInt(value);
                stack = new ItemStack(id);
            } catch (Exception e) {
                try {
                    stack = new ItemStack(Material.getMaterial(value.toUpperCase()));
                } catch (Exception e1) {
                    return null;
                }
            }
        }
        return stack;
    }
    
}
