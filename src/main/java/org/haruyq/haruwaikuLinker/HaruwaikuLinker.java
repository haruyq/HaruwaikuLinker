package org.haruyq.haruwaikuLinker;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import org.haruyq.haruwaikuLinker.Config.Config;
import org.haruyq.haruwaikuLinker.Config.ConfigManager;
import org.haruyq.haruwaikuLinker.Discord.Bot;
import org.haruyq.haruwaikuLinker.Discord.Commands.SquareMapLink;
import org.haruyq.haruwaikuLinker.Events.*;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.List;

public class HaruwaikuLinker {

    private final ProxyServer server;
    private final Logger log;
    private final Config config;
    private final Bot bot;

    private org.haruyq.haruwaikuLinker.Discord.Sender dscSender;
    private final Sender mcSender;

    @Inject
    public HaruwaikuLinker(ProxyServer server, Logger log, @DataDirectory Path dataDir) {
        ConfigManager configManager = new ConfigManager();
        configManager.loadConfig(dataDir.resolve("config.toml"));
        this.config = configManager.getConfig();

        this.mcSender = new Sender(server, log);

        this.server = server;
        this.log = log;
        this.bot = new Bot(config, log, mcSender);
    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        try {
            bot.registerCommandModule(() -> List.of(
                    new SquareMapLink(server, config.squaremap.baseUrl)
            ));
            bot.init();

            this.dscSender = new org.haruyq.haruwaikuLinker.Discord.Sender(bot.getJda(), config.discord.channelId, log);
            server.getEventManager().register(this, new OnPlayerChat(dscSender));
            server.getEventManager().register(this, new OnPlayerJoin(dscSender));
            server.getEventManager().register(this, new OnPlayerLeft(dscSender));

            log.info("HaruwaikuLinkerが有効になりました。");

        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
