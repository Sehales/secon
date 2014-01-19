package net.sehales.secon.itemmenu;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

/**
 * Class to display a menu with icons in a virtual inventory, based on http://forums.bukkit.org/threads/icon-menu.108342/
 * 
 * @author nisovin, Maddis, Sehales
 * 
 */
public class Menu implements Listener {

	private String                  name;
	private int                     size;
	private OptionClickEventHandler handler;
	private Plugin                  plugin;

	private MenuItem[]              items;

	private Inventory               inventory;
	private boolean                 destroyOnClose = true;

	public Menu(String name, int size, OptionClickEventHandler handler, Plugin plugin) {
		this.name = name;
		this.size = size;
		this.handler = handler;
		this.plugin = plugin;
		this.items = new MenuItem[size];
		inventory = Bukkit.createInventory(null, size, name);
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
	}

	public void clear() {
		this.inventory.clear();
		this.items = new MenuItem[this.size];
	}

	public void destroy() {
		HandlerList.unregisterAll(this);
		handler = null;
		plugin = null;
		items = null;
	}

	public MenuItem getItem(int i) {
		return items[i];
	}

	/**
	 * get the name of the inventory
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public boolean isAutoDestroyEnabled() {
		return destroyOnClose;
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClick(InventoryClickEvent event) {
		if (event.getInventory().getName().equals(name)) {
			event.setCancelled(true);
			int slot = event.getRawSlot();
			if (slot >= 0 && slot < size && items[slot] != null) {
				Plugin plugin = this.plugin;
				OptionClickEvent e = new OptionClickEvent((Player) event.getWhoClicked(), slot, items[slot].getLabel(), event);
				handler.onOptionClick(e);
				if (e.willClose()) {
					final Player p = (Player) event.getWhoClicked();
					Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {

						public void run() {
							p.closeInventory();
						}
					}, 1);
				}
				if (e.willDestroy())
					destroy();
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onInventoryClose(InventoryCloseEvent event) {
		if (event.getInventory().getName().equals(name) && isAutoDestroyEnabled())
			destroy();
	}

	public void open(Player player) {
		player.openInventory(inventory);
	}

	public void setAutoDestroyEnabled(boolean destroyOnClose) {
		this.destroyOnClose = destroyOnClose;
	}

	protected void setHandler(OptionClickEventHandler handler) {
		this.handler = handler;
	}

	public Menu setMenuItem(int position, ItemStack icon, String label, List<String> text) {
		return setMenuItem(position, new MenuItem(icon, name, text));
	}

	public Menu setMenuItem(int position, MenuItem item) {
		items[position] = item;
		this.inventory.setItem(position, item.getIcon());
		return this;
	}

}
