package com.pikachu.bungeeaddon.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.pikachu.bungeeaddon.AsyncEffect;
import com.pikachu.bungeeaddon.BungeeAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

public class EffSend extends AsyncEffect {

	static {
		Skript.registerEffect(EffSend.class, "(send|move) %players% to [server] %string%");
	}

	public EffSend() {
		Bukkit.getServer().getMessenger().registerOutgoingPluginChannel(BungeeAddon.getInstance(), BungeeAddon.CHANNEL);
	}

	private Expression<Player> players;
	private Expression<String> server;

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		players = (Expression<Player>) exprs[0];
		server = (Expression<String>) exprs[1];
		return true;
	}

	@Override
	protected void execute(Event e) {
		String server = this.server.getSingle(e);
		if (server != null) {
			for (Player player : players.getArray(e)) {
				ByteArrayDataOutput out = ByteStreams.newDataOutput();
				out.writeUTF("ConnectOther");
				out.writeUTF(player.getName());
				out.writeUTF(server);
				player.sendPluginMessage(BungeeAddon.getInstance(), BungeeAddon.CHANNEL, out.toByteArray());
			}
		}
	}

}
