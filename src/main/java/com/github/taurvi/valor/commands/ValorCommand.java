package com.github.taurvi.valor.commands;

import org.javacord.api.event.message.MessageCreateEvent;

public interface ValorCommand {
    String COMMAND_TEMPLATE = "!%s";
    String COMMAND_TEMPLATE_WITH_PARAMS = COMMAND_TEMPLATE.concat(" ");

    boolean isBeingInvokedAndIsValid(MessageCreateEvent event);

    void execute(MessageCreateEvent event);
}
