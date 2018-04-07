package com.pikachu.bungeeaddon.skript;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import org.bukkit.event.Event;

public class ExprServers extends SimpleExpression<String> {

	static {
		Skript.registerExpression(ExprServers.class, String.class, ExpressionType.SIMPLE,
				"(all [grabbed]|[last] grabbed) bungee[ ]cord servers");
	}

	@Override
	public boolean init(Expression<?>[] exprs, int matchedPattern, Kleenean isDelayed, SkriptParser.ParseResult parseResult) {
		return true;
	}

	@Override
	protected String[] get(Event e) {
		return EffGrabServers.servers;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public String toString(Event e, boolean debug) {
		return "last grabbed bungeecord servers";
	}

}
