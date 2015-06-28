package me.mcf5.logic;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ArrayI {
	
	
	public ItemStack[] spread(ItemStack[] s){
		s = compress(s);
		ArrayList<ItemStack> list = new ArrayList<>();
		for(ItemStack i : s){
			int byAmount = getAmount(i.getType(), s);
			list.add(new ItemStack(i.getType(), split(i, byAmount)[0].getAmount()));
			//getAmount(i.getType(), s);
			//ArrayI.split
		}
		return toArray(list);
	}
	
	
	public static boolean hasEnough(ItemStack[] s){
		boolean preview = false;
		for(ItemStack i : s){
			if(i.getAmount() > 1)
				preview = true;
			else
				preview = false;
		}
		return preview;
	}
	
	public static ItemStack[] removeEnough(ItemStack[] s){
		ArrayList<ItemStack> list = new ArrayList<>();
		for(ItemStack i : s){
			list.add(new ItemStack(i.getType(), i.getAmount() - 1));
		}
		return toArray(list);
	}
	
	public static ItemStack[] combineEven(ItemStack[] one, ItemStack[] two){
		ArrayList<ItemStack> list = new ArrayList<>();
		list = toArrayList(two);
		
		one = compress(one);
		
		
		for(ItemStack s : one){
			if(contains(s.getType(), two)){ //If the item is in two
				ItemStack[] splited = split(s, getAmount(s.getType(), two));
				//log(toArray(apply(splited, toArray(list))));
				list = apply(splited, toArray(list));
			}
		}
		return toArray(list);
	}
	
	public static ArrayList<ItemStack> apply(ItemStack[] split, ItemStack[] two){
		ItemStack[] list = new ItemStack[two.length];
		list = two.clone();
		int i = 0;
		int l = 0;
		if(split == null || split.length == 0)
			return null;
		for(ItemStack s : two){
			if(s.getType().equals(split[0].getType())){
				if(list[i] != null){
					if(split.length > l){
						list[i] = new ItemStack(list[i].getType(), list[i].getAmount() + split[l].getAmount());
						l++;
					}
				}
			}
			i++;
		}
		return toArrayList(list);
	}
	
	
	public static int getAmount(Material mat, ItemStack[] list){
		int i = 0;
		for(ItemStack s : list)
			if(s.getType().equals(mat))
				i++;
		return i;
	}
	
	public static boolean isInt(float value){
		return value % 1 == 0;
	}
	
	
	public static ItemStack[] split(ItemStack item, int byAmount){
		//Split
		//(STONE, 7)
		//by
		//2
		
		ArrayList<ItemStack> list = new ArrayList<>();
		if(item.getAmount() < byAmount){ //Can we move
			return new ItemStack[]{item};
		}else{
			float f = (float)((float)item.getAmount() / (float)byAmount);
			if(isInt(f)){
				//Evenly Divided
				int i = 0;
				while(i < byAmount){
					list.add(new ItemStack(item.getType(), (item.getAmount() / byAmount)));
					i++;
				}
			}else{
				//Not evenly divided
				int i = 0;
				int collect = 0;
				while(i < byAmount){
					list.add(new ItemStack(item.getType(), Math.round(item.getAmount() / byAmount)));
					collect += Math.round(item.getAmount() / byAmount);
					i++;
				}
				int s = 0;
				while((list.get(s).getAmount() + collect) < 0)
					s++;
				collect = item.getAmount() - collect;
				list.set(s, new ItemStack(list.get(s).getType(), list.get(s).getAmount() + collect));
			}
		}
		return toArray(list);
	}
	
	
	public static ArrayList<ItemStack> toArrayList(ItemStack[] list){
		ArrayList<ItemStack> l = new ArrayList<>();
		for(ItemStack s : list)
			l.add(s);
		return l;
	}
	
	
	public static boolean contains(Material mat, ItemStack[] check){
		for(ItemStack s : check)
			if(s.getType().equals(mat))
				return true;
		return false;
	}
	
	
	public static ItemStack[] compress(ItemStack[] array){
		
		HashMap<Material, Integer> list = new HashMap<>();
		
		for(ItemStack s : array){
			if(list.get(s.getType()) == null)
				list.put(s.getType(), s.getAmount());
			else
				list.put(s.getType(), list.get(s.getType()) + s.getAmount());
		}
		return toArray(list);
	}
	
	
	public static ItemStack[] toArray(HashMap<Material, Integer> list){
		
		ArrayList<ItemStack> l = new ArrayList<>();
		
		Set<Material> keys = list.keySet();
		
		for(Material m : keys)
			l.add(new ItemStack(m, list.get(m)));
		
		return toArray(l);
	}
	
	public static ItemStack[] toArray(ArrayList<ItemStack> list){
		if(list == null || list.size() == 0)
			return null;
		return list.toArray(new ItemStack[list.size()]);
	}
	
	
	public static void log(ItemStack[] list){
		if(list == null || list.length == 0)
			return;
		for(ItemStack s : list){
			System.out.println("(" + s.getType().toString() + ", " + s.getAmount() + ")");
		}
	}
	
	public static void logEvenly(ItemStack[] contents){
		System.out.println(toString(contents[0]) + toString(contents[1]) + toString(contents[2]));
		System.out.println(toString(contents[3]) + toString(contents[4]) + toString(contents[5]));
		System.out.println(toString(contents[6]) + toString(contents[7]) + toString(contents[8]));
	}
	
	public static String toString(ItemStack s){
		return "(" + s.getType() + ", " + s.getAmount() + ")";
	}
	
}
