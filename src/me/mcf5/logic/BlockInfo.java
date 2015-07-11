package me.mcf5.logic;

import me.mcf5.main.Config;
import me.mcf5.main.MCF5;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class BlockInfo implements CommandExecutor {
	
	Location location;
	String information;
	
	public BlockInfo(Location loc, String command, MCF5 plugin){
		this.location = loc;
		this.information = command;
		Config c = new Config("block", plugin);
		FileConfiguration cfg = c.getConfig();
		cfg.set(toString(loc), String.valueOf(command));
		c.Save();
	}
	
	MCF5 plugin;
	public BlockInfo(MCF5 plugin){
		this.plugin = plugin;
	}
	
	private String toString(Location loc){
		String s = loc.getX() + "," + loc.getY() + "," + loc.getZ();
		s = s.replace(".", "");
		return s;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg1, String[] args) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("block")){
			if(p.isOp()){
				if(args.length != 0){
					String arguments = "";
					for(String s : args)
						arguments += " " + s;
					BlockInfo block = new BlockInfo(p.getTargetBlock(null, 100).getLocation(), arguments, plugin);
					p.sendMessage(ChatColor.BLUE + "Added " + ChatColor.GOLD + arguments + ChatColor.BLUE + " to block " + ChatColor.BLUE + block.location.getBlock().getType().toString());
				}
			}
		}
		return false;
	}
}