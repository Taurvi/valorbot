package com.github.taurvi.valor;

import com.github.taurvi.valor.clients.discord.DiscordApiProvider;
import com.github.taurvi.valor.clients.discord.EmbedBuilderProvider;
import com.github.taurvi.valor.clients.discord.MessageBuilderProvider;
import com.github.taurvi.valor.config.BaseConfig;
import com.github.taurvi.valor.config.ValorConfig;
import com.google.inject.AbstractModule;
import org.javacord.api.DiscordApi;

public class CoreModule extends AbstractModule {
    @Override
    public void configure() {
        bind(ValorConfig.class).to(BaseConfig.class);
        bind(DiscordApi.class).toProvider(DiscordApiProvider.class);
        bind(MessageBuilderProvider.class).toInstance(new MessageBuilderProvider());
        bind(EmbedBuilderProvider.class).toInstance(new EmbedBuilderProvider());
    }
}
