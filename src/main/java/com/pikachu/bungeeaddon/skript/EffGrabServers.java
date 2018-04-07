package com.pikachu.bungeeaddon.skript;

import ch.njol.skript.ScriptLoader;
import ch.njol.skript.Skript;
import ch.njol.skript.effects.Delay;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.TriggerItem;
import ch.njol.util.Kleenean;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.pikachu.bungeeaddon.BungeeAddon;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.messaging.Messenger;
import org.bukkit.plugin.messaging.PluginMessageListener;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.Set;

public class EffGrabServers extends Effect implements PluginMessageListener {

	private static final Field DELAYED;
	public static String[] servers;

	static {
		Skript.registerEffect(EffGrabServers.class, "grab all [bungee[ ]cord] servers");
	}

	static {
		Field _DELAYED = null;
		try {
			_DELAYED = Delay.class.getDeclaredField("delayed");
			_DELAYED.setAccessible(true);
		} catch (NoSuchFieldException e) {
			Skript.warning("Skript's delayed field couldn't be found. " +
					"You may not get errors related to delays when using this effect.");
		}
		DELAYED = _DELAYED;
	}

	private Event event;

	public EffGrabServers() {
		Messenger messenger = Bukkit.getServer().getMessenger();
		messenger.registerIncomingPluginChannel(BungeeAddon.getInstance(), BungeeAddon.CHANNEL, this);
		messenger.registerOutgoingPluginChannel(BungeeAddon.getInstance(), BungeeAddon.CHANNEL);
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		ScriptLoader.hasDelayBefore = Kleenean.TRUE;
		return true;
	}

	@Override
	protected void execute(Event e) {
		Bukkit.getScheduler().runTaskAsynchronously(BungeeAddon.getInstance(), () -> {
			Optional<? extends Player> player = Bukkit.getOnlinePlayers().stream().findAny();
			if (!player.isPresent()) {
				BungeeAddon.getInstance().getLogger().warning("Tried to grab all bungeecord servers, but no players were online.");
				return;
			}
			event = e;
			ByteArrayDataOutput out = ByteStreams.newDataOutput();
			out.writeUTF("GetServers");
			player.get().sendPluginMessage(BungeeAddon.getInstance(), BungeeAddon.CHANNEL, out.toByteArray());
		});
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "grab all bungeecord servers";
	}

	@Override
	protected TriggerItem walk(Event e) {
		debug(e, true);

		try {
			((Set<Event>) DELAYED.get(null)).add(e);
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		}

		execute(e);
		return null;
	}

	@Override
	public void onPluginMessageReceived(String channel, Player player, byte[] message) {
		ByteArrayDataInput in = ByteStreams.newDataInput(message);
		if ("GetServers".equals(in.readUTF())) {
			servers = in.readUTF().split(", ");
			Bukkit.getScheduler().runTask(BungeeAddon.getInstance(), () -> TriggerItem.walk(getNext(), event));
		}
	}

}
