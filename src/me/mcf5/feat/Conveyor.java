package me.mcf5.feat;

import java.util.Arrays;
import java.util.logging.Level;
import me.mcf5.logic.ArrayI;
import me.mcf5.main.Config;
import me.mcf5.main.MCF5;
import me.mcf5.main.Util;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.block.Hopper;
import org.bukkit.block.Sign;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class Conveyor implements Listener {
	
	Config config = null;
	
	MCF5 plugin;
	public Conveyor(MCF5 plugin){
		this.plugin = plugin;
	}
	
	public static Sign getSignsNear(Location loc){
		Location modify;
		//LEFT
		modify = new Location(loc.getWorld(), loc.getX() - 1, loc.getY(), loc.getZ());
		if(modify.getBlock().getType().equals(Material.WALL_SIGN) && ChatColor.stripColor(((Sign)modify.getBlock().getState()).getLine(0)).equalsIgnoreCase("[VALVE]"))
			return (Sign)modify.getBlock().getState();
		//RIGHT
		modify = new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ());
		if(modify.getBlock().getType().equals(Material.WALL_SIGN) && ChatColor.stripColor(((Sign)modify.getBlock().getState()).getLine(0)).equalsIgnoreCase("[VALVE]"))
			return (Sign)modify.getBlock().getState();
		//FRONT
		modify = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() + 1);
		if(modify.getBlock().getType().equals(Material.WALL_SIGN) && ChatColor.stripColor(((Sign)modify.getBlock().getState()).getLine(0)).equalsIgnoreCase("[VALVE]"))
			return (Sign)modify.getBlock().getState();
		//BEHIND
		modify = new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1);
		if(modify.getBlock().getType().equals(Material.WALL_SIGN) && ChatColor.stripColor(((Sign)modify.getBlock().getState()).getLine(0)).equalsIgnoreCase("[VALVE]"))
			return (Sign)modify.getBlock().getState();
		return null;
	}
	
	public static void moveItemContainer(Block hopper){
		Location hopperLoc = hopper.getLocation(); 
		Block container = new Location(hopperLoc.getWorld(), hopperLoc.getX(), hopperLoc.getY() + 1, hopperLoc.getZ()).getBlock();
		if(container.getType() != null){ //IS NOT AIR
			if(container.getType().equals(Material.CHEST)){
				//AUTO MOVES, DO NOTHING
			}else if(container.getType().equals(Material.WORKBENCH) && getHopperConnected(hopper).getType().equals(Material.CHEST)){
				ItemStack[] craft = Get(container.getLocation());
				if(ArrayI.hasEnough(craft)){
					Save(container.getLocation(), ArrayI.removeEnough(craft));
					Chest chest = ((Chest) getHopperConnected(hopper).getState());
					chest.getInventory().addItem(GetOutput(container.getLocation()));
				}
			}
		}
	}
	
	
	
	
	
	public void moveItemTarget(Sign s, Block hopper){
		if(config == null)
			Initialize();
		if(getHopperConnected(hopper).getType().equals(Material.WORKBENCH)){
			if(hopper.getType().equals(Material.HOPPER)){
				Hopper h = (Hopper)hopper.getState();
				ItemStack[] contents = h.getInventory().getContents();
				if(getLast(contents) == null){
					Bukkit.getScheduler().cancelTask(config.getConfig().getInt(toString(s.getLocation()) + ".taskID"));
					return;
				}
				ItemStack[] craft = Get(getHopperConnected(hopper).getLocation());
				if(getLast(craft) == null){
					return;
				}else{
					
					ItemStack[]	one = new ItemStack[]{new ItemStack(getLast(contents).getType(), 1), new ItemStack(getSecondLast(contents).getType(), 1)};
					ItemStack[] two = craft;
					ArrayI.logEvenly(two);
					ArrayI.log(one);
					Save(getHopperConnected(hopper).getLocation(), ArrayI.combineEven(one, two)); //Update Crafting table with combined...
					h.getInventory().removeItem(new ItemStack(getLast(contents).getType(), 1));
					h.getInventory().removeItem(new ItemStack(getSecondLast(contents).getType(), 1));
					h.update();
					
					//Update Crafitng Table to Spread out Items
					//Save(getHopperConnected(hopper).getLocation(), ArrayI.compress(Get(getHopperConnected(hopper).getLocation())));
					
				}
			}
		}
	}
	
	
	public static ItemStack[] Get(Location loc){
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
	
	public static ItemStack GetOutput(Location loc){
		Config config = new Config("crafting");
		return config.getConfig().getItemStack(toString(loc) + ".output");
	}
	
	public static void Save(Location loc, ItemStack[] m){
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
	
	public static ItemStack getLast(ItemStack[] i){
		ItemStack l = null;
		for(ItemStack s : i)
			if(s != null)
				if(s.getAmount() != 0)
					l = s;
		return l;
	}
	
	public static ItemStack getSecondLast(ItemStack[] i){
		ItemStack l = null;
		ItemStack o = null;
		for(ItemStack s : i)	{
			if(s != null){
				if(s.getAmount() != 0){
					o = l;
					l = s;
				}
			}
		}
		if(o == null)
			return l;
		return o;
	}
	
	public static Block getHopperConnected(Block hopper){
		Location l = hopper.getLocation();
		if(face(hopper) == 2){
			//LEFT -1 Z
			return new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() - 1).getBlock();
		}else if(face(hopper) == 5){
			//BEHIND -1 X
			return new Location(l.getWorld(), l.getX() - 1, l.getY(), l.getZ()).getBlock();
		}else if(face(hopper) == 3){
			//RIGHT +1 Z
			return new Location(l.getWorld(), l.getX(), l.getY(), l.getZ() + 1).getBlock();
		}else if(face(hopper) == 4){
			//FRONT +1 X
			return new Location(l.getWorld(), l.getX() + 1, l.getY(), l.getZ()).getBlock();
		}else{
			//DOWN -1 Y
			return new Location(l.getWorld(), l.getX(), l.getY() - 1, l.getZ()).getBlock();
		}
	}
	
	public static Block getAttached(Sign s){
		org.bukkit.material.Sign sign = (org.bukkit.material.Sign) s.getBlock().getState().getData();
		return s.getBlock().getRelative(sign.getAttachedFace());
	}
	
	
	@SuppressWarnings("deprecation")
	public static int face(Block b){
		return (int)b.getData();
	}
	
	//CHANGE SIGN ON POWER
	@EventHandler
	public void onBlockPhysicsEvent(BlockPhysicsEvent e){
		if(e.getBlock().getType().equals(Material.WALL_SIGN) && e.getBlock().getBlockPower() != 0){
			Sign s = (Sign)e.getBlock().getState();
			if(ChatColor.stripColor(s.getLine(0)).equalsIgnoreCase("[VALVE]") && canRepeat()){
				if(ChatColor.stripColor(s.getLine(1)).equalsIgnoreCase("true")){
					s.setLine(1, ChatColor.BLUE + "false");
					update(s, "false");
				}else{
					s.setLine(1, ChatColor.BLUE + "true");
					update(s, "true");
				}
			}
		}
			
	}
	
	
	public void update(final Sign s, String value){
		if(config == null) //START CONFIG
			Initialize();
		final String mode = ChatColor.stripColor(value); //TAKE AWAY COLOR
		s.update(); //UPDATE SIGN WITH NEW VALUES
		if(mode.equalsIgnoreCase("true")){ //CREATE NEW TASK OR KILL EXISTING TASK
			int task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new Runnable(){
				public void run(){
					moveItemTarget(s, getAttached(s));
					moveItemContainer(getAttached(s));
				}
			}, 0L, 20L);
			config.getConfig().set(toString(s.getLocation()) + ".taskID", Integer.valueOf(task));
			config.Save();
		}else{
			int TaskID = config.getConfig().getInt(toString(s.getLocation()) + ".taskID");
			if(TaskID != 0){
				Bukkit.getScheduler().cancelTask(TaskID);
			}
		}
	}
	
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
	
	public void Initialize(){
		config = new Config("valve");
	}
	
	
	//REGION UTIL
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
	//END REGION
	
	public static boolean getValue(Location valve){
		return Boolean.parseBoolean(ChatColor.stripColor(((Sign)valve.getBlock().getState()).getLine(1)));
	}
	
	public static boolean getValue(Sign s){
		return Boolean.parseBoolean(ChatColor.stripColor(s.getLine(1)));
	}
	
	
	@EventHandler
	public void onSignChange(SignChangeEvent  e){
		if(e.getLines()[0].equalsIgnoreCase("[valve]")){
			e.setLine(0, ChatColor.BLUE + "[VALVE]");
			e.setLine(1, ChatColor.BLUE + "false");
			((Sign)e.getBlock().getState()).update();
		}
	}
	
	@EventHandler
	public void onInteract(PlayerInteractEvent e){ 
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			@SuppressWarnings("deprecation")
			Block b = e.getPlayer().getTargetBlock(null, 10);
			if(b.getType().equals(Material.WALL_SIGN)){
				Sign sign = (Sign)b.getState();
				if(ChatColor.stripColor(sign.getLine(0)).equalsIgnoreCase("[VALVE]")){
					String value = ChatColor.stripColor(sign.getLine(1));
					if(value.equalsIgnoreCase("false")){
						sign.setLine(1, ChatColor.BLUE + "true");
						update(sign, "true");
					}else{
						sign.setLine(1, ChatColor.BLUE + "false");
						update(sign, "false");
					}
				}
			}
		}
	}
	
}
