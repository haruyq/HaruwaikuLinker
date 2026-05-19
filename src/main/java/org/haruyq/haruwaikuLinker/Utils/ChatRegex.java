package org.haruyq.haruwaikuLinker.Utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatRegex {

    private static final Pattern URL_PATTERN = Pattern.compile("https?://\\S+");

    private ChatRegex() {
    }

    public static String decorateLinks(String message) {
        if (message == null || message.isEmpty()) {
            return "";
        }

        StringBuilder out = new StringBuilder();
        Matcher matcher = URL_PATTERN.matcher(message);
        int lastIndex = 0;
        while (matcher.find()) {
            String before = message.substring(lastIndex, matcher.start());
            out.append(escapeMiniMessage(before));

            String url = matcher.group();
            String safeUrl = url.replace("'", "%27");
            String visibleUrl = escapeMiniMessage(url);

            out.append("<u><click:open_url:'")
                    .append(safeUrl)
                    .append("'><#3477eb>")
                    .append(visibleUrl)
                    .append("</#3477eb></click></u>");

            lastIndex = matcher.end();
        }

        if (lastIndex < message.length()) {
            out.append(escapeMiniMessage(message.substring(lastIndex)));
        }

        return out.toString();
    }

    public static String escapeMiniMessage(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }
        return text
                .replace("\\", "\\\\")
                .replace("<", "\\<")
                .replace(">", "\\>");
    }
}
