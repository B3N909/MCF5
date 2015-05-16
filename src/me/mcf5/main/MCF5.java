package me.mcf5.main;

import me.mcf5.feat.Broadcaster;
import me.mcf5.feat.Chat;
import me.mcf5.feat.CraftingUI;
import me.mcf5.feat.Door;
import me.mcf5.feat.DoorListener;
import me.mcf5.feat.License;
import me.mcf5.feat.MenuUI;
import me.mcf5.feat.Refine;
import me.mcf5.feat.VoteKick;
import me.mcf5.gui.UIDatabase;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MCF5 extends JavaPlugin implements Listener{
	
	Plugin plugin;
	
	@SuppressWarnings("deprecation")
	public void onEnable(){
		saveConfig();
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, this);
        pm.registerEvents(new DoorListener(pm), this);
        pm.registerEvents(new Door(this), this);
        pm.registerEvents(new Broadcaster(this), this);
		pm.registerEvents(new VoteKick(this), this);
        pm.registerEvents(new Chat(), this);
		pm.registerEvents(new MiscListener(this), this);
		pm.registerEvents(new Refine(this), this);
		pm.registerEvents(new License(this), this);
		pm.registerEvents(new UIDatabase(this), this);
		pm.registerEvents(new CraftingUI(this), this);
		pm.registerEvents(new MenuUI(this), this);
		pm.registerEvents(new Chat(), this);
		
		UIDatabase.Initialize();
		
        this.plugin = Bukkit.getPluginManager().getPlugin("MCF5");
        
        Logger.Initialize();
		
		
		getCommand("reset").setExecutor(new DoorListener(pm));	
		getCommand("delete").setExecutor(new DoorListener(pm));	
		getCommand("key").setExecutor(new DoorListener(pm));	
		getCommand("public").setExecutor(new DoorListener(pm));	
		getCommand("broadoff").setExecutor(new Broadcaster(this));
		getCommand("votekick").setExecutor(new VoteKick(this));
		getCommand("refine").setExecutor(new Refine(this));
		getCommand("license").setExecutor(new License(this));
		getCommand("chat").setExecutor(new Chat());
		
		//Auto broadcaster
		String prefix = getConfig().getString("prefix");
		if(prefix == null){
			getConfig().set("prefix", String.valueOf("&eCity"));
			saveConfig();
			prefix = getConfig().getString("prefix");
		}
		Util.setPrefix(getConfig().getString("prefix"));
		
		String world = getConfig().getString("defWorld");
		if(world == null){
			getConfig().set("defWorld", String.valueOf("city"));
			saveConfig();
			world = getConfig().getString("defWorld");
		}
		Util.setDefWorld(getConfig().getString("defWorld"));
		
		Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){
			
			public void run(){
				//Will run every 2 minutes
				Broadcaster.Broadcast();			
			}
			
		}, 0L, 2400L);
		
		Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(plugin, new Runnable(){
			
			public void run(){
				//Will run every 1 minute
				int timeLeft = getConfig().getInt("VoteKick.cooldown");
				int c = timeLeft;
				if(timeLeft - 1 <= 0){
					c = 0;
				}
				getConfig().set("VoteKick.cooldown", Integer.valueOf(c));
				saveConfig();
			}
			
		}, 0L, 1200L);

	}
	public void onDisable(){
		//saveConfig();
	}
	
	
	@EventHandler
	public void onCommand(PlayerCommandPreprocessEvent e){
		Logger.log("[cmd]: " + e.getPlayer().getName().toLowerCase() + " ran " + e.getMessage().toString());
	}
	
}
