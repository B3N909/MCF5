package me.mcf5.feat;

import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

public class MenuUI implements Listener{
	MCF5 plugin;
	public MenuUI(MCF5 plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e){
		if(e.getInventory().getType() == InventoryType.PLAYER){
			Player p = (Player)e.getPlayer();
			p.closeInventory();
			Util.sendMessage(p, "Opened Inventory.");
		}
	}
	
}
