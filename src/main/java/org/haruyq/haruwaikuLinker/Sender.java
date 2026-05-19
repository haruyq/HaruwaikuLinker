package org.haruyq.haruwaikuLinker;

import com.velocitypowered.api.proxy.ProxyServer;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.slf4j.Logger;

public class Sender {

    private final ProxyServer server;
    private final Logger log;

    public Sender(ProxyServer server, Logger log) {
        this.server = server;
        this.log = log;
    }

    public void broadcast(String msg) {
        var mm = MiniMessage.miniMessage();
        var component = mm.deserialize(msg);
        server.sendMessage(component);
    }
}
