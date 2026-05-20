package org.haruyq.haruwaikuLinker.Config;

public class Config {
    public static class Discord {
        public String token = "YOUR_TOKEN_HERE";
        public long channelId = 0L;
        public long guildId = 0L;
    }

    public Discord discord = new Discord();

    public static class SquareMap {
        public String baseUrl = "https://example.com/";
    }

    public SquareMap squaremap = new SquareMap();
}
