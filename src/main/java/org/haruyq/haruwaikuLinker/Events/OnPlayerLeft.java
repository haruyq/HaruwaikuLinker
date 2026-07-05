package org.haruyq.haruwaikuLinker.Events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import org.haruyq.haruwaikuLinker.Discord.Sender;

public class OnPlayerLeft {

    private final Sender sender;

    public OnPlayerLeft(Sender sender) {
        this.sender = sender;
    }

    @Subscribe
    public void onPlayerLeft(DisconnectEvent ev) {
        Player player = ev.getPlayer();

        sender.broadcast(player.getUsername() + "がゲームから退出しました");
    }
}
