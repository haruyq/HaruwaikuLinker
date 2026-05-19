package org.haruyq.haruwaikuLinker.Discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.slf4j.Logger;

public class Sender {

    private final JDA jda;
    private final long channelId;
    private final Logger log;

    public Sender(JDA jda, long channelId, Logger log) {
        this.jda = jda;
        this.channelId = channelId;
        this.log = log;
    }

    public void broadcast(String message) {
        if (message == null || message.isBlank()) {
            return;
        }

        TextChannel channel = jda.getTextChannelById(channelId);
        if (channel == null) {
            log.warn("チャンネルが存在しません。 channelId: {}", channelId);
            return;
        }

        channel.sendMessage(message).queue(
                success -> { },
                error -> log.error("メッセージの送信に失敗しました。", error)
        );
    }
}
