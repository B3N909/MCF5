package me.mcf5.feat;

import me.mcf5.gui.UIDatabase;
import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class License implements Listener, CommandExecutor{
	
	MCF5 plugin;
	public License(MCF5 plugin){
		this.plugin = plugin;
	}
	
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String arg2, String[] args) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("license")){
			if(args[0].equalsIgnoreCase("check")){
				UIDatabase.Open(p, UIDatabase.license);
				Util.sendMessage(p, "Check current Licenses");
			}
		}
		return false;
	}

}
