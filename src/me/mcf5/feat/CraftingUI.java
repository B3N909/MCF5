package me.mcf5.feat;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import me.mcf5.main.Config;
import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.ItemDespawnEvent;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class CraftingUI implements Listener{
	
	
	
	
	
	
	private static final ItemStack[][] ItemStack = null;
	MCF5 plugin;
	public CraftingUI(MCF5 plugin){
		this.plugin = plugin;
	}
	

	
	
	
	
	
	
	
	ItemStack[] oldMatrix;
	ItemStack[] currentMatrix;
	
	
	
	
	
	//REMOVE DROPS ON BEAK
	@EventHandler
	public void onBreak(BlockBreakEvent e){
		if(e.getBlock().getType().equals(Material.WORKBENCH)){
			ClearCraftingDrops(e.getBlock().getLocation());
		}
	}
		
	
	
	boolean isHasClosed;
	//LOADING CRAFTING CONTENTS
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent e){
		org.bukkit.inventory.Inventory inv = e.getInventory();
		if(inv.getType() == InventoryType.WORKBENCH){
			final Player p = (Player) e.getPlayer();
			Location loc = p.getTargetBlock(null, 10).getLocation();
			smartSave((CraftingInventory)inv, loc);
			e.getInventory().clear();
			isHasClosed = true;
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					isHasClosed = false;
				}
			}, 7L);
		}
	}


	
	
	
	
	//SAVING CRAFTING CONTENTS
	@EventHandler
	public void onInventoryOpen(InventoryOpenEvent e){
		if(e.getInventory().getType() == InventoryType.WORKBENCH && e.getPlayer().getTargetBlock(null, 10).getType().equals(Material.WORKBENCH)){
			Player p = (Player)e.getPlayer();
			if(Conveyor.getSignsNear(p.getTargetBlock(null, 10).getLocation()) == null || Conveyor.getValue(Conveyor.getSignsNear(p.getTargetBlock(null, 10).getLocation()))){
				CraftingInventory inv = (CraftingInventory)e.getInventory();
				Location loc = e.getPlayer().getTargetBlock(null, 10).getLocation();
				inv.setMatrix(Get(loc));
				Update((Player)e.getPlayer(), inv.getMatrix());
				inv.setMatrix(LoreUpdate(inv.getMatrix()));
				oldMatrix = inv.getMatrix();
				currentMatrix = inv.getMatrix();		
				//InventoryCrafting handle1 = (InventoryCrafting)((CraftInventory)inv).getInventory();
				//WorldServer world = ((CraftWorld)Util.getDefWorld()).getHandle();
				//CraftingManager.getInstance().craft(handle1, world);
				//p.updateInventory();
				//InventoryCrafting handle = (InventoryCrafting)((CraftInventory)inv).getInventory();
				//int id = 0;
				//try {
				//	Field field = InventoryCrafting.class.getDeclaredField("d");
				//	field.setAccessible(true);
				//	Container container = (Container) field.get(handle);
				//	id = container.windowId;
				//} catch (Exception ex){
				//	ex.printStackTrace();
				//}
				//net.minecraft.server.v1_8_R1.ItemStack stack = CraftItemStack.asNMSCopy(inv.getItem(0));
				//PacketPlayOutSetSlot packet = new PacketPlayOutSetSlot(id, 0, stack);
				//CraftPlayer cp = (CraftPlayer) p;
				//cp.getHandle().playerConnection.sendPacket(packet);
			}else{
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				e.setCancelled(true);
			}
			
		}else if(e.getInventory().getType() == InventoryType.FURNACE && e.getPlayer().getTargetBlock(null, 10).getType().equals(Material.FURNACE)){
			Player p = (Player)e.getPlayer();
			if(Conveyor.getSignsNear(p.getTargetBlock(null, 10).getLocation()) == null || Conveyor.getValue(Conveyor.getSignsNear(p.getTargetBlock(null, 10).getLocation()))){
				
			}else{
				p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				e.setCancelled(true);
			}
		}
	}
	
	
	
	
	
	
	
	//SETTING CRAFTING ICONS UI
	@EventHandler
	public void onCrafting(final PrepareItemCraftEvent e){
		final Player p = (Player)e.getView().getPlayer();
		if(canRepeat() && e.getInventory().getMatrix() != null){
			oldMatrix = currentMatrix;
			currentMatrix = e.getInventory().getMatrix();
			Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
				public void run(){
					if(!(isHasClosed)){
						Update(p, e.getInventory().getMatrix()); //UPDATE
						e.getInventory().setMatrix(LoreUpdate(e.getInventory().getMatrix())); //FIX LORES
					}
				}
			}, 5L);
		}
	}
	
	
	
	
	
	
	//REMOVING CRAFTING ICONS UI
	@EventHandler
	public void onCrafted(final CraftItemEvent e){
		ClearCraftingDrops(ViewersToViewer(e.getInventory().getViewers()).getTargetBlock(null, 10).getLocation());
	}
	
	
	
	
	
	
	//GETS CURRENT VIEWER
	public Player ViewersToViewer(List<HumanEntity> viewers){
		return (Player) viewers.get(0);
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
				ClearCraftingDrops(p.getTargetBlock(null, 10).getLocation());
				ItemStack[] Matrix = oldMatrix.clone();
				for(ItemStack n : Matrix){
					if(n != null && n.getType() != Material.AIR){
						ItemStack m = n;
						List<String> lore = new ArrayList<String>();
						lore.add("CRAFTING" + i);
						ItemMeta meta = m.getItemMeta();
						meta.setLore(lore);
						m.setItemMeta(meta);
						if(p.getTargetBlock(null, 10).getType().equals(Material.WORKBENCH)){
							Location loc = p.getTargetBlock(null, 10).getLocation();
							Util.getDefWorld().dropItem(getCorner(loc.add(0.5, 1, 0.5), i), m).setVelocity(new Vector(0,0,0));
						}
						i++;
					}else{
						i++;
					}
				}
			}
		
		}, 5L);
	}
	
	public void Update(final Location l , final ItemStack[] oldMatrix){
		Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, new Runnable(){
			@Override
			public void run() {
				int i = 1;
				ClearCraftingDrops(l);
				ItemStack[] Matrix = oldMatrix.clone();
				for(ItemStack n : Matrix){
					if(n != null && n.getType() != Material.AIR){
						ItemStack m = n;
						List<String> lore = new ArrayList<String>();
						lore.add("CRAFTING" + i);
						ItemMeta meta = m.getItemMeta();
						meta.setLore(lore);
						m.setItemMeta(meta);
						Util.getDefWorld().dropItem(getCorner(l.add(0.5, 1, 0.5), i), m).setVelocity(new Vector(0,0,0));
						i++;
					}
				}
			}
		
		}, 5L);
	}
	
	
	
	
	
	
	//#REGION UTILS
	public void q(Location loc, ItemStack[] m){
		Config config = new Config("crafting", plugin);
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
		Config config = new Config("crafting", plugin);
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
	
	
	public void smartSave(CraftingInventory inv, Location loc){
		if(inv.getResult() != null)
			Save(loc, inv.getMatrix(), inv.getResult());
		else
			Save(loc, inv.getMatrix());
	}
	
	public void Save(Location loc, ItemStack[] m){
		Config config = new Config("crafting", plugin);
		config.Save();
		int i = 0;
		while(i <= 8){
			int newi = i+1;
			config.getConfig().set(toString(loc) + "." + newi, m[i]);
			config.Save();
			i++;
		}
	}
	
	public void Save(Location loc, ItemStack[] m, ItemStack output){
		Config config = new Config("crafting", plugin);
		config.Save();
		int i = 0;
		while(i <= 8){
			int newi = i+1;
			config.getConfig().set(toString(loc) + "." + newi, m[i]);
			config.Save();
			i++;
		}
		config.getConfig().set(toString(loc) + ".output", output);
		config.Save();
	}
	
	
	public static String toString(Location loc){
		return split(loc.getX() + "") + "," + split(loc.getY() + "") + "," + split(loc.getZ() + "");
	}
	
	
	
	
	public static String split(String s){
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
	//#END REGION
	
	
	
	
	
	//CLEARING CRAFTING ICONS UI
	public void ClearCraftingDrops(Location loc){
		for(Item entity : Util.getDefWorld().getEntitiesByClass(Item.class)){
			if(entity.getItemStack().hasItemMeta()){
				if(entity.getItemStack().getItemMeta().hasLore()){
					for(String s : entity.getItemStack().getItemMeta().getLore()){
						if(s.contains("CRAFTING")){
							if(entity.getLocation().distance(loc) < 2)
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
	
	
	
	
	
	//NO DESPAWN CRAFTING ICONS UI
	@EventHandler
	public void onDespawn(ItemDespawnEvent e){
		if(e.getEntity().getItemStack().hasItemMeta()){
			ItemStack en = e.getEntity().getItemStack();
			if(en.getItemMeta().hasLore()){
				for(String s : en.getItemMeta().getLore()){
					if(s.contains("CRAFTING")){
						e.setCancelled(true);
					}
				}
			}
		}
	}
	
	
	
	
	
	//ANTI-SPAM
	boolean repeat = false;
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
