package me.mcf5.main;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Util implements Listener{
	
	public static String prefix;
	public static World world;
	
	public static void setPrefix(String msg){
		prefix = colorize(msg);
	}
	
	public static String colorize(String line){
		return line.replaceAll("&([a-f0-9])", ChatColor.COLOR_CHAR + "$1");
	}
	
	public static void sendMessage(Player p, String msg){
		p.sendMessage(prefix + ChatColor.WHITE + " " + msg);
		
	}
	
	public static void broadcast(String msg){
		for(Player p : Bukkit.getOnlinePlayers()){
			p.sendMessage(prefix + ChatColor.WHITE + " " + msg);
		}
	}
	
	public static World getDefWorld(){
		if(world == null){
			world = Bukkit.getWorld("city");
		}
		return world;
	}
	public static void setDefWorld(String name){
		world = Bukkit.getWorld(name);
	}
}
