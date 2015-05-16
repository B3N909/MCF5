package me.mcf5.feat;

import java.util.ArrayList;
import java.util.List;

import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public class VoteKick implements Listener, CommandExecutor{
	
	
	private static MCF5 plugin;
	public VoteKick(MCF5 plugin){
		this.plugin = plugin;
	}
	
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player p = (Player) sender;
		
		if(cmd.getName().equalsIgnoreCase("votekick")){
			if(args.length == 0 || args.length >= 2){
				Util.sendMessage(p, "votekick username");
				return true;
			}
			boolean mode = plugin.getConfig().getBoolean("VoteKick.active");
			if(args[0].equalsIgnoreCase("yes")){
				if(mode && alreadyVoted(p) == false){
					setVoted(p);
					int talley = plugin.getConfig().getInt("VoteKick.yes");
					plugin.getConfig().set("VoteKick.yes", Integer.valueOf(talley + 1));
					plugin.saveConfig();
					Util.sendMessage(p, "You voted to kick.");
				}else{
					Util.sendMessage(p, "You already voted.");
				}
			}else if(args[0].equalsIgnoreCase("no")){
				if(mode && alreadyVoted(p) == false){
					setVoted(p);
					int talley = plugin.getConfig().getInt("VoteKick.no");
					plugin.getConfig().set("VoteKick.no", Integer.valueOf(talley + 1));
					plugin.saveConfig();
					Util.sendMessage(p, "You voted to not kick.");
				}else{
					Util.sendMessage(p, "You already voted.");
				}
			}else if(args[0].equalsIgnoreCase("admin")){
				plugin.getConfig().set("VoteKick.cooldown", Integer.valueOf(0));
				plugin.saveConfig();
			}else{
				
				try {
					if(Bukkit.getPlayerExact(args[0]).isOnline()){
						
					}
				} catch (Exception e) {
					Util.sendMessage(p, "Player is not online.");
					e.printStackTrace();
					return false;
				}
				
				
				//Player are online.
				int timeLeft = plugin.getConfig().getInt("VoteKick.cooldown");
				if(timeLeft == 0){
					
					plugin.getConfig().set("VoteKick.cooldown", Integer.valueOf(5));
					plugin.getConfig().set("VoteKick.active", Boolean.valueOf(true));
					plugin.getConfig().set("VoteKick.yes", Integer.valueOf(0));
					plugin.getConfig().set("VoteKick.no", Integer.valueOf(0));
					plugin.saveConfig();
					
					Util.broadcast(ChatColor.GREEN + p.getName() + " wants to kick " + args[0] + " use /votekick yes or no");
					
					final String player = args[0];
					Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
						public void run(){
							float yes = plugin.getConfig().getInt("VoteKick.yes");
							float no = plugin.getConfig().getInt("VoteKick.no");
							System.out.println("Yes: " + yes + " | No: " + no);
							if((yes / 2 ) > no){
								Bukkit.getPlayer(player).kickPlayer("There were 2x the amount of people wanting to kick you than to not.");
								
								plugin.getConfig().set("VoteKick.active", Boolean.valueOf(false));
								plugin.getConfig().set("VoteKick.yes", Integer.valueOf(0));
								plugin.getConfig().set("VoteKick.no", Integer.valueOf(0));
								plugin.saveConfig();
								
								
							}else{
								Util.broadcast(ChatColor.GREEN + "Kick did not have enough votes");
							}
						}
					},600L);
					
				}else{
					Util.sendMessage(p, "There is still a cooldown in effect.");
				}
			}
		}
		
		return false;
	}
	
	
	public void setVoted(Player p){
		if(alreadyVoted(p) == false){
			List<String> list = (List<String>)plugin.getConfig().getList("VoteKick.list");
			if(list == null)
				list = new ArrayList<String>();
			list.add(p.getUniqueId().toString().toLowerCase());
			plugin.getConfig().set("VoteKick.list", list);
			plugin.saveConfig();
		}
	}
	
	public boolean alreadyVoted(Player p){
		List<String> list = (List<String>)plugin.getConfig().getList("VoteKick.list");
		if(list == null){
			list = new ArrayList<String>();
		}
		String id = p.getUniqueId().toString().toLowerCase();
		for(String uuid : list){
			if(uuid.equalsIgnoreCase(id)){
				return true;
			}
		}
		return false;
	}
	
}
