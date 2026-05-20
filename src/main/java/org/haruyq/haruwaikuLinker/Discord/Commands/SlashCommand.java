package org.haruyq.haruwaikuLinker.Discord.Commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface SlashCommand {
    String name();

    String description();

    default List<OptionData> options() {
        return List.of();
    }

    void execute(SlashCommandInteractionEvent ev);
}

