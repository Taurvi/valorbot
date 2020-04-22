package com.github.taurvi.valor.listeners.react;

import com.github.taurvi.valor.clients.discord.MessageBuilderProvider;
import com.github.taurvi.valor.exceptions.ValorException;
import com.github.taurvi.valor.utilities.ValorEmoji;
import com.google.inject.Inject;
import com.vdurmont.emoji.EmojiParser;
import org.javacord.api.entity.message.Message;
import org.javacord.api.entity.message.MessageBuilder;
import org.javacord.api.entity.message.Reaction;
import org.javacord.api.entity.user.User;
import org.javacord.api.event.message.MessageCreateEvent;
import org.javacord.api.event.message.reaction.ReactionAddEvent;
import org.javacord.api.listener.message.reaction.ReactionAddListener;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RollcallReactListener implements ReactionAddListener {
    private MessageBuilderProvider messageBuilderProvider;
    private MessageCreateEvent originalEvent;
    private Message originalMessage;

    @Inject
    public RollcallReactListener(MessageBuilderProvider messageBuilderProvider) {
        this.messageBuilderProvider = messageBuilderProvider;
        this.originalEvent = null;
        this.originalMessage = null;
    }

    public void setOriginalEvent(MessageCreateEvent event) {
        this.originalEvent = event;
    }

    public void setOriginalMessage(Message message) {
        this.originalMessage = message;
    }

    @Override
    public void onReactionAdd(ReactionAddEvent event) {
        String userId = event.getUser().getIdAsString();
        if (event.getEmoji().equalsEmoji(ValorEmoji.CHECK.getEmoji()) && getEventRequestCount(event) > 1) {
            if (checkIfOriginalRollcallOwner(userId)) {
                CompletableFuture<Void> removeOwnReactsAndEndRollcall = removeOwnReactsAndEndRollcall();
                try {
                    removeOwnReactsAndEndRollcall.get();
                    postResults();
                } catch (Exception e) {
                    throw new ValorException(e);
                }

            } else {
                event.removeReaction();
            }
        }

        if (event.getEmoji().equalsEmoji(ValorEmoji.CANCEL.getEmoji()) && getEventRequestCount(event) > 1) {
            if (checkIfOriginalRollcallOwner(userId)) {
                originalMessage.delete();
                messageBuilderProvider.get()
                        .append("🚫 Rollcall aborted.")
                        .send(event.getChannel());
            } else {
                event.removeReaction();
            }
        }
    }

    private int getEventRequestCount(ReactionAddEvent event) {
        try {
            return event.requestCount().get();
        } catch (Exception exception) {
            throw new ValorException(exception);
        }
    }

    private boolean checkIfOriginalRollcallOwner(String userId) {
        return userId.equals(originalEvent.getMessageAuthor().getIdAsString());
    }

    private CompletableFuture<Void> removeOwnReactsAndEndRollcall() {
        CompletableFuture<Void> removeOwnSetterReact = originalMessage.removeOwnReactionByEmoji(ValorEmoji.SWORDS.getEmoji());
        CompletableFuture<Void> removeOwnFillerReact = originalMessage.removeOwnReactionByEmoji(ValorEmoji.SHIELD.getEmoji());
        CompletableFuture<Void> removeCancelRolllcallReact = originalMessage.removeReactionByEmoji(ValorEmoji.CANCEL.getEmoji());
        CompletableFuture<Void> removeEndRolllcallReact = originalMessage.removeReactionByEmoji(ValorEmoji.CHECK.getEmoji());

        CompletableFuture<Void> combinedFuture
                = CompletableFuture.allOf(removeOwnSetterReact, removeOwnFillerReact, removeCancelRolllcallReact, removeEndRolllcallReact);

        return combinedFuture;
    }


    private void postResults() {
        try {
            MessageBuilder results = messageBuilderProvider.get()
                    .append("**__ROLLCALL RESULTS:__**")
                    .appendNewLine()
                    .appendNewLine();

            parseResultsForReact(results, EmojiParser.parseToUnicode(":crossed_swords:"), "⚔️ Setters:");
            results.appendNewLine();
            parseResultsForReact(results, EmojiParser.parseToUnicode(":shield:"), "🛡️ Fillers:");

            results.send(originalEvent.getChannel());
        } catch (Exception exception) {
            throw new ValorException(exception);
        }
    }

    private void parseResultsForReact(MessageBuilder results, String emoji, String title) {
        Optional<Reaction> optionalReaction = originalMessage.getReactionByEmoji(emoji);
        results.append(String.format("** %s **", title))
                .appendNewLine();
        if (optionalReaction.isPresent()) {
            Reaction reaction = optionalReaction.get();
            List<User> usersList = getUsersWhoReacted(reaction);
            for (int count = 0; count < usersList.size(); count++) {
                User user = usersList.get(count);
                postUsername(results, count + 1, user);
            }
        } else {
            results.append("Nobody 😭")
                    .appendNewLine();
        }
    }

    private List<User> getUsersWhoReacted(Reaction reaction) {
        CompletableFuture<List<User>> futureUsersList = reaction.getUsers();
        try {
            return futureUsersList.get();
        } catch (Exception e) {
            throw new ValorException(e);
        }
    }

    private void postUsername(MessageBuilder results, int count, User user) {
        String username = user.getMentionTag();
        results.append(String.format("%d. %s", count, username))
                .appendNewLine();
    }
}
