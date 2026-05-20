package org.haruyq.haruwaikuLinker.Discord.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.Player;
import java.util.Optional;

import java.util.List;

public class SquareMapLink implements SlashCommand {

    private final ProxyServer server;
    private final String baseUrl;

    public SquareMapLink(ProxyServer server, String baseUrl) {
        this.server = server;
        this.baseUrl = baseUrl;
    }

    @Override
    public String name() { return "squaremap-link"; }

    @Override
    public String description() { return "SquareMapのURLを取得します。"; }

    @Override
    public List<OptionData> options() {
        return List.of(
                new OptionData(OptionType.STRING, "user", "対象のMCID", true)
        );
    }

    @Override
    public void execute(SlashCommandInteractionEvent ev) {
        String username = ev.getOption("user") != null ? ev.getOption("user").getAsString().trim() : "";
        if (username.isEmpty()) {
            ev.reply("MCIDを指定してください。").setEphemeral(true).queue();
            return;
        }

        Optional<Player> player = server.getPlayer(username);
        if (player.isEmpty()) {
            ev.reply("そのプレイヤーは現在オンラインではありません。").setEphemeral(true).queue();
            return;
        }

        String uuid = player.get().getUniqueId().toString().replace("-", "");
        String link = normalizeBaseUrl(baseUrl) + "/?uuid=" + uuid;
        ev.reply(link).queue();
    }

    private String normalizeBaseUrl(String url) {
        if (url == null) {
            return "";
        }
        String trimmed = url.trim();
        if (trimmed.endsWith("/")) {
            return trimmed.substring(0, trimmed.length() - 1);
        }
        return trimmed;
    }
}
