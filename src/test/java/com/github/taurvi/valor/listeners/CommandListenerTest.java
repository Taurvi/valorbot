package com.github.taurvi.valor.listeners;

import com.github.taurvi.valor.commands.CommandRegistry;
import com.github.taurvi.valor.commands.ValorCommand;
import com.github.taurvi.valor.listeners.text.CommandListener;
import com.google.common.collect.ImmutableList;
import org.javacord.api.event.message.MessageCreateEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

public class CommandListenerTest {
    @Mock
    private CommandRegistry commandRegistry;
    @Mock
    private ValorCommand command;
    @Mock
    private MessageCreateEvent event;
    private CommandListener sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        given(commandRegistry.getCommandList()).willReturn(ImmutableList.of(command));
        this.sut = new CommandListener(commandRegistry);
    }

    @Test
    public void should_do_nothing_if_command_is_not_being_invoked_or_valid() {
        // Given
        given(command.isBeingInvokedAndIsValid(event)).willReturn(false);

        // When
        sut.onMessageCreate(event);

        // Then
        then(command).should().isBeingInvokedAndIsValid(eq(event));
        then(command).shouldHaveNoMoreInteractions();
    }

    @Test
    public void should_execute_command_if_being_invoked_and_valid() {
        // Given
        given(command.isBeingInvokedAndIsValid(event)).willReturn(true);

        // When
        sut.onMessageCreate(event);

        // Then
        then(command).should().isBeingInvokedAndIsValid(eq(event));
        then(command).should().execute(eq(event));
    }
}
