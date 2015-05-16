package me.mcf5.feat;

import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class Broadcaster implements Listener, CommandExecutor{
	

	private static MCF5 plugin;
	public Broadcaster(MCF5 plugin){
		this.plugin = plugin;
	}
	
	public static void Broadcast() {
		int max = plugin.getConfig().getInt("Broadcast.max");
		int i = plugin.getConfig().getInt("Broadcast.i");
		int c = i + 1;
		if(c > max)
			c = 1;
		//System.out.println("C:" + c + " | I: " + i + " | Max: " + max);
		String message = plugin.getConfig().getString("Broadcast." + c);
		plugin.getConfig().set("Broadcast.i", Integer.valueOf(c));
		plugin.saveConfig();
		AutoMessage(message);
	}
	
	public static void AutoMessage(String msg){
		for(Player p : Bukkit.getOnlinePlayers()){
			boolean value = plugin.getConfig().getBoolean("Player." + p.getUniqueId().toString().toLowerCase() + ".broadcast");
			if(value){
				//Do not broadcast
			}else{
				//Broadcast
				Util.sendMessage(p, msg);
			}
		}
	}

	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("broadoff")){
			if(args.length == 0){
				Util.sendMessage(p, "broadoff <value(off/on)> - Toogles off Auto-Broadcaster tips.");
				return true;
			}
			if(args[0].equalsIgnoreCase("off")){
				plugin.getConfig().set("Player." + p.getUniqueId().toString().toLowerCase() + ".broadcast", Boolean.valueOf(true));
				Util.sendMessage(p, "Tips turned off.");
			}else if(args[0].equalsIgnoreCase("on")){
				plugin.getConfig().set("Player." + p.getUniqueId().toString().toLowerCase() + ".broadcast", Boolean.valueOf(false));
				Util.sendMessage(p, "Tips turned on.");
			}else{
				Util.sendMessage(p, "broadoff <value(off/on)> - Toogles off Auto-Broadcaster tips.");
			}
			
		}
		return false;
	}
	
	
	
	
	
}
