package me.mcf5.feat;

import me.confuser.barapi.BarAPI;
import me.mcf5.main.Inventory;
import me.mcf5.main.MCF5;
import me.mcf5.main.Util;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

public class Refine implements Listener, CommandExecutor{
	
	static MCF5 plugin;
	public Refine(MCF5 plugin){
		this.plugin = plugin;
	}
	
	static int timerID;
	static int delayID;
	
	@SuppressWarnings("deprecation")
	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String arg2, String[] arg3) {
		if(cmd.getName().equalsIgnoreCase("refine")){
			Bukkit.getLogger().info(arg3[1].toString());
			final Player p = Bukkit.getPlayer(arg3[1]);
			if(arg3[0].equalsIgnoreCase("1")){
				if(plugin.getConfig().getBoolean(p.getName().toLowerCase() + ".refine") == true){
					Util.sendMessage(p, "You already are refining...");
					return true;
				}
				plugin.getConfig().set(p.getName().toLowerCase() + ".refine", true);
				plugin.saveConfig();
				startRefine(p, new Material[] { Material.COAL_ORE, Material.IRON_ORE, Material.DIAMOND_ORE }, new Material[]{ Material.COAL, Material.IRON_INGOT, Material.DIAMOND });
			}
			if(arg3[0].equalsIgnoreCase("reset")){
				plugin.getConfig().set(p.getName().toLowerCase() + ".refine", false);
				plugin.saveConfig();
			}
			if(arg3[0].equalsIgnoreCase("stop")){
				Bukkit.getScheduler().cancelTask(delayID);
				Bukkit.getScheduler().cancelTask(timerID);
				Util.sendMessage(p, "You moved to far, Refining Cancelled");
				BarAPI.removeBar(p);
				plugin.getConfig().set(p.getName().toLowerCase() + ".refine", false);
				plugin.saveConfig();
			}
		}
		return false;
	}
	
	@SuppressWarnings("deprecation")
	public static void startRefine(final Player p, final Material[] items, final Material[] refined){
		final Inventory inv = new Inventory(p);
		int itemAmount = 0; //Find Amount of Refinable Items for Delay Calculations
		for(Material i : items)
			if(inv.hasItem(i))
				itemAmount += inv.getSize(i);
		if(itemAmount == 0){
			Util.sendMessage(p, "Nothing to refine");
			return;
		}
		long delay = itemAmount * 6; //Calculating Delay
		final long start = System.currentTimeMillis(); //Start time to calculate Delay percentage
		final long end = (delay / 20) * 1000; //Estimated Delay end time in System.millaseconds
		final BukkitTask percentCalc = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable(){
			public void run(){
				long time = System.currentTimeMillis()-start;
				float percent = ((float)time / end) * 100; //Calculating Boss Bar Percent based on estimated end time
				BarAPI.removeBar(p); //Updating Boss Bar /via BarAPI
				BarAPI.setMessage(p, "Refining... " + (int)percent + "%");
				BarAPI.setHealth(p, percent);
			}
		}, 0L, 5L);
		final float finalItemAmount = itemAmount;
		int delayCalc = Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
			public void run(){
				float cut = finalItemAmount * .15f; //Calculating cut based off '0.15'
				float gain = finalItemAmount - (finalItemAmount * .15f); //Calculating gain based off cut
				Util.sendMessage(p, "You got " + (int)gain + " minerals back, " + (int)cut + " were lost");
				int f = 0;
				for(Material i : items){ //Removing all Refinable Items & Giving back gain
					if(inv.hasItem(i)){
						inv.giveItem(i, refined[f], inv.getSize(i),(int) ((int)inv.getSize(i) - (inv.getSize(i) * .15f)));
					}
					f++;
				}
				plugin.getConfig().set(p.getName().toLowerCase() + ".refine", false);
				plugin.saveConfig();
				BarAPI.removeBar(p); //Reset Boss Bar
				Bukkit.getScheduler().cancelTask(percentCalc.getTaskId()); //Cancells boss bar percentage calculations
			}
		}, delay);
		timerID = percentCalc.getTaskId();
		delayID = delayCalc;
	}
	
	
	
	/**
	 * 				float delay = 6;
				float cut = .15f;
				int coal = 0;
				int iron = 0;
				int diamond = 0;
				final Inventory inv = new Inventory(p);
				if(inv.hasItem(Material.COAL_ORE))
					coal = inv.getSize(Material.COAL_ORE);
				if(inv.hasItem(Material.IRON_ORE))
					iron = inv.getSize(Material.IRON_ORE);
				if(inv.hasItem(Material.DIAMOND_ORE))
					diamond = inv.getSize(Material.DIAMOND_ORE);
				if(p.hasPermission("mine.license")){
					cut = .3f;
					delay = 3;
				}
				if(coal == 0 && iron == 0 && diamond == 0){
					Util.sendMessage(p, "Nothing to refine");
					return true;
				}
				final int tCoal = coal;
				final int tIron = iron;
				final int tDiamond = diamond;
				final int gCoal = (int) ((int) coal - (coal * cut));
				final int cCut = (int) (coal * cut);
				
				final int gIron = (int) ((int) iron - (iron * cut));
				final int iCut = (int) (iron * cut);
				
				final int gDiamond = (int) ((int) diamond - (diamond * cut));
				final int dCut = (int) (diamond * cut);
				
				final Long iDelay = (long) ((delay * iron) + (delay * coal) + (delay * diamond));
				
				final long start = System.currentTimeMillis();
				final long end = (iDelay / 20) * 1000;
				
				final BukkitTask timer = Bukkit.getScheduler().runTaskTimer(plugin, new Runnable(){
					public void run(){
						long delay = System.currentTimeMillis()-start;
						float percent = ((float)delay / end) * 100;
						BarAPI.removeBar(p);
						BarAPI.setMessage(p, "Refining... " + (int)percent + "%");
						BarAPI.setHealth(p, percent);
					}
				}, 0L, 5L);
				
				Bukkit.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new Runnable(){
					public void run(){
						//if in region
						Util.sendMessage(p, "You got " + (gCoal + gIron + gDiamond) + " minerals back, " + (cCut + iCut + dCut) + " were lost");
						inv.giveItem(Material.COAL_ORE, Material.COAL, tCoal, gCoal);
						inv.giveItem(Material.IRON_ORE, Material.IRON_INGOT, tIron, gIron);
						inv.giveItem(Material.DIAMOND_ORE, Material.DIAMOND, tDiamond, gDiamond);
						//Bukkit.getScheduler().cancelTask(tID);
						Bukkit.getScheduler().cancelTask(timer.getTaskId());
						BarAPI.removeBar(p);
						BarAPI.setMessage(p, "Minecraft Life");
						plugin.getConfig().set(p.getName().toLowerCase() + ".refine", false);
						plugin.saveConfig();
					}
				}, iDelay);	
			}
	 */
}

