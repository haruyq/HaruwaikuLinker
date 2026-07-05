package org.haruyq.haruwaikuLinker.Discord.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;

import java.util.List;
import java.util.ArrayList;

public class Players implements SlashCommand {

	private final ProxyServer server;

	public Players(ProxyServer server) {
		this.server = server;
	}

	@Override
	public String name() {
		return "players";
	}

	@Override
	public String description() {
		return "プレイヤーリストを表示します。";
	}

	@Override
	public void execute(SlashCommandInteractionEvent ev) {
		List<String> playerlist = new ArrayList<>();

		for (Player player : server.getAllPlayers()) {
			playerlist.add(player.getUsername());
		}

		String message = String.join(",", playerlist);
		ev.reply(message).queue();
	}
}
