package me.mcf5.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerJoinBoatEvent extends Event implements Listener{
	
	Player p;
	Boat b;
	
	public PlayerJoinBoatEvent(Player p, Boat b){
		this.p = p;
		this.b = b;
	}
	
	public Player getPlayer(){
		return p;
	}
	
	public Boat getBoat(){
		return b;
	}
	
	
	private static final HandlerList handlers = new HandlerList();
	
	public HandlerList getHandlers(){
		return handlers;
	}
	
	public static HandlerList getHandlerList(){
		return handlers;
	}
	
}
