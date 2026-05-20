package org.haruyq.haruwaikuLinker.Config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.toml.TomlFormat;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class ConfigManager {
    private Config config;

    public void loadConfig(Path filePath) {
        try {
            Path parent = filePath.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to prepare config file: " + filePath, e);
        }

        CommentedFileConfig fileConfig = CommentedFileConfig.builder(filePath.toString(), TomlFormat.instance())
                .concurrent()
                .autoreload()
                .build();

        fileConfig.load();

        this.config = new Config();

        // -- Config --
        this.config.discord.token = fileConfig.getOrElse("discord.token", this.config.discord.token);
        this.config.discord.channelId = getLong(fileConfig, "discord.channelId", this.config.discord.channelId);
        this.config.discord.guildId = getLong(fileConfig, "discord.guildId", this.config.discord.guildId);

        this.config.squaremap.baseUrl = fileConfig.getOrElse("squaremap.baseUrl", this.config.squaremap.baseUrl);

        fileConfig.set("discord.token", this.config.discord.token);
        fileConfig.set("discord.channelId", this.config.discord.channelId);
        fileConfig.set("discord.guildId", this.config.discord.guildId);

        fileConfig.set("squaremap.baseUrl", this.config.squaremap.baseUrl);
        // -- End --

        fileConfig.save();
    }

    private long getLong(CommentedFileConfig fileConfig, String path, long defaultValue) {
        Object value = fileConfig.get(path);
        if (value instanceof Number number) {
            return number.longValue();
        }
        if (value instanceof String text) {
            try {
                return Long.parseLong(text);
            } catch (NumberFormatException ignored) {
                return defaultValue;
            }
        }
        return defaultValue;
    }

    public Config getConfig() {
        return config;
    }

    public void log() {

    }
}