package me.mcf5.gui;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.mcf5.gui.IconMenu.OptionClickEvent;
import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

public class UIDatabase implements Listener {
	
	static MCF5 plugin;
	public UIDatabase(MCF5 plugin){
		this.plugin = plugin;
	}
	
	
	public static IconMenu license;
	public static void Initialize(){
		boolean driver;
		license = new IconMenu("Current License(s)", 9, new IconMenu.OptionClickEventHandler(){
			@Override
			public void onOptionClick(OptionClickEvent e) {
				e.getPlayer().closeInventory();
			}
			
		}, plugin);
	}
	
	
	
	public static void Open(Player p, IconMenu menu){
		menu.open(p);
	}
	
	@EventHandler
	public void onInventroyOpen(InventoryOpenEvent e){
		if(e.getInventory().getName().equalsIgnoreCase("Current License(s)")){
			Inventory inv = e.getInventory();
			Player p = (Player) e.getPlayer();
			if(p.hasPermission("ucars.cars")){
				inv.addItem(me.mcf5.main.Inventory.setName(new ItemStack(Material.MINECART, 10), ChatColor.GOLD + "Drivers License"));
			}else{
				inv.addItem(me.mcf5.main.Inventory.setName(new ItemStack(Material.MINECART, 1), ChatColor.RED + "[Missing] Drivers License"));
			}
			if(p.hasPermission("crackshot.use.*")){
				inv.addItem(me.mcf5.main.Inventory.setName(new ItemStack(Material.MINECART, 10), ChatColor.GOLD + "Firearms License"));
			}else{
				inv.addItem(me.mcf5.main.Inventory.setName(new ItemStack(Material.MINECART, 1), ChatColor.RED + "[Missing] Firearms License"));
			}
			if(p.hasPermission("truck.use")){
				inv.addItem(me.mcf5.main.Inventory.setName(new ItemStack(Material.MINECART, 10), ChatColor.GOLD + "Trucking License"));
			}else{
				inv.addItem(me.mcf5.main.Inventory.setName(new ItemStack(Material.MINECART, 1), ChatColor.RED + "[Missing] Trucking License"));
			}
			inv.addItem(me.mcf5.main.Inventory.setName(new ItemStack(Material.MINECART, 1), ChatColor.RED + "[Missing] Politician License"));
		}
	}
	
	@EventHandler
	public void onInventoryClick(InventoryClickEvent e){
		if(e.getInventory().getName().equalsIgnoreCase("Current License(s)")){
			if(e.isShiftClick()){
				e.setCancelled(true);
				Util.sendMessage((Player)e.getWhoClicked(), "Stop cheatin'");
			}
		}
	}
		
}
