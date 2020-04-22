package com.github.taurvi.valor.clients.discord;

import com.google.inject.Provider;
import org.javacord.api.entity.message.embed.EmbedBuilder;

public class EmbedBuilderProvider implements Provider<EmbedBuilder> {
    @Override
    public EmbedBuilder get() {
        return new EmbedBuilder();
    }
}
