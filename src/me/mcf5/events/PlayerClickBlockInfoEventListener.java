package me.mcf5.events;

import me.mcf5.main.Config;
import me.mcf5.main.MCF5;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.util.Vector;

public class PlayerClickBlockInfoEventListener implements Listener{
	MCF5 plugin;
	public PlayerClickBlockInfoEventListener(MCF5 plugin){
		this.plugin = plugin;
	}
	//Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinBoatEvent(p, (Boat) entity));
	@EventHandler
	public void interact(PlayerInteractEvent e){
		if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)){
			Config c = new Config("block", plugin);
			FileConfiguration cfg = c.getConfig();
			Location loc = e.getClickedBlock().getLocation();
			for(String s : cfg.getConfigurationSection("").getKeys(false)){
				if(toString(loc).equalsIgnoreCase(s)){
					Bukkit.getServer().getPluginManager().callEvent(new PlayerClickBlockInfoEvent(e.getPlayer(), e.getClickedBlock(), cfg.getString(s)));
				}
			}
		}
	}
	
	private String toString(Location loc){
		String s = loc.getX() + "," + loc.getY() + "," + loc.getZ();
		s = s.replace(".", "");
		return s;
	}
	
	@EventHandler
	public void click(PlayerClickBlockInfoEvent e){
		if(e.getInformation().contains("/")){ 
			String command = e.getInformation().substring(1);
			command = command.replace("/", "");
			System.out.println(command);
			Bukkit.dispatchCommand((CommandSender) e.getPlayer(), command); 
		}
	}
}
