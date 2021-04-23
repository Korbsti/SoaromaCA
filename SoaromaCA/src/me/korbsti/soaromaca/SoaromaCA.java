package me.korbsti.soaromaca;

import java.io.File;
import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import me.korbsti.soaromaca.commands.Commands;
import me.korbsti.soaromaca.configmanager.ConfigManagerr;
import me.korbsti.soaromaca.util.RandomLine;
import me.korbsti.soaromaca.util.Hex;

public class SoaromaCA extends JavaPlugin {
	static final String mainVariable = "Korbsti";
	public File configFile;
	public YamlConfiguration yamlConfig;
	public ConfigManagerr config = new ConfigManagerr(this);
	public RandomLine randomLine = new RandomLine(this);
	public ArrayList<String> command = new ArrayList<>();
	public Hex hexColour = new Hex();
		@Override
	public void onEnable() {
		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(new Commands(this), this);
		config.configCreator("config.yml");
		getCommand("calist").setExecutor(new Commands(this));
		getCommand("ca").setExecutor(new Commands(this));
		getCommand("careload").setExecutor(new Commands(this));
		getCommand("cahelp").setExecutor(new Commands(this));

		for (Object obj : getConfig().getKeys(true)) {
			String str = String.valueOf(obj);
			int num = 0;
			for (String character : str.split("")) {
				if (".".equals(character)) {
					num++;
				}
			}
			if (str.startsWith("commands.") && num == 1) {
				command.add(str.substring(9, str.length()));
			}
		}
	}

	@Override
	public void onDisable() {
	}
}
