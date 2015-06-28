package me.mcf5.feat;

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

public class Buisness implements Listener, CommandExecutor{

	MCF5 plugin;
	public Buisness(MCF5 plugin){
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String arg2, String[] args) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("cm")){
			if(args.length == 0){
				p.sendMessage("/cm ?");
			}else if(args.length == 1){
				if(args[0].equalsIgnoreCase("?")){
					p.sendMessage("/cm ? - For help");
					p.sendMessage("/cm stats - For Stats on your Company");
					p.sendMessage("/cm create <name> - To create a Company");
					p.sendMessage("/cm disband - Deletes the company");
					p.sendMessage("/cm list - Lists all");
					p.sendMessage("/cm deposit <amount> - Adds to company's networth");
					p.sendMessage("/cm withdraw <amount> - Takes company's networth away");
					p.sendMessage("/cm pay <company> <amount> - Pays a company");
					p.sendMessage("/cm buy <company> <amount> - Purchases a amount of stock from a company");
				}else if(args[0].equalsIgnoreCase("stats")){
					if(Company.isOwner(p)){
						p.sendMessage(ChatColor.BLUE + "You own " + ChatColor.GOLD + Company.get(p).name.toUpperCase() + ChatColor.BLUE +", NetWorth of: " + ChatColor.GOLD + Company.get(p).balance() + "$");
						p.sendMessage(ChatColor.BLUE + "You have " + ChatColor.GOLD + Company.get(p).members.toArray().length + ChatColor.BLUE + " employees!");
					}else{
						p.sendMessage("You don't own a Company, create one with /cm create <name>");
					}
				}else if(args[0].equalsIgnoreCase("disband")){
					if(Company.isOwner(p)){
						Company cmp = Company.get(p);
						p.sendMessage(ChatColor.BLUE + "You left " + ChatColor.GOLD + cmp.name.toUpperCase() + ChatColor.BLUE + " it is now " + ChatColor.GOLD + "DELETED" + ChatColor.BLUE + " you lost " + ChatColor.GOLD + cmp.balance + "$");
						cmp.delete(p);
					}else{
						p.sendMessage("You do not own a company!");
					}
				}else if(args[0].equalsIgnoreCase("create")){
					p.sendMessage("Usage /cm create <name>");
				}else if(args[0].equalsIgnoreCase("list")){
					List<String> companys = Company.listCompanys();
					if(companys != null && companys.toArray().length != 0){
						p.sendMessage(ChatColor.BLUE + "~~~~" + ChatColor.GOLD + " Company List Based on Ranking" + ChatColor.BLUE + " ~~~~");
						int i = 1;
						for(String name : companys){
							if(i < 15){
								p.sendMessage(ChatColor.BLUE + "" + i + ".) " + ChatColor.GOLD + name.toUpperCase() + ChatColor.BLUE + " owned by " + ChatColor.GOLD + Company.get(name).owner.toUpperCase());
								i++;
							}else{
								return true;
							}
						}
					}else{
						p.sendMessage("No company has been created yet!");
					}				
				}
			}else if(args.length == 2){
				if(args[0].equalsIgnoreCase("create")){
					if(Company.exists(args[1])){
						p.sendMessage("Company already exists!");
					}else{
						if(!Company.isOwner(p)){
							Company cmp = Company.create(args[1], p);
							p.sendMessage(ChatColor.BLUE + "You created " + ChatColor.GOLD + Company.get(p).name.toUpperCase() + ChatColor.BLUE +", NetWorth of: " + ChatColor.GOLD + Company.get(p).balance + "$");
						}else{
							p.sendMessage("You already have a company, leave the current one to create a new one!");
						}
					}
				}else if(args[0].equalsIgnoreCase("deposit")){
					if(tryParseDouble(args[1])){
						if(Company.isOwner(p)){
							double amount = Double.parseDouble(args[1]);
							if(plugin.econ.getBalance(p) >= amount){
								plugin.econ.withdrawPlayer(p, amount);
								Company cmp = Company.get(p);
								double oldBalance = cmp.balance();
								cmp.deposit(amount);
								p.sendMessage(ChatColor.BLUE + "Deposited " + ChatColor.GOLD + amount + "$" + ChatColor.BLUE + ", " +ChatColor.BLUE + "Balance of " + ChatColor.GOLD + oldBalance + "$ " + ChatColor.BLUE + "changed to " + ChatColor.GOLD + cmp.balance + "$" + ChatColor.BLUE + " in " + ChatColor.GOLD + cmp.name.toUpperCase());
								
							}else{
								p.sendMessage("Not enough founds in your balance!");
							}
						}else{
							p.sendMessage("You do not own a comapny!");
						}
					}else{
						p.sendMessage("Invalid Amount!");
					}
				}else if(args[0].equalsIgnoreCase("withdraw")){
					if(tryParseDouble(args[1])){
						if(Company.isOwner(p)){
							double amount = Double.parseDouble(args[1]);
							Company cmp = Company.get(p);
							if(cmp.balance() - amount >= 0){
								plugin.econ.depositPlayer(p, amount);
								double oldBalance = cmp.balance();
								cmp.withdraw(amount);
								p.sendMessage(ChatColor.BLUE + "Withdrawed " + ChatColor.GOLD + amount + "$" + ChatColor.BLUE + ", " +ChatColor.BLUE + "Balance of " + ChatColor.GOLD + oldBalance + "$ " + ChatColor.BLUE + "changed to " + ChatColor.GOLD + cmp.balance + "$" + ChatColor.BLUE + " in " + ChatColor.GOLD + cmp.name.toUpperCase());
								
							}else{
								p.sendMessage("Invalid Transaction!");
							}
						}else{
							p.sendMessage("You do not own a comapny!");
						}
					}else{
						p.sendMessage("Invalid Amount!");
					}
				}else if(args[0].equalsIgnoreCase("select")){
					
				}
			}else if(args.length == 3){
				if(args[0].equalsIgnoreCase("pay")){
					if(Company.get(args[1]) != null){
						if(tryParseDouble(args[2])){
							if(Company.isOwner(p)){
								Company cmp = Company.get(p);
								Company target = Company.get(args[1]);
								double send = Double.parseDouble(args[2]);
								if(Bukkit.getOfflinePlayer(target.owner) != null){
									if(cmp.balance - send >= 0){
										cmp.withdraw(send);
										target.deposit(send);
										Bukkit.getOfflinePlayer(target.owner).getPlayer().sendMessage(ChatColor.BLUE + "Company " + ChatColor.GOLD + cmp.name.toUpperCase() + ChatColor.BLUE + " sent " + ChatColor.GOLD + target.name.toUpperCase() + ChatColor.BLUE + " amount of " + ChatColor.GOLD + send + "$");
									}else{
										p.sendMessage("Not enough money in your company!");
									}
								}else{
									p.sendMessage("That company's owner is offline!");
								}
							}else{
								p.sendMessage("You do not own a company!");
							}
						}else{
							p.sendMessage("Invalid Amount!");
						}
					}else{
						p.sendMessage("Company does not exist!");
					}
				}
			}else if(args[0].equalsIgnoreCase("buy")){
				//cm buy <company> <price>
				if(Company.exists(args[1])){
					if(tryParseInteger(args[2])){
						Company cmp = Company.get(args[1]);
						int amount = Integer.parseInt(args[2]);
						if(plugin.econ.getBalance(p) >= cmp.stockPricePerShare() * amount){
							plugin.econ.withdrawPlayer(p, cmp.stockPricePerShare() * amount);
							addShares(cmp, p, amount);
							p.sendMessage(ChatColor.BLUE + "Bought " + ChatColor.GOLD + amount + ChatColor.BLUE + " shares of " + ChatColor.GOLD + cmp.name.toUpperCase() + ChatColor.BLUE + " is worth " + ChatColor.GOLD + cmp.stockPricePerShare() * amount + "$");
						}else{
							p.sendMessage(ChatColor.BLUE + "Not enough founds, " + ChatColor.GOLD + amount + ChatColor.BLUE + " shares of " + ChatColor.GOLD + cmp.name.toUpperCase() + ChatColor.BLUE + " is worth " + ChatColor.GOLD + cmp.stockPricePerShare() * amount + "$");
						}
					}else{
						p.sendMessage("Invalid Amount!");
					}
				}else{
					p.sendMessage("Company does not exist!");
				}
			}
		}
		return false;
	}
	
	private boolean tryParseDouble(String value){
		try{
			Double.parseDouble(value);
			return true;
		}catch(NumberFormatException nfe){
			return false;
		}
	}
	
	private boolean tryParseInteger(String value){
		try{
			Integer.parseInt(value);
			return true;
		}catch(NumberFormatException nfe){
			return false;
		}
	}
	
	private void addShares(Company cmp, Player p, int amount){
		
	}
	
	
	
}
