package me.mcf5.feat;

import java.awt.List;
import java.util.ArrayList;
import java.util.UUID;

import me.mcf5.main.Util;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.command.CommandExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.material.Openable;
import org.bukkit.plugin.PluginManager;

public class DoorListener implements Listener, CommandExecutor{
	
	public DoorListener(PluginManager pm) {
		// TODO Auto-generated constructor stub
	}
	@EventHandler
	public void Click(PlayerInteractEvent e){
		Player p = e.getPlayer();
		if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
			if(e.getClickedBlock().getType() == Material.IRON_DOOR_BLOCK || e.getClickedBlock().getType() == Material.IRON_DOOR){
				
				Location cb = e.getClickedBlock().getLocation();
				Location Clicked;
				Clicked = new Location(p.getWorld(), cb.getX(), cb.getY() - 1, cb.getZ());
				if(Clicked.getBlock().getType() == Material.IRON_DOOR_BLOCK){
					//Top Half
				}else{
					//Bottom Half
					Clicked = cb;
				}
				
				
				if(p.getItemInHand() != null){
					if(p.getItemInHand().getType() == Material.FLINT){
						if(p.getItemInHand().hasItemMeta()){
							if(p.getItemInHand().getItemMeta().hasLore()){
								for(String lore : p.getItemInHand().getItemMeta().getLore()){
									if(Door.getID(Clicked).contains(lore)){
										Block door = Clicked.getBlock();
										BlockState state = door.getState();
										MaterialData data = state.getData();
										Openable opn = (Openable) data;
										if(opn.isOpen()){
											opn.setOpen(false);
											state.setData((MaterialData) opn);
											state.update();
										}else{
											opn.setOpen(true);
											state.setData((MaterialData) opn);
											state.update();
										}
										return;
									}
									Util.sendMessage(p, "Incorrect Key.");
									return;
								}
							}
						}
					}
				}
				//Else check if public
				Door door = new Door();
				Door.loadID(Door.getRawID(Clicked));
				if(door.mode == true){
					Block door1 = Clicked.getBlock();
					BlockState state = door1.getState();
					MaterialData data = state.getData();
					Openable opn = (Openable) data;
					if(opn.isOpen()){
						opn.setOpen(false);
						state.setData((MaterialData) opn);
						state.update();
						p.playSound(p.getLocation(), Sound.DOOR_CLOSE, 10, 1);
						
					}else{
						opn.setOpen(true);
						state.setData((MaterialData) opn);
						state.update();
						p.playSound(p.getLocation(), Sound.DOOR_OPEN, 10, 1);
					}
				}else{
					//Util.sendMessage(p, "You do not own this door.");
					p.playSound(p.getLocation(), Sound.CLICK, 10, 1);
				}
				
				
			}
		}
		
	}
	@EventHandler
	public void Break(BlockBreakEvent e){
		Player p = e.getPlayer();
		if(e.getBlock().getType() == Material.IRON_DOOR_BLOCK){
			//top?
			if(p.isSneaking()){
				Util.sendMessage(p, "Break.");
			}else{
				Door door = new Door(e.getBlock().getLocation(), p);
				door.owner = p.getName().toLowerCase().toString();
				Util.sendMessage(p, "Door Stolen, ID " + door.ID);
				e.setCancelled(true);
			}
		}
	}
	@EventHandler
	public void Place(BlockPlaceEvent e){
		Player p = e.getPlayer();
		if(e.getBlock().getType() == Material.IRON_DOOR_BLOCK){
			Door door = new Door(e.getBlock().getLocation(), p);
			Util.sendMessage(p, "You placed Door: " + door.ID);
		}
	}

	public boolean onCommand(org.bukkit.command.CommandSender sender, org.bukkit.command.Command cmd, String arg2, String[] arg3) {
		Player p = (Player) sender;
		if(cmd.getName().equalsIgnoreCase("delete") && p.hasPermission("mcf5.delete")){
			p.getTargetBlock(null, 50).setType(Material.AIR);
			Util.sendMessage(p, "Block Deleted.");
		}else if(cmd.getName().equalsIgnoreCase("key")){
			if(arg3.length == 0){
				Util.sendMessage(p, "'/key ID' generates a physicall key to open doors.");
				return false;
			}
			Door door = new Door();
			Door.loadID(Integer.parseInt(arg3[0]));
			if(p.getUniqueId().toString().equalsIgnoreCase(door.owner)){
				Util.sendMessage(p, "Generated Key!");
				ItemStack key = new ItemStack(Material.FLINT);
				ItemMeta keyIM = key.getItemMeta();
				keyIM.setDisplayName(ChatColor.GOLD + "ID?=" + arg3[0]);
				ArrayList<String> lore = new ArrayList<String>();
				lore.add(arg3[0]);
				keyIM.setLore(lore);
				key.setItemMeta(keyIM);
				p.getInventory().addItem(key);	
			}else{
				Util.sendMessage(p, "You can only generate keys for doors you own.");
				System.out.println("[MCF5] Failed UUID: " + door.owner);
			}
		}else if(cmd.getName().equalsIgnoreCase("public")){
			if(arg3.length == 0){
				Util.sendMessage(p, "'/public ID true' sets a door to be openable by anyone");
				return false;
			}
			Door door = new Door();
			Door.loadID(Integer.parseInt(arg3[0]));
			door.setMode(Boolean.parseBoolean(arg3[1]));
			Util.sendMessage(p, "Door " + arg3[0] + " has been set to " + arg3[1]);
		}
		return false;
	}
}