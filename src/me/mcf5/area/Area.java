package me.mcf5.area;

import me.mcf5.main.Config;
import me.mcf5.main.Util;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public class Area {
	
	public static FileConfiguration fc = null;
	
	private String name;
	private String enter;
	private String exit;
	private Cuboid pos;
	
	public Area(String name, Cuboid area){
		if(fc == null)
			fc = new Config("area").getConfig();
		this.name = name;
		if(isDefined(name)){
			this.pos = area;
			this.enter = getStat(name, "enter");
			this.exit = getStat(name, "exit");
		}
	}
	
	private boolean isDefined(String n){
		if(fc.getConfigurationSection(n) == null)
			return false;
		return true;
	}
	
	private String getStat(String name, String stat){
		return fc.getString(name + "." + stat);
	}
	
	public Cuboid getCuboid(String n){
		Location pos1 = new Location(Util.getDefWorld(), fc.getVector(n + ".pos1").getX(), fc.getVector(n + ".pos1").getY(), fc.getVector(n + ".pos1").getZ());
		Location pos2 = new Location(Util.getDefWorld(), fc.getVector(n + ".pos2").getX(), fc.getVector(n + ".pos2").getY(), fc.getVector(n + ".pos2").getZ());
		return new Cuboid(pos1, pos2);
	}
	
	
	
	
	public boolean inArea(Location targetLocation, Location inAreaLocation1, Location inAreaLocation2){
	    if(inAreaLocation1.getWorld().getName() == inAreaLocation2.getWorld().getName())
	        if(targetLocation.getWorld().getName() == inAreaLocation1.getWorld().getName())
	            if((targetLocation.getBlockX() >= inAreaLocation1.getBlockX() && targetLocation.getBlockX() <= inAreaLocation2.getBlockX()) || (targetLocation.getBlockX() <= inAreaLocation1.getBlockX() && targetLocation.getBlockX() >= inAreaLocation2.getBlockX()))
	                if((targetLocation.getBlockZ() >= inAreaLocation1.getBlockZ() && targetLocation.getBlockZ() <= inAreaLocation2.getBlockZ()) || (targetLocation.getBlockZ() <= inAreaLocation1.getBlockZ() && targetLocation.getBlockZ() >= inAreaLocation2.getBlockZ()))
	                    return true;
	    return false;
	}
	
}
