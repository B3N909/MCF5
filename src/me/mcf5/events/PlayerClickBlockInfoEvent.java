package me.mcf5.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerClickBlockInfoEvent extends Event{
	
	Player p;
	Block b;
	String i;
	
	public PlayerClickBlockInfoEvent(Player p, Block b, String i){
		this.p = p;
		this.b = b;
		this.i = i;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	public Block getBlock(){
		return b;
	}
	
	public String getInformation(){
		return i;
	}
	
	
	private static final HandlerList handlers = new HandlerList();
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
