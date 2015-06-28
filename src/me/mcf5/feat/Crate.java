package me.mcf5.feat;

import java.util.ArrayList;
import java.util.Random;

import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class Crate implements Listener, CommandExecutor{
	
	MCF5 plugin;
	public Crate(MCF5 plugin){
		this.plugin = plugin;
	}
	
	private void createCrate(Location loc){
		loc.getBlock().setType(Material.DIAMOND_BLOCK);
		new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ()).getBlock().setType(Material.LOG);
		new Location(loc.getWorld(), loc.getX(), loc.getY(), loc.getZ() - 1).getBlock().setType(Material.LOG);
		new Location(loc.getWorld(), loc.getX() + 1, loc.getY(), loc.getZ() - 1).getBlock().setType(Material.WOOD);
		new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ()).getBlock().setType(Material.WOOD_STAIRS);
		new Location(loc.getWorld(), loc.getX() + 1, loc.getY() + 1, loc.getZ() - 1).getBlock().setType(Material.WOOD_STAIRS);
		new Location(loc.getWorld(), loc.getX(), loc.getY() + 1, loc.getZ() - 1).getBlock().setType(Material.WOOD_STEP);
		new Location(loc.getWorld(), loc.getX() + 1, loc.getY() + 1, loc.getZ()).getBlock().setType(Material.WOOD_STEP);
		
	}
	
	public Vector getPos(Location loc){
		return new Vector(loc.getX(), loc.getY(), loc.getZ());
	}
	
	int taskID1;
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		final Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("forcecrate")){
			if(p.isOp()){
				Util.sendMessage(p, "Started Search for location...");
				startLocationSearching();
				taskID1 = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable(){
					public void run(){
						if(loc1 != null){
							Util.sendMessage(p, "Crate dropping at X: " + loc1.getX() + ", Y: " + loc1.getY() + ", Z: " + loc1.getZ());
							createCrate(loc1);
							Bukkit.getScheduler().cancelTask(taskID1);
							Bukkit.getScheduler().cancelTask(taskID);
						}
					}
				}, 20L, 20L);
				return true;
			}else{
				Util.sendMessage(p, "You do not have permission.");
			}
		}
		return false;
	}
	public Location loc1 = null;
	int taskID;
	@SuppressWarnings("deprecation")
	public void startLocationSearching(){
		this.taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, new BukkitRunnable(){
			public void run(){
				Location loc = randomLoc();
				if(isValid(loc)){
					Bukkit.getScheduler().cancelTask(taskID);
					loc1 = loc;
				}
			}
		}, 5L, 5L);
	}
	
	private boolean isValid(Location loc){
		if(loc.getBlock().getType() == Material.AIR){
			ArrayList<Location> list = new ArrayList<>();
			Location one = loc;
			Location two = loc.add(new Vector(1, 1, -1));
			if(isEmpty(contains(one, two)))
				if(loc.subtract(new Vector(0, 1, 0)).getBlock().getType() != Material.AIR && loc.subtract(new Vector(0, 1, 0)).getBlock().getType() != Material.WATER){
					Bukkit.getLogger().info(loc.subtract(new Vector(0, 1, 0)).getBlock().getType().toString() + " < UNDER CRATE");
					return true;
				}else
					Bukkit.getLogger().info("Invalid Under Block: " + loc.subtract(new Vector(0, 1, 0)).getBlock().getType().toString());
			else
				Bukkit.getLogger().info("Not Empty");
		}
		return false;
	}
	
	private boolean isEmpty(ArrayList<Location> list){
		boolean retrn = false;
		for(Location loc : list)
			if(loc.getBlock().getType() == Material.AIR)
				retrn = true;
			else
				retrn = false;
		return retrn;
	}
	
	private ArrayList<Location> contains(Location one, Location two){
		ArrayList<Location> list = new ArrayList<>();
		int minX = (int) Math.min(one.getX(), two.getX());
		int maxX = (int) Math.max(one.getX(), two.getX());
		int minY = (int) Math.min(one.getY(), two.getY());
		int maxY = (int) Math.max(one.getY(), two.getY());
		int minZ = (int) Math.min(one.getZ(), two.getZ());
		int maxZ = (int) Math.max(one.getZ(), two.getZ());
		for(int x = minX; x <= maxX; x++){
			for(int y = minY; y <= maxY; y++){
				for(int z = minZ; z <= maxZ; z++){
					list.add(new Location(Util.getDefWorld(), x, y, z));
				}
			}
		}
		return list;
	}
	
	private Location randomLoc(){
		Random rand = new Random();
		int min = -100;
		int max = 100;
		int x = rand.nextInt((max - min) + 1) + min;
		int z = rand.nextInt((max - min) + 1) + min;
		int y = Util.getDefWorld().getHighestBlockYAt(x, z);
		return new Location(Util.getDefWorld(), x, y, z);
	}
	
}
