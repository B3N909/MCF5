package me.mcf5.feat;

import me.mcf5.main.Config;
import me.mcf5.main.Logger;
import me.mcf5.main.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChatTabCompleteEvent;

public class Chat implements Listener, CommandExecutor{
	
	//TALK LOCAL
	@EventHandler
	public void ChatMsg(AsyncPlayerChatEvent e){
		String msg = e.getMessage();
		Player p = e.getPlayer();
		ChatMsg(msg, p, false);
		e.setCancelled(true);
	}
	
	//TALK GLOBAL
	@EventHandler
	public void onTabChat(PlayerChatTabCompleteEvent  e){
		Player p = e.getPlayer();
		String m = e.getChatMessage();
		e.getTabCompletions().clear();
		Chat.ChatMsg(m, p, true);
		org.bukkit.inventory.Inventory inv = Bukkit.getServer().createInventory(null, 9);
		p.openInventory(inv);
		p.closeInventory();
	}
	
	//TALK MSG
	public static void ChatMsg(String msg, Player p, boolean isPublic){
		if(p.getWorld().getName().equalsIgnoreCase("city")){
			Config config = new Config("chat"); //REGISTERING CONFIGURATION
			config.Save();
			
			if(isPublic){ //LOGGING MESSAGES
				//PUBLIC
				Logger.log("[global] - " + p.getName().toLowerCase() + " said - " + msg);
			}else{
				//NON-PUBLIC
				Logger.log("[local] - " + p.getName().toLowerCase() + " said - " + msg);
			}
			
			for(Player p1 : Bukkit.getOnlinePlayers()){
				if(isPublic){
					//CHECK FOR DISABLED
					if(config.getConfig().getBoolean(p.getName().toLowerCase().toString() + ".global")){
						Util.sendMessage(p, "Global Chat forced-on");
						config.getConfig().set(p.getName().toLowerCase().toString() + ".global", false);
						config.Save();
					}
					if(config.getConfig().getBoolean(p1.getName().toLowerCase().toString() + ".global")) { return; }
					if(p1.equals(p)){
						p.sendMessage(ChatColor.GRAY + "" + "[G]" + ChatColor.UNDERLINE + p.getName().toString() + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.GRAY + msg);
					}else{
						p1.sendMessage(ChatColor.GRAY + "[G]" + p.getName().toString() + ":" + ChatColor.WHITE + msg);
					}
				}else{
					Bukkit.getLogger().info(p1.getName().toLowerCase());
					//CHECK FOR DISTANCE
					if(p.getLocation().distance(p1.getLocation()) < 20 || p.getLocation().distance(p1.getLocation()) > -20){
						if(p1.equals(p)){
							p.sendMessage(ChatColor.GRAY + "" + "[L]" + ChatColor.UNDERLINE + p.getName().toString() + ChatColor.RESET + ChatColor.GRAY + ": " + ChatColor.GRAY + msg);
						}else{
							p1.sendMessage(ChatColor.GRAY + "[L]" + p.getName().toString() + ":" + ChatColor.WHITE + msg);
						}
					}
				}
			}
		}
	}
	
	//TURN OFF GLOBAL
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String arg2, String[] args) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("chat")){
			if(args.length == 1){
				if(args[0].equalsIgnoreCase("off")){
					Config config = new Config("chat");
					config.Save();
					config.getConfig().set(p.getName().toLowerCase().toString() + ".global", true);
					config.Save();
					Util.sendMessage(p, "Global Chat off");
				}else if(args[0].equalsIgnoreCase("on")){
					Config config = new Config("chat");
					config.Save();
					config.getConfig().set(p.getName().toLowerCase().toString() + ".global", false);
					config.Save();
					Util.sendMessage(p, "Global Chat on");
				}else{
					Util.sendMessage(p, "chat <value(off/on)> - turns off global chat.");
				}
			}else{
				Util.sendMessage(p, "chat <value(off/on)> - turns off global chat.");
			}
		}
		return false;
	}
	
}
