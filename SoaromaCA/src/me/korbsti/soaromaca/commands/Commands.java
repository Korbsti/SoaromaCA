package me.korbsti.soaromaca.commands;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import me.korbsti.soaromaca.SoaromaCA;

public class Commands implements CommandExecutor, Listener {
	SoaromaCA plugin;
	
	public Commands(SoaromaCA instance) {
		this.plugin = instance;
	}
	
	@SuppressWarnings("unchecked")
	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
	public void preCommand(PlayerCommandPreprocessEvent e) {
		for (String str : plugin.command) {
			if (e.getMessage().startsWith("/" + str)) {
				Player p;
				p = e.getPlayer();
				String[] msg;
				String s1;
				s1 = e.getMessage();
				msg = s1.split(" ");
				ArrayList<String> arr;
				arr = new ArrayList<>(Arrays.asList(msg));
				if (!plugin.yamlConfig.getBoolean("commands." + str + ".enablePreCommandProcess")) {
					return;
				}
				if (arr.size() == 1) {
					p.sendMessage(plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("invalidArgs")));
					e.setCancelled(true);
					return;
				}
				if (p.hasPermission(plugin.yamlConfig.getString("commands." + str + ".permission"))) {
					Player targetPlayer = Bukkit.getPlayer(arr.get(1));
					if (targetPlayer != null) {
						if (targetPlayer.getName() == p.getName()) {
							String string = plugin.randomLine.chooseRandom((List<String>) plugin.yamlConfig.getList(
							        "commands." + str + ".ifselfTo"));
							p.sendMessage(
							        plugin.hexColour.hexTranslate("#", "/", string.substring(0, string.length())));
							e.setCancelled(true);
							if (plugin.yamlConfig.getBoolean("commands." + str + ".enableBroadcast")) {
								Bukkit.broadcastMessage(plugin.hexColour.hexTranslate("#", "/",
								        plugin.randomLine
								                .chooseRandom((List<String>) plugin.yamlConfig
								                        .getList("commands." + str + ".broadcastSelf"))
								                .replace("{player}", p.getName())));
							}
							return;
						}
						String string = plugin.randomLine
						        .chooseRandom(
						                (List<String>) plugin.yamlConfig.getList("commands." + str + ".sendMessageTo"))
						        .replace("{player}", p.getName());
						targetPlayer.sendMessage(
						        plugin.hexColour.hexTranslate("#", "/", string.substring(0, string.length())));
						String secondString = plugin.randomLine
						        .chooseRandom(
						                (List<String>) plugin.yamlConfig.getList("commands." + str + ".recieveMessage"))
						        .replace("{player}", targetPlayer.getName());
						p.sendMessage(secondString.substring(0, secondString.length()));
						if (plugin.yamlConfig.getBoolean("commands." + str + ".enableBroadcast")) {
							Bukkit.broadcastMessage(plugin.hexColour.hexTranslate("#", "/",
							        plugin.randomLine
							                .chooseRandom((List<String>) plugin.yamlConfig
							                        .getList("commands." + str + ".broadcastMessage"))
							                .replace("{player}", p.getName())
							                .replace("{secondPlayer}", targetPlayer.getName())));
						}
					} else {
						p.sendMessage(
						        plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("notOnline")));
					}
				} else {
					p.sendMessage(plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("noPerm")));
				}
				e.setCancelled(true);
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean onCommand(CommandSender s, Command cmd, String label, String[] args) {
		if ("calist".equalsIgnoreCase(label)) {
			if (s.hasPermission("ca.list")) {
				String s1;
				s1 = plugin.hexColour.hexTranslate("#", "/",
				        plugin.yamlConfig.getString("calist").replace("{actions}", plugin.command.toString()));
				s.sendMessage(s1);
			} else {
				s.sendMessage(plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("noPerm")));
			}
		}
		if ("cahelp".equalsIgnoreCase(label)) {
			if (s.hasPermission("ca.help")) {
				for (Object obj : plugin.yamlConfig.getList("help-message")) {
					s.sendMessage(plugin.hexColour.hexTranslate("#", "/", String.valueOf(obj)));
				}
			} else {
				s.sendMessage(plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("noPerm")));
			}
		}
		
		if ("careload".equalsIgnoreCase(label)) {
			if (s.hasPermission("ca.reload")) {
				plugin.reloadConfig();
				plugin.saveConfig();
				for (Object obj : plugin.getConfig().getKeys(true)) {
					String str = String.valueOf(obj);
					int num = 0;
					for (String character : str.split("")) {
						if (".".equals(character)) {
							num++;
						}
					}
					if (str.startsWith("commands.") && num == 1) {
						plugin.command.add(str.substring(9, str.length()));
					}
				}
				s.sendMessage(plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("reloaded")));
			} else {
				s.sendMessage(plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("noPerm")));
			}
		}
		if ("ca".equalsIgnoreCase(label)) {
			for (String str : plugin.command) {
				if (args[0].equalsIgnoreCase(str)) {
					Player p = (Player) s;
					if (args.length == 1) {
						p.sendMessage(
						        plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("invalidArgs")));
						return true;
					}
					if (!p.hasPermission(plugin.yamlConfig.getString("commands." + str + ".permission"))) {
						p.sendMessage(plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString("noPerm")));
					} else {
						Player targetPlayer = Bukkit.getPlayer(args[1]);
						if (targetPlayer == null) {
							p.sendMessage(plugin.hexColour.hexTranslate("#", "/", plugin.yamlConfig.getString(
							        "notOnline")));
						} else {
							if (targetPlayer.getName() == p.getName()) {
								String string = plugin.randomLine.chooseRandom((List<String>) plugin.yamlConfig.getList(
								        "commands." + str + ".ifselfTo"));
								p.sendMessage(plugin.hexColour.hexTranslate("#", "/", string.substring(0, string
								        .length())));
								if (!plugin.yamlConfig.getBoolean("commands." + str + ".enableBroadcast")) {
									return true;
								}
								String anotherString = plugin.randomLine.chooseRandom((List<String>) plugin.yamlConfig
								        .getList("commands." + str + ".broadcastSelf")).replace("{player}", p
								                .getName());
								Bukkit.broadcastMessage(plugin.hexColour.hexTranslate("#", "/", anotherString.substring(
								        0, anotherString.length())));
								return true;
							}
							String string = plugin.randomLine.chooseRandom((List<String>) plugin.yamlConfig.getList(
							        "commands." + str + ".sendMessageTo")).replace("{player}", p.getName());
							targetPlayer.sendMessage(plugin.hexColour.hexTranslate("#", "/", string.substring(1, string
							        .length() - 1)));
							String secondString = plugin.randomLine.chooseRandom((List<String>) plugin.yamlConfig
							        .getList("commands." + str + ".recieveMessage")).replace("{player}", targetPlayer
							                .getName());
							p.sendMessage(secondString.substring(0, secondString.length()));
							if (plugin.yamlConfig.getBoolean("commands." + str + ".enableBroadcast")) {
								String string2 = plugin.randomLine.chooseRandom((List<String>) plugin.yamlConfig
								        .getList("commands." + str + ".broadcastMessage")).replace("{player}", p
								                .getName()).replace("{secondPlayer}", targetPlayer.getName());
								Bukkit.broadcastMessage(plugin.hexColour.hexTranslate("#", "/", string2.substring(0,
								        string2.length())));
							}
						}
					}
				}
			}
		}
		return false;
	}
}