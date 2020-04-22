package com.github.taurvi.valor.clients.discord;

import com.github.taurvi.valor.config.ValorConfig;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.javacord.api.DiscordApi;
import org.javacord.api.DiscordApiBuilder;

public class DiscordApiProvider implements Provider<DiscordApi> {
    private String token;

    @Inject
    public DiscordApiProvider(ValorConfig appConfig) {
        this.token = appConfig.getBotToken();
    }

    @Override
    public DiscordApi get() {
        return new DiscordApiBuilder().setToken(token).login().join();
    }
}
