package me.mcf5.main;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Inventory {
	
	Player p;
	
	public Inventory(Player p){
		this.p = p;
	}
	
	public Player getP() {
		return p;
	}

	public void setP(Player p) {
		this.p = p;
	}

	public void giveItem(Material mat_took, Material mat_give, int took, int give){
		if(give == 0){
			return;
		}
		this.p.getInventory().removeItem(new ItemStack(mat_took, took));
		this.p.getInventory().addItem(new ItemStack(mat_give, give));
	}
	
	public void removeItem(Material i, int take){
		this.p.getInventory().removeItem(new ItemStack(i, take));
	}
	
	public void removeItem(ItemStack i){
		this.p.getInventory().removeItem(i);
	}
	
	public boolean hasItem(Material i){
		ItemStack[] inv = this.p.getInventory().getContents();
		for(ItemStack item : inv)
			if(item != null)
				if(item.getType().equals(i))
					return true;
		return false;
	}
	
	public int getSize(Material i){
		ItemStack[] inv = this.p.getInventory().getContents();
		int s = 0;
		for(ItemStack item : inv)
			if(item != null)
				if(item.getType().equals(i))
					s = s + item.getAmount();
		return s;
	}
	
	public static ItemStack setName(ItemStack item, String name){
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(name);
		item.setItemMeta(meta);
		return item;
	}
	
	public static ItemStack setLore(ItemStack item, List<String> lore){
		ItemMeta meta = item.getItemMeta();
		meta.setLore(lore);
		item.setItemMeta(meta);
		return item;
	}
}
