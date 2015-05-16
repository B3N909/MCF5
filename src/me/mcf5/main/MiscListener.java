package me.mcf5.main;

import java.util.List;
import me.mcf5.events.PlayerJoinBoatEvent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Boat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

public class MiscListener implements Listener {
	
	private static MCF5 plugin;
	public MiscListener(MCF5 plugin){
		this.plugin = plugin;
	}
	
	@EventHandler
	public void Move(PlayerMoveEvent e){
		Player p = e.getPlayer();
		Location t = p.getLocation();
		Location s = new Location(t.getWorld(), t.getX(), t.getY() - 1, t.getZ());
		if(s.getBlock().getType().equals(Material.WATER)){
			Util.sendMessage(p, "Water is currently disabled");
			p.damage(0.05);
		}
	}
	
	

	
	@EventHandler
	public void fallDamage(EntityDamageEvent e){
		if(e.getEntity() instanceof Player){
			Player p = (Player) e.getEntity();
			if(e.getCause() == DamageCause.FALL){
				List<Entity> near = p.getNearbyEntities(2, 2, 2);
				if(near.isEmpty()){
					return;
				}
				Entity last = null;
				for(Entity en : near){
					if(en instanceof LivingEntity){
						last = en;
					}
				}
				if(last != null){
					LivingEntity d = (LivingEntity) last;
					d.damage(p.getFallDistance() * 2);
					e.setCancelled(true);
					p.playSound(p.getLocation(), Sound.FALL_SMALL, 10, 1);
				}
				
			}
		}
	}
	

	
	@EventHandler
	public void BoatJoin(PlayerJoinBoatEvent e){
		final Player p = e.getPlayer();
		final Boat b = e.getBoat();
		Util.sendMessage(p, "Boats require coal to burn");
		final Inventory inv = new Inventory(p);
		new BukkitRunnable() {
			public void run() {
				if(inv.hasItem(Material.COAL)){
					b.setMaxSpeed(999);
					inv.removeItem(Material.COAL, 1);
				}else{
					b.setVelocity(new Vector(0,0,0));
				}
				if(!(isBoat(p))){
					this.cancel();
				}
			}
		}.runTaskTimer(plugin, 0, 2);
	}
	
	public boolean isBoat(Player p){
		if(p.getVehicle() == null){
			return false;
		}else{
			if(p.getVehicle() instanceof Boat){
				return true;
			}
		}
		return false;
	}
}
