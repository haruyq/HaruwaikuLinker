package org.haruyq.haruwaikuLinker.Discord.Events;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.haruyq.haruwaikuLinker.Config.Config;
import org.haruyq.haruwaikuLinker.Sender;
import org.haruyq.haruwaikuLinker.Utils.ChatRegex;

import java.awt.*;

public class OnMessage extends ListenerAdapter {

    private final Config config;
    private final Sender sender;

    public OnMessage(Config config, Sender sender) {
        this.config = config;
        this.sender = sender;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent ev) {
        if (ev.getAuthor().isBot()) return;

        if (!ev.getChannelType().isMessage() ||
                ev.getChannel().getIdLong() != config.discord.channelId) return;

        Message msg = ev.getMessage();
        String author = ChatRegex.escapeMiniMessage(msg.getAuthor().getEffectiveName());
        String content = ChatRegex.decorateLinks(msg.getContentRaw());
        String attachments = formatAttachments(msg);
        if (!attachments.isEmpty()) {
            if (!content.isEmpty()) {
                content = content + " " + attachments;
            } else {
                content = attachments;
            }
        }

        String memberHexColor = "#ffffff";
        if (msg.getMember() != null && msg.getMember().getColor() != null) {
            Color color = msg.getMember().getColor();
            memberHexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        }

        author = "<" + memberHexColor + ">" + author + "</" + memberHexColor + ">";

        sender.broadcast("<aqua>[Discord]</aqua> " + author + " » " + content);
    }

    private String formatAttachments(Message msg) {
        if (msg.getAttachments().isEmpty()) {
            return "";
        }

        StringBuilder out = new StringBuilder();
        for (Message.Attachment attachment : msg.getAttachments()) {
            if (out.length() > 0) {
                out.append(" ");
            }
            String name = ChatRegex.escapeMiniMessage(attachment.getFileName());
            String url = ChatRegex.decorateLinks(attachment.getUrl());
            out.append("<gray>[File]</gray> ").append(name).append(": ").append(url);
        }
        return out.toString();
    }
}
