package me.mcf5.feat;

import java.util.ArrayList;
import java.util.List;

import me.mcf5.main.MCF5;
import net.milkbowl.vault.economy.EconomyResponse;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class ATM implements Listener{
	
	MCF5 plugin;
	public ATM(MCF5 plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onSign(SignChangeEvent  e){
		if(e.getLine(0).equalsIgnoreCase("[atm]")){
			e.setLine(0, ChatColor.WHITE + "== ATM ==");
			e.setLine(1, ChatColor.GREEN + "> " + ChatColor.WHITE + "Withdraw");
			e.setLine(2, ChatColor.BLACK + "Deposit");
			e.setLine(3, ChatColor.DARK_GRAY + "Click for Balance");
		}
	}
	
	@EventHandler
	public void onClick(PlayerInteractEvent e){
		if(e.getClickedBlock() != null && e.getClickedBlock().getType().equals(Material.WALL_SIGN)){
			Sign s = (Sign) e.getClickedBlock().getState();
			if(e.getAction() == Action.LEFT_CLICK_BLOCK){
				if(s.getLine(1).equals(ChatColor.GREEN + "> " + ChatColor.WHITE + "Withdraw")){
					setUse(s, e.getPlayer());
					s.setLine(1, ChatColor.GREEN + "> " + ChatColor.WHITE + "Deposit");
					s.setLine(2, ChatColor.BLACK + "Withdraw");
					s.update();
				}else if(s.getLine(1).equals(ChatColor.GREEN + "> " + ChatColor.WHITE + "Deposit")){
					setUse(s, e.getPlayer());
					s.setLine(1, ChatColor.GREEN + "> " + ChatColor.WHITE + "Withdraw");
					s.setLine(2, ChatColor.BLACK + "Deposit");
					s.update();
				}
			}else if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
				Player p = e.getPlayer();
				if(s.getLine(1).equals(ChatColor.GREEN + "> " + ChatColor.WHITE + "Withdraw")){
					if(p.isSneaking()){
						withdraw(p, true, s);
					}else{
						withdraw(p, false, s);
					}
				}else if(s.getLine(1).equals(ChatColor.GREEN + "> " + ChatColor.WHITE + "Deposit")){
					if(p.isSneaking()){
						deposit(p, true, s);
					}else{
						deposit(p, false, s);
					}
				}
			}
		}
	}
	
	private void withdraw(Player p, boolean all, Sign s){
		if(plugin.econ.getBalance(p) > 5){
			if(all){
				if(plugin.econ.getBalance(p) > 64 * 5){
					p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 64));
					p.updateInventory();
					plugin.econ.withdrawPlayer(p, 64 * 5);
					setUse(s, p);
				}else{
					p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, (int) plugin.econ.getBalance(p) / 5));
					p.updateInventory();
					plugin.econ.withdrawPlayer(p, ((int) plugin.econ.getBalance(p) / 5) * 5);
					setUse(s, p);
				}
			}else{
				if(plugin.econ.getBalance(p) > 5){
					p.getInventory().addItem(new ItemStack(Material.GOLD_INGOT, 1));
					p.updateInventory();
					plugin.econ.withdrawPlayer(p, 5);
					setUse(s, p);
				}
			}
		}else{
			s.setLine(3, ChatColor.DARK_GRAY + "No Money");
			s.update();
		}
	}
	
	private void deposit(Player p, boolean all, Sign s){
		if(p.getInventory().contains(Material.GOLD_INGOT)){
			if(all){
				int amount = 0;
				for(ItemStack s1 : p.getInventory().getContents()){
					if(s1 != null && s1.getType().equals(Material.GOLD_INGOT))
						amount = s1.getAmount();
				}
				plugin.econ.depositPlayer(p, (double)amount * 5);
				p.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, amount));
				p.updateInventory();
				setUse(s, p);
			}else{
				plugin.econ.depositPlayer(p, (double)5);
				p.getInventory().removeItem(new ItemStack(Material.GOLD_INGOT, 1));
				p.updateInventory();
				setUse(s, p);
			}
		}else{
			//p.sendMessage(ChatColor.RED + "You need to hold " + ChatColor.GOLD + " GOLD" + ChatColor.RED + " in your hand to deposit gold!");
			s.setLine(3, ChatColor.DARK_GRAY + "Deposit Only Gold");
			s.update();
		}
	}
	
	private void setUse(Sign s, Player p){
		s.setLine(3, ChatColor.DARK_GRAY + "$" + plugin.econ.getBalance(p));
		s.update();
	}
}
