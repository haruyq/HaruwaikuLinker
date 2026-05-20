package org.haruyq.haruwaikuLinker.Discord.Commands;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.haruyq.haruwaikuLinker.Config.Config;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommandManager extends ListenerAdapter {

    private final Config config;
    private final Logger log;
    private final Map<String, SlashCommand> commandMap = new HashMap<>();

    public CommandManager(Config config, Logger log) {
        this.config = config;
        this.log = log;
    }

    public void registerModule(CommandModule module) {
        for (SlashCommand command : module.commands()) {
            commandMap.put(command.name(), command);
        }
    }

    public Collection<SlashCommand> getCommands() {
        return Collections.unmodifiableCollection(commandMap.values());
    }

    @Override
    public void onReady(ReadyEvent ev) {
        registerCommands(ev.getJDA());
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent ev) {
        SlashCommand command = commandMap.get(ev.getName());
        if (command == null) {
            return;
        }

        try {
            command.execute(ev);
        } catch (Exception e) {
            log.error("Failed to execute slash command: {}", ev.getName(), e);
            if (ev.isAcknowledged()) {
                ev.getHook().sendMessage("コマンドの実行中にエラーが発生しました。")
                        .setEphemeral(true)
                        .queue();
            } else {
                ev.reply("コマンドの実行中にエラーが発生しました。")
                        .setEphemeral(true)
                        .queue();
            }
        }
    }

    private void registerCommands(JDA jda) {
        if (commandMap.isEmpty()) {
            log.info("No slash commands to register.");
            return;
        }

        List<CommandData> data = new ArrayList<>();
        for (SlashCommand command : commandMap.values()) {
            SlashCommandData commandData = Commands.slash(command.name(), command.description());
            List<OptionData> options = command.options();
            for (OptionData option : options) {
                commandData.addOptions(option);
            }
            data.add(commandData);
        }

        long guildId = config.discord.guildId;
        if (guildId > 0L) {
            Guild guild = jda.getGuildById(guildId);
            if (guild == null) {
                log.warn("Guild not found for slash command registration. guildId={}", guildId);
                return;
            }
            guild.updateCommands().addCommands(data).queue();
        } else {
            jda.updateCommands().addCommands(data).queue();
        }
    }
}
