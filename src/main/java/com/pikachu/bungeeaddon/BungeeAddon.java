package com.pikachu.bungeeaddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class BungeeAddon extends JavaPlugin {

	public static final String CHANNEL = "BungeeCord";

	private static SkriptAddon addonInstance;
	private static BungeeAddon instance;

	public static SkriptAddon getAddonInstance() {
		if (addonInstance == null) {
			addonInstance = Skript.registerAddon(getInstance());
		}
		return addonInstance;
	}

	public static BungeeAddon getInstance() {
		if (instance == null) {
			instance = new BungeeAddon();
		}
		return instance;
	}

	@Override
	public void onEnable() {
		instance = this;
		try {
			getAddonInstance().loadClasses("com.pikachu.bungeeaddon", "skript");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
