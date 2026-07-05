package org.haruyq.haruwaikuLinker.Events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import net.kyori.adventure.text.Component;
import org.haruyq.haruwaikuLinker.Discord.Sender;

import com.velocitypowered.api.proxy.ProxyServer;

public class OnPlayerChat {

    private final Sender sender;
    private final ProxyServer server;

    public OnPlayerChat(Sender sender, ProxyServer server) {
        this.sender = sender;
        this.server = server;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent ev) {
        String msg = ev.getMessage();
        Player player = ev.getPlayer();

        for (Player p : server.getAllPlayers()) {
            if (p.getCurrentServer() != player.getCurrentServer()) {
                String message = "[" + player.getCurrentServer().toString() + "] " + "<" + player.getUsername() + ">"
                        + msg;
                p.sendMessage(Component.text(message));
            }
        }

        sender.broadcast("[" + player.getCurrentServer().toString() + "] " + player.getUsername() + " » " + msg);
    }
}
