package net.sehales.secon.itemmenu;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.inventory.InventoryClickEvent;

public class OptionClickEvent extends Event {

	public static HandlerList getHandlerList() {
		return handlers;
	}

	private Player                   player;
	private int                      position;
	private String                   name;
	private boolean                  close;
	private InventoryClickEvent      originalEvent;
	private boolean                  destroy;

	private static final HandlerList handlers = new HandlerList();

	public OptionClickEvent(Player player, int position, String name, InventoryClickEvent originalEvent) {
		this.player = player;
		this.position = position;
		this.name = name;
		this.originalEvent = originalEvent;
		this.close = false;
		this.destroy = false;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public String getItemName() {
		return name;
	}

	public InventoryClickEvent getOriginalEvent() {
		return this.originalEvent;
	}

	public Player getPlayer() {
		return player;
	}

	public int getPosition() {
		return position;
	}

	public void setWillClose(boolean close) {
		this.close = close;
	}

	public void setWillDestroy(boolean destroy) {
		this.destroy = destroy;
	}

	public boolean willClose() {
		return close;
	}

	public boolean willDestroy() {
		return destroy;
	}
}