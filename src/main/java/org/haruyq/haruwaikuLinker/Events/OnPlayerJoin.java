package org.haruyq.haruwaikuLinker.Events;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.PostLoginEvent;
import com.velocitypowered.api.proxy.Player;
import org.haruyq.haruwaikuLinker.Discord.Sender;

public class OnPlayerJoin {

    private final Sender sender;

    public OnPlayerJoin(Sender sender) {
        this.sender = sender;
    }

    @Subscribe
    public void onPlayerJoin(PostLoginEvent ev) {
        Player player = ev.getPlayer();

        sender.broadcast("Server » " + player.getUsername() + "がゲームに参加しました");
    }
}
