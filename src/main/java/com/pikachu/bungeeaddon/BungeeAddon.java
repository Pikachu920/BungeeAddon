package com.pikachu.bungeeaddon;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public class BungeeAddon extends JavaPlugin {

	public static final String CHANNEL = "BungeeCord";

	private static SkriptAddon addonInstance;
	private static BungeeAddon instance;

	@Override
	public void onEnable() {
		instance = this;
		try {
			getAddonInstance().loadClasses("com.pikachu.bungeeaddon", "skript");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

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

}
