package com.github.taurvi.valor.commands;

import com.github.taurvi.valor.clients.discord.EmbedBuilderProvider;
import com.github.taurvi.valor.clients.discord.MessageBuilderProvider;
import com.github.taurvi.valor.exceptions.ValorException;
import com.github.taurvi.valor.listeners.react.RollcallReactListener;
import com.google.inject.Inject;
import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.embed.EmbedBuilder;
import org.javacord.api.event.message.MessageCreateEvent;

import java.awt.*;
import java.util.concurrent.CompletableFuture;

public class Rollcall implements ValorCommand {
    private static final String COMMAND = "rollcall";
    private static final String COMMAND_WITH_PREFIX = String.format(COMMAND_TEMPLATE_WITH_PARAMS, COMMAND);
    public static final String ALARMCLOCK_IMAGE = "https://emojipedia-us.s3.dualstack.us-west-1.amazonaws.com/thumbs/240/google/241/police-car-light_1f6a8.png";
    private MessageBuilderProvider messageBuilderProvider;
    private EmbedBuilderProvider embedBuilderProvider;
    private RollcallReactListener rollcallReactListener;

    @Inject
    public Rollcall(MessageBuilderProvider messageBuilderProvider, EmbedBuilderProvider embedBuilderProvider,
                    RollcallReactListener rollcallReactListener) {
        this.messageBuilderProvider = messageBuilderProvider;
        this.embedBuilderProvider = embedBuilderProvider;
        this.rollcallReactListener = rollcallReactListener;
    }

    @Override
    public boolean isBeingInvokedAndIsValid(MessageCreateEvent event) {
        String messageContent = event.getMessageContent();
        if (isBeingInvoked(messageContent)) {
            String params = getParams(messageContent);
            return !event.getMessageAuthor().isBotUser() && !params.isEmpty();
        }
        return false;
    }

    @Override
    public void execute(MessageCreateEvent event) {
        String messageContent = event.getMessageContent();
        String params = getParams(messageContent);

        messageBuilderProvider.get()
                .append(String.format("@everyone ⚠️ New Rollcall: %s", params))
                .send(event.getChannel());

        EmbedBuilder rollcallEmbed = embedBuilderProvider.get()
                .setTitle("__ROLLCALL CHECK IN__")
                .setDescription(String.format("Target: %s", params))
                .setThumbnail(ALARMCLOCK_IMAGE)
                .addField("**React Key**", "⚔️: Setters \n\n 🛡: Fillers \n\n 🚫: Cancel Rollcall \n\n ✅: End Rollcall")
                .setColor(Color.RED);

        CompletableFuture<Message> futureMessage = messageBuilderProvider.get()
                .setEmbed(rollcallEmbed)
                .send(event.getChannel());

        try {
            Message message = futureMessage.get();
            message.addReaction(EmojiParser.parseToUnicode(":crossed_swords:"));
            message.addReaction(EmojiParser.parseToUnicode(":shield:"));
            message.addReaction(EmojiParser.parseToUnicode(":no_entry_sign:"));
            message.addReaction(EmojiParser.parseToUnicode(":white_check_mark:"));
            rollcallReactListener.setOriginalEvent(event);
            rollcallReactListener.setOriginalMessage(message);
            message.addReactionAddListener(rollcallReactListener);
        } catch (Exception exception) {
            throw new ValorException(exception);
        }
    }

    private boolean isBeingInvoked(String messageContent) {
        return messageContent.startsWith(COMMAND_WITH_PREFIX);
    }

    private String getParams(String messageContent) {
        return messageContent.substring(COMMAND_WITH_PREFIX.length());
    }
}
