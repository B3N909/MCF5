package me.mcf5.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

public class PlayerJoinBoatEventListener implements Listener{
	@EventHandler
	public void Click(PlayerInteractEntityEvent e){
		final Player p = e.getPlayer();
		final Entity entity = e.getRightClicked();
		if(entity instanceof Boat){
				Bukkit.getServer().getPluginManager().callEvent(new PlayerJoinBoatEvent(p, (Boat) entity));
		}
	}
	
}
