package me.korbsti.soaromaca.configmanager;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;

import me.korbsti.soaromaca.SoaromaCA;

public class ConfigManagerr {
	static final String mainVariable = "Korbsti";
	SoaromaCA plugin;
	String dir = System.getProperty("user.dir");
	String directoryPathFile = dir + File.separator + "plugins" + File.separator + "SoaromaCA";
	public ConfigManagerr(SoaromaCA instance) {
		this.plugin = instance;
	}
	public void configCreator(String fileName) {
		if (new File(directoryPathFile).mkdirs()) {
			Bukkit.getLogger().info("Generated SoaromaCA configuration folder...");
		}
		plugin.configFile = new File(plugin.getDataFolder(), fileName);
		if (!plugin.configFile.exists()) {
			saveDefaultConfiguration(plugin.configFile);
		}
		plugin.yamlConfig = YamlConfiguration.loadConfiguration(plugin.configFile);
	}
		public void saveDefaultConfiguration(File f) {
		try {
			java.nio.file.Files.copy(plugin.getResource(f.getName()), f.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}