package org.haruyq.haruwaikuLinker.Events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.PlayerChatEvent;
import com.velocitypowered.api.proxy.Player;
import org.haruyq.haruwaikuLinker.Discord.Sender;

public class OnPlayerChat {

    private final Sender sender;

    public OnPlayerChat(Sender sender) {
        this.sender = sender;
    }

    @Subscribe
    public void onPlayerChat(PlayerChatEvent ev) {
        String msg = ev.getMessage();
        Player player = ev.getPlayer();

        sender.broadcast("[" + player.getCurrentServer().toString() + "] " + player.getUsername() + " » " + msg);
    }
}
