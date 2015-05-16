package me.mcf5.main;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Logger {
	
	public static Config config;
	
	public static String getDate(){
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		return "[" + format.format(now) + "]";
	}
	
	public static String getDay(){
		Date now = new Date();
		SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
		return "[" + format.format(now) + "]";
	}
	
	public static void log(String info){
		config.getConfig().set(getDate(), String.valueOf(info));
		config.Save();
	}
	
	public static void Initialize(){
		config = new Config(getDay());
		config.Save();
	}
	
	
}
