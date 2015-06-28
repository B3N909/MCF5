package me.mcf5.feat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import me.mcf5.main.Config;
import org.bukkit.entity.Player;

public class Company {
	
	public String name;
	public String owner;
	public List<String> members = new ArrayList<String>();
	public int level;
	public double balance;
	
	public static Company create(String name, Player p){
		return new Company(name, p.getName().toLowerCase());
	}
	
	public Company(String name, String owner){
		this.name = name;
		this.owner = owner;
		this.level = 1;
		this.balance = 0;
		this.save();
	}
	
	public Company(String name, String owner, int level, double balance, List<String> memebers){
		this.name = name;
		this.owner = owner;
		this.members = memebers;
		this.level = level;
		this.balance = balance;
	}
	
	
	public static Company get(String name){
		if(exists(name)){
			Config cfg = new Config("company");
			cfg.Save();
			String owner = cfg.getConfig().getString(name + ".owner");
			int level = cfg.getConfig().getInt(name + ".level");
			double balance = cfg.getConfig().getDouble(name + ".balance");
			return new Company(name, owner, level, balance, getMembers(name));
		}
		return null;
	}
	
	public static List<String> getMembers(String name){
		Config cfg = new Config("company");
		cfg.Save();
		List<String> members = new ArrayList<String>();
		if(cfg.getConfig().getConfigurationSection(name + ".memebers") != null)
			for(String member : cfg.getConfig().getConfigurationSection(name + ".members").getKeys(false)){
				members.add(member);
			}
		return members;
	}
	
	public static Company get(Player p){
		if(isOwner(p)){
			Config cfg = new Config("company");
			cfg.Save();
			for(String s : cfg.getConfig().getConfigurationSection("").getKeys(false))
				if(cfg.getConfig().getString(s + ".owner").equalsIgnoreCase(p.getName().toLowerCase()))
					return get(s);
		}
		return null;
	}
	
	public static boolean exists(String name){
		Config cfg = new Config("company");
		cfg.Save();
		for(String s : cfg.getConfig().getConfigurationSection("").getKeys(false))
			if(s.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public void addMembers(String member){
		this.members.add(member);
		this.save();
	}
	
	public List<String> getMembers(){
		return this.members;
	}
	
	public void save(){
		Config cfg = new Config("company");
		cfg.Save();
		cfg.getConfig().set(this.name + ".level", Integer.valueOf(this.level));
		cfg.getConfig().set(this.name + ".owner", String.valueOf(this.owner));
		for(String member : members){
			cfg.getConfig().set(this.name + ".members." + member, Double.valueOf(cfg.getConfig().getDouble(this.name + ".members." + member)));
		}
		cfg.Save();
	}
	
	public static boolean isOwner(Player p){
		Config cfg = new Config("company");
		cfg.Save();
		for(String s : cfg.getConfig().getConfigurationSection("").getKeys(false))
			if(cfg.getConfig().getString(s + ".owner").equalsIgnoreCase(p.getName().toLowerCase()))
				return true;
		return false;
	}
	
	public boolean isMember(String name){
		Config cfg = new Config("company");
		cfg.Save();
		for(String s : this.members)
			if(s.equalsIgnoreCase(name))
				return true;
		return false;
	}
	
	public void setPayment(String member, double value){
		if(isMember(member)){
			Config cfg = new Config("company");
			cfg.Save();
			cfg.getConfig().set(this.name + ".members." + member, Double.valueOf(value));
		}
		this.save();
	}
	
	public void setOwner(String name){
		Config cfg = new Config("company");
		cfg.Save();
		cfg.getConfig().set(this.name + ".owner", String.valueOf(name));
		this.owner = name;
		cfg.Save();
	}
	
	public static List<String> listCompanys(){
		final List<String> list = new ArrayList<String>();
		List<Double> money = new ArrayList<Double>();
		Config cfg = new Config("company");
		cfg.Save();
		for(String company : cfg.getConfig().getConfigurationSection("").getKeys(false)){
			list.add(company);
			money.add(cfg.getConfig().getDouble(company + ".balance"));
		}
		Collections.sort(money, new Comparator<Double>(){
			@Override
			public int compare(Double left, Double right) {
				return Double.compare(list.indexOf(left), list.indexOf(right));
			}
			
		});
		return list;
	}
	
	public void deposit(double amount){
		this.balance += amount;
		Config cfg = new Config("company");
		cfg.getConfig().set(this.name + ".balance", this.balance);
		cfg.Save();
	}
	
	public void withdraw(double amount){
		this.balance -= amount;
		Config cfg = new Config("company");
		cfg.getConfig().set(this.name + ".balance", this.balance);
		cfg.Save();
	}
	
	public double balance(){
		return this.balance;
	}
	
	public void delete(Player p){
		Config cfg = new Config("company");
		cfg.getConfig().set(this.name, null);
		cfg.Save();
	}
	
	public static double stockPricePerShare(Company cmp){
		System.out.println(cmp.balance() + " / 1000 = " + cmp.balance/1000);
		return cmp.balance() / 500000;
	}
	
}
