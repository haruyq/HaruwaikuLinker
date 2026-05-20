package org.haruyq.haruwaikuLinker.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

import net.dv8tion.jda.api.requests.GatewayIntent;
import org.haruyq.haruwaikuLinker.Config.Config;
import org.haruyq.haruwaikuLinker.Discord.Events.*;
import org.haruyq.haruwaikuLinker.Discord.Commands.CommandManager;
import org.haruyq.haruwaikuLinker.Discord.Commands.CommandModule;
import org.slf4j.Logger;

public class Bot {

    private Config config;
    private JDA jda;
    private Logger log;
    private final org.haruyq.haruwaikuLinker.Sender minecraftSender;
    private final CommandManager commandManager;

    public Bot(Config config, Logger log, org.haruyq.haruwaikuLinker.Sender minecraftSender) {
        this.config = config;
        this.log = log;
        this.minecraftSender = minecraftSender;
        this.commandManager = new CommandManager(config, log);
    }

    public void init() {
        this.jda = JDABuilder.createDefault(config.discord.token)
                .addEventListeners(new OnMessage(config, minecraftSender), commandManager)
                .enableIntents(
                        GatewayIntent.MESSAGE_CONTENT
                )
                .build();
    }

    public void shutdown() {
        if (this.jda != null) {
            this.jda.shutdown();
        }
    }

    public JDA getJda() {
        return jda;
    }

    public void registerCommandModule(CommandModule module) {
        commandManager.registerModule(module);
    }
}
