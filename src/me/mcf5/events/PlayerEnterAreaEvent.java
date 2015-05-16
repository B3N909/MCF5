package me.mcf5.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerEnterAreaEvent extends Event{
	Player p;
	String n;
	
	public PlayerEnterAreaEvent(Player p, String n){
		this.p = p;
		this.n = n;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	public String getName(){
		return n;
	}
	
	
	private static final HandlerList handlers = new HandlerList();
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
}
