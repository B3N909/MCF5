package me.mcf5.main;

import me.mcf5.logic.ArrayI;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Debug {
	public static void main(String[] args){
		System.out.println("Startup...");
		
		
		
		
		ItemStack[] one = new ItemStack[]{item(Material.LOG, 22), item(Material.WOOD_STEP, 5), item(Material.WOOD, 1)};
		ItemStack[] two	= new ItemStack[]{item(Material.WOOD_STEP, 1), item(Material.WOOD_STEP, 1), item(Material.WOOD_STEP, 1), item(Material.LOG, 1), item(Material.WOOD, 1), item(Material.LOG, 1), item(Material.LOG, 1), item(Material.WOOD, 1), item(Material.LOG, 1)};
		
		
		System.out.println("~Original~");
		ArrayI.logEvenly(two);
		
		System.out.println("~Combined~");
		ArrayI.logEvenly(ArrayI.combineEven(one, two));
	}
	
	
	
	
	
	
	
	public static ItemStack item(Material mat, int amount){
		return new ItemStack(mat, amount);
	}
}
