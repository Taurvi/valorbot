package com.github.taurvi.valor.listeners.text;

import com.github.taurvi.valor.commands.CommandRegistry;
import com.github.taurvi.valor.commands.ValorCommand;
import com.github.taurvi.valor.exceptions.ValorException;
import com.google.inject.Inject;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.listener.message.MessageCreateListener;

import java.util.List;

public class CommandListener implements MessageCreateListener {
    private List<ValorCommand> commandList;

    @Inject
    public CommandListener(CommandRegistry commandRegistry) {
        this.commandList = commandRegistry.getCommandList();
    }

    @Override
    public void onMessageCreate(MessageCreateEvent event) {
        commandList.forEach(command -> checkIsBeingInvokedAndExecuteCommand(command, event));
    }

    private void checkIsBeingInvokedAndExecuteCommand(ValorCommand command, MessageCreateEvent event) {
        if (command.isBeingInvokedAndIsValid(event)) {
            command.execute(event);
        }
    }
}
