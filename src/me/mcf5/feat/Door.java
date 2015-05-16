package me.mcf5.feat;

import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;

public class Door implements Listener{
	
	public static Location loc;
	public static int ID;
	public static String owner;
	public static boolean mode;
	
	
	//Constructor
	private static MCF5 plugin;
	public Door(MCF5 plugin){
		this.plugin = plugin;
	}
	
	public Door(Location loc, Player p){
		if(isDoor(loc)){
			//Already made
			System.out.println("Already made.");
			String name = getID(loc);
			
			/** Setting Location **/
			int x = plugin.getConfig().getInt("Door." + name + ".location.x");
			int y = plugin.getConfig().getInt("Door." + name + ".location.y");
			int z = plugin.getConfig().getInt("Door." + name + ".location.z");
			Location bl = new Location(Util.getDefWorld(), x, y, z);
			this.loc = bl;
			/** Setting Variables **/
			this.owner = plugin.getConfig().getString("Door." + name + ".owner");
			this.mode = plugin.getConfig().getBoolean("Door." + name + ".mode");
			this.ID = plugin.getConfig().getInt("Door." + name + ".ID");
			
		}else{
			//Note made yet
			System.out.println("Note made yet");
			register(loc, p);
		}
	}
	
	public void register(Location loc, Player p){
		int key = plugin.getConfig().getInt("Door.doors") + 1;
		String name = "Door" + key;
		this.ID = key;
		this.loc = loc;
		this.owner = p.getUniqueId().toString();
		this.mode = false;
		plugin.getConfig().set("Door." + name + ".location.x", Integer.valueOf((int) loc.getX()));
		plugin.getConfig().set("Door." + name + ".location.y", Integer.valueOf((int) loc.getY()));
		plugin.getConfig().set("Door." + name + ".location.z", Integer.valueOf((int) loc.getZ()));
		plugin.getConfig().set("Door." + name + ".mode", false);
		plugin.getConfig().set("Door." + name + ".ID", Integer.valueOf(key));
		plugin.getConfig().set("Door." + name + ".owner", String.valueOf(this.owner));
		plugin.getConfig().set("Door.doors", Integer.valueOf(key));
		plugin.saveConfig();
	}
	
	public Door(){
		
	}
	
	public boolean isDoor(Location loc){
		//Check if the door is already created based on Locations.
		if(plugin.getConfig().getConfigurationSection("Door") == null)
			return false;
		
		for(Object key1 : plugin.getConfig().getConfigurationSection("Door").getKeys(false)){
			//System.out.println(key1);
			if(key1.toString().toLowerCase().equalsIgnoreCase("doors")){
				//System.out.println("REMOVED KEYS");
			}else{
				int x = plugin.getConfig().getInt("Door." + key1.toString() + ".location.x");
				int y = plugin.getConfig().getInt("Door." + key1.toString() + ".location.y");
				int z = plugin.getConfig().getInt("Door." + key1.toString() + ".location.z");
				Location bl = new Location(Util.getDefWorld(), x, y, z);
				if(bl.equals(loc)){
					//System.out.println("YUPP!");
					return true;
				}else{
					//System.out.println("NOPE");
				}
				//System.out.println(x);
			}
		}
		return false;
	}
	

	
	public static String getID(Location loc){
		if(plugin.getConfig().getConfigurationSection("Door") == null)
			return "";
		
		for(Object key1 : plugin.getConfig().getConfigurationSection("Door").getKeys(false)){
			//System.out.println(key1);
			if(key1.toString().toLowerCase().equalsIgnoreCase("doors")){
				//System.out.println("REMOVED KEYS");
			}else{
				int x = plugin.getConfig().getInt("Door." + key1.toString() + ".location.x");
				int y = plugin.getConfig().getInt("Door." + key1.toString() + ".location.y");
				int z = plugin.getConfig().getInt("Door." + key1.toString() + ".location.z");
				Location bl = new Location(Util.getDefWorld(), x, y, z);
				if(bl.equals(loc)){
					//System.out.println("YUPP!");
					return key1.toString();
				}else{
					//System.out.println("NOPE");
				}
				//System.out.println(x);
			}
		}
		return "";
	}
	
	public static int getRawID(Location loc){
		if(plugin.getConfig().getConfigurationSection("Door") == null)
			return 0;
		
		for(Object key1 : plugin.getConfig().getConfigurationSection("Door").getKeys(false)){
			//System.out.println(key1);
			if(key1.toString().toLowerCase().equalsIgnoreCase("doors")){
				//System.out.println("REMOVED KEYS");
			}else{
				int x = plugin.getConfig().getInt("Door." + key1.toString() + ".location.x");
				int y = plugin.getConfig().getInt("Door." + key1.toString() + ".location.y");
				int z = plugin.getConfig().getInt("Door." + key1.toString() + ".location.z");
				Location bl = new Location(Util.getDefWorld(), x, y, z);
				if(bl.equals(loc)){
					//System.out.println("YUPP!");
					return plugin.getConfig().getInt("Door." + key1.toString() + ".ID");
				}else{
					//System.out.println("NOPE");
				}
				//System.out.println(x);
			}
		}
		return 0;
	}
	
	public static Location getLoc() {
		return loc;
	}
	public static void setLoc(Location loc) {
		Door.loc = loc;
	}
	public static int getID() {
		return ID;
	}
	public static void setID(int iD) {
		ID = iD;
	}
	public static String getOwner() {
		return owner;
	}
	public static void setOwner(String owner) {
		Door.owner = owner;
	}
	public static boolean isMode() {
		return mode;
	}
	public static void setMode(boolean mode) {
		Door.mode = mode;
		plugin.getConfig().set("Door.Door" + ID + ".mode", mode);
		plugin.saveConfig();
	}
	
	public static void reset(){
		plugin.getConfig().set("Door.doors", Integer.valueOf(1));
		
	}
	
	
	public static void loadID(int ID1){
		if(plugin.getConfig().getConfigurationSection("Door.Door" + ID1) != null){
			int x = plugin.getConfig().getInt("Door.Door" + ID1 + ".location.x");
			int y = plugin.getConfig().getInt("Door.Door" + ID1 + ".location.y");
			int z = plugin.getConfig().getInt("Door.Door" + ID1 + ".location.z");
			loc = new Location(Util.getDefWorld(), x, y, z);
			ID = ID1;
			mode = plugin.getConfig().getBoolean("Door.Door" + ID1 + ".mode");
			owner = plugin.getConfig().getString("Door.Door" + ID1 + ".owner");
		}
	}
	
	
}
