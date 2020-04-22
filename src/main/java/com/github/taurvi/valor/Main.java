package com.github.taurvi.valor;

import com.github.taurvi.valor.listeners.text.CommandListener;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.javacord.api.DiscordApi;

public class Main {
    public static void main(String args[]) {
        Injector injector = Guice.createInjector(new CoreModule());

        DiscordApi discordApi = injector.getInstance(DiscordApi.class);
        CommandListener commandListener = injector.getInstance(CommandListener.class);

        discordApi.addMessageCreateListener(commandListener);

        System.out.println(discordApi.createBotInvite());
    }
}
