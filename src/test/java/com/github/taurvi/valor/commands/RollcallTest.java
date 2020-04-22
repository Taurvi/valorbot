package com.github.taurvi.valor.commands;

import com.github.taurvi.valor.clients.discord.EmbedBuilderProvider;
import com.github.taurvi.valor.clients.discord.MessageBuilderProvider;
import com.github.taurvi.valor.listeners.react.RollcallReactListener;
import org.javacord.api.entity.message.MessageAuthor;
import org.javacord.api.event.message.MessageCreateEvent;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

public class RollcallTest {
    private static final String COMMAND_PREFIX = "!rollcall";
    private static final String COMMAND_PREFIX_WITH_PARAMS = COMMAND_PREFIX.concat(" param");
    private static final String BAD_COMMAND_PREFIX = "!badcommand";
    @Mock
    private MessageBuilderProvider messageBuilderProvider;
    @Mock
    private EmbedBuilderProvider embedBuilderProvider;
    @Mock
    private RollcallReactListener rollcallReactListener;
    @Mock
    private MessageCreateEvent event;
    @Mock
    private MessageAuthor messageAuthor;

    private Rollcall sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        this.sut = new Rollcall(messageBuilderProvider, embedBuilderProvider, rollcallReactListener);
    }

    @Test
    public void should_return_false_if_command_prefix_does_not_match() {
        // Given
        given(event.getMessageContent()).willReturn(BAD_COMMAND_PREFIX);

        // When
        boolean response = sut.isBeingInvokedAndIsValid(event);

        // Then
        assertThat(response).isFalse();
    }

    @Test
    public void should_return_false_if_command_invoked_by_bot() {
        // Given
        given(event.getMessageAuthor()).willReturn(messageAuthor);
        given(messageAuthor.isBotUser()).willReturn(true);

        // When
        boolean response = sut.isBeingInvokedAndIsValid(event);

        // Then
        assertThat(response).isFalse();
    }

    @Test
    public void should_return_false_if_command_invoked_without_params() {
        // Given
        given(event.getMessageContent()).willReturn(COMMAND_PREFIX);

        // When
        boolean response = sut.isBeingInvokedAndIsValid(event);

        // Then
        assertThat(response).isFalse();
    }

    @Test
    public void should_return_true_if_command_invoked_with_params() {
        // Given
        given(event.getMessageContent()).willReturn(COMMAND_PREFIX_WITH_PARAMS);

        // When
        boolean response = sut.isBeingInvokedAndIsValid(event);

        // Then
        assertThat(response).isTrue();
    }
}
