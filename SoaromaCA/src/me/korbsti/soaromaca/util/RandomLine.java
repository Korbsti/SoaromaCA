package me.korbsti.soaromaca.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;

import me.korbsti.soaromaca.SoaromaCA;

public class RandomLine {
	SoaromaCA plugin;
	
	public RandomLine(SoaromaCA instance) {
		this.plugin = instance;
	}
	
	public String chooseRandom(List<String> list) {
		Random random = new Random();   
		
		return list.get(random.nextInt(list.size()));
	}
}
