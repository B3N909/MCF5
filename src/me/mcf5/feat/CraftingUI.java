package me.mcf5.feat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

import me.mcf5.main.Config;
import me.mcf5.main.Inventory;
import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

public class CraftingUI implements Listener{
	private static final ItemStack[][] ItemStack = null;
	MCF5 plugin;
	public CraftingUI(MCF5 plugin){
		this.plugin = plugin;
	}

	
	//LOADING CRAFTING CONTENTS
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		org.bukkit.inventory.Inventory inv = e.getInventory();
		if(inv.getType() == InventoryType.WORKBENCH){
			Player p = (Player) e.getPlayer();
			Location loc = p.getTargetBlock(null, 10).getLocation();
			Save(loc, ((CraftingInventory) e.getInventory()).getMatrix());
			e.getInventory().clear();
		}
	}
	
	//SAVING CRAFTING CONTENTS
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e){
		if(e.getInventory().getType() == InventoryType.WORKBENCH){
			CraftingInventory inv = (CraftingInventory)e.getInventory();
			Location loc = e.getPlayer().getTargetBlock(null, 10).getLocation();
			inv.setMatrix(Get(loc));
			Update((Player)e.getPlayer(), inv.getMatrix());
			inv.setMatrix(LoreUpdate(inv.getMatrix()));
		}
	}
	
	//SETTING CRAFTING ICONS UI
	@EventHandler
	public void onCrafting(final PrepareItemCraftEvent e){
		final Player p = (Player)e.getView().getPlayer();
		if(canRepeat() && e.getInventory().getMatrix() != null){
			Update(p, e.getInventory().getMatrix());
			e.getInventory().setMatrix(LoreUpdate(e.getInventory().getMatrix()));
		}
	}
	
	//REMOVING CRAFTING ICONS UI
	@EventHandler
	public void onCrafted(CraftItemEvent e){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run() {
				ClearCraftingDrops();
			}
			
		}, 10L);
	}
	
	//REMOVED ITEM LORE FROM MATRIX AFTER DROPS
	public ItemStack[] LoreUpdate(ItemStack[] i){
		ItemStack[] newList = new ItemStack[i.length];
		int list = 0;
		for(ItemStack m : i){
			if(m == null) return i;
			if(m.hasItemMeta()){
				if(m.getItemMeta().hasLore()){
					for(String s : m.getItemMeta().getLore()){
						if(s.contains("CRAFTING")){
							List<String> lore = new ArrayList<String>();
							ItemMeta meta = m.getItemMeta();
							meta.setLore(lore);
							m.setItemMeta(meta);
							Bukkit.getLogger().info(m.getItemMeta().getLore().get(0));
						}
					}
				}
			}
			newList[list] = m;
			list++;
		}
		return i;
	}
	
	//UPDATE CRAFTING ICONS UI
	@SuppressWarnings("deprecation")
	public void Update(final Player p , final ItemStack[] oldMatrix){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run() {
				int i = 1;
				ClearCraftingDrops();
				ItemStack[] Matrix = oldMatrix.clone();
				for(ItemStack n : Matrix){
					if(n != null && n.getType() != Material.AIR){
						ItemStack m = n;
						List<String> lore = new ArrayList<String>();
						lore.add("CRAFTING" + i);
						ItemMeta meta = m.getItemMeta();
						meta.setLore(lore);
						m.setItemMeta(meta);
						Location loc = p.getTargetBlock(null, 10).getLocation();
						Util.getDefWorld().dropItem(getCorner(loc.add(0.5, 1, 0.5), i), m).setVelocity(new Vector(0,0,0));
						i++;
					}
				}
			}
		
		}, 5L);
	}
	
	public void Save(Location loc, ItemStack[] m){
		Config config = new Config("crafting");
		config.Save();
		int i = 0;
		while(i <= 8){
			int newi = i+1;
			config.getConfig().set(toString(loc) + "." + newi, m[i]);
			config.Save();
			i++;
		}
	}
	
	public ItemStack[] Get(Location loc){
		Config config = new Config("crafting");
		config.Save();
		ItemStack[] al = new ItemStack[9];
		int i = 0;
		while(i <= 8){
			int newi = i+1;
			al[i] = config.getConfig().getItemStack(toString(loc) + "." + newi + "");
			i++;
		}
		return al;
	}
	
	
	public String toString(Location loc){
		return split(loc.getX() + "") + "," + split(loc.getY() + "") + "," + split(loc.getZ() + "");
	}
	
	public String split(String s){
		return s.split("\\.", 2)[0];
	}
	
	public Location toLocation(String s){
		Location loc = null;
		loc.setWorld(Util.getDefWorld());
		int i = 0;
		for(String l : Arrays.asList(s.split(","))){
			if(i == 0){
				loc.setX(Double.parseDouble(l));
			}else if(i == 1){
				loc.setY(Double.parseDouble(l));
			}else if(i == 2){
				loc.setZ(Double.parseDouble(l));
			}
		}
		return loc;
	}
	
	
	
	public Location getCorner(Location loc, int i){
		if(i == 1){
			return new Location(loc.getWorld(), loc.getX() -0.35, loc.getY(), loc.getZ() + 0.35);
		}else if(i == 2){
			return new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 0.35);
		}else if(i == 3){
			return new Location(loc.getWorld(), loc.getX() +0.35, loc.getY(), loc.getZ() + 0.35);
		}else if(i == 4){
			return new Location(loc.getWorld(), loc.getX() -0.35, loc.getY(), loc.getZ());
		}else if(i == 5){
			return new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ());
		}else if(i == 6){
			return new Location(loc.getWorld(), loc.getX() +0.35, loc.getY(), loc.getZ());
		}else if(i == 7){
			return new Location(loc.getWorld(), loc.getX() -0.35, loc.getY(), loc.getZ() - 0.35);
		}else if(i == 8){
			return new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 0.35);
		}else if(i == 9){
			return new Location(loc.getWorld(), loc.getX() +0.35, loc.getY(), loc.getZ() - 0.35);
		}
		return loc;
			
	}
	
	//CLEARING CRAFTING ICONS UI
	public void ClearCraftingDrops(){
		for(Item entity : Util.getDefWorld().getEntitiesByClass(Item.class)){
			if(entity.getItemStack().hasItemMeta()){
				if(entity.getItemStack().getItemMeta().hasLore()){
					for(String s : entity.getItemStack().getItemMeta().getLore()){
						if(s.contains("CRAFTING")){
							entity.remove();
						}
					}
				}
			}
		}
	}
	
	//SETTING CRAFTING ICONS UI NON-INTERACTABLE
	@EventHandler
	public void onItemPickup(PlayerPickupItemEvent e){
		if(e.getItem().getItemStack().hasItemMeta()){
			for(String s : e.getItem().getItemStack().getItemMeta().getLore()){
				if(s.contains("CRAFTING")){
					e.setCancelled(true);
				}
			}
		}
	}
	
	//ANTI-SPAM
	boolean repeat = false;
	@SuppressWarnings("deprecation")
	public boolean canRepeat(){
		if(repeat == false){
			repeat = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				@Override
				public void run() {
					repeat = false;					
				}
				
			}, 10L);
			return true;
		}
		return false;
	}
	
}
