package me.mcf5.main;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {
	
	public FileConfiguration getConfig() {
		return fc;
	}

	public void setConfig(FileConfiguration fc) {
		this.fc = fc;
	}

	public String getName() {
		return n;
	}

	public void setName(String n) {
		this.n = n;
	}

	private FileConfiguration fc;
	private String n;
	private File file;
	
	MCF5 plugin;
	public Config(MCF5 plugin){
		this.plugin = plugin;
	}
	
	public Config(String n){
		File f = new File(n + ".yml");
		FileConfiguration fc = YamlConfiguration.loadConfiguration(f);
		this.fc = fc;
		this.n = n;
		this.file = f;
	}
	
	public void Save(){
		try {
			fc.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
