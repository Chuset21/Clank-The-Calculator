package org.chuset.discord;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class MessageListener extends ListenerAdapter {

    private final static Map<Long, List<String>> USER_REACTION_MAP;
    private final User selfUser;
    private final Map<Long, Boolean> smileMap;

    static {
        USER_REACTION_MAP = new HashMap<>();

        USER_REACTION_MAP.put(243411239861092352L, List.of("<:pepe_clown:881911897237123133")); // Krozo
        USER_REACTION_MAP.put(349654731305779231L, List.of("<:child_mortality:921550210260434994")); // Mickey
        USER_REACTION_MAP.put(671008061938204685L, List.of("<:snailbob:938390896884461648")); // Angry Lady
        USER_REACTION_MAP.put(661924517173657622L, List.of("\uD83D\uDCE2", "\uD83D\uDCA8")); // baZoo
        USER_REACTION_MAP.put(167321102438105088L, List.of("\uD83C\uDF44")); // Pogloser
    }

    public MessageListener(final User selfUser) {
        this.selfUser = selfUser;
        smileMap = new HashMap<>();
    }

    private static final RuntimeException MATCHER_ERROR =
            new RuntimeException("Provide the text to be sent and the number of times for it to be repeated.");
    private static final String DM = "-dm";

    @Override
    public void onMessageReceived(final @NotNull MessageReceivedEvent event) {
        new Thread(() -> harass(event)).start();
    }

    public void harass(final MessageReceivedEvent event) {
        if (selfUser.getIdLong() != event.getAuthor().getIdLong()) {
            final Message message = event.getMessage();
            try {
                USER_REACTION_MAP.getOrDefault(event.getAuthor().getIdLong(), Collections.emptyList()). // Emoji List
                        forEach(emoji -> message.addReaction(emoji).complete());

                final String rawText = message.getContentRaw();

                final long guildId = event.getGuild().getIdLong();
                final String lowercase = rawText.toLowerCase(Locale.ROOT);
                if (lowercase.contains("smile on")) {
                    smileMap.put(guildId, true);
                } else if (lowercase.contains("smile off")) {
                    smileMap.put(guildId, false);
                }

                if (smileMap.getOrDefault(guildId, false)) {
                    message.addReaction("\uD83D\uDE42").complete();
                }

                if (message.isMentioned(selfUser)) {
                    if (rawText.contains(DM)) {
                        final List<User> mentionedUsers = message.getMentionedMembers().stream().
                                map(Member::getUser).
                                filter(u -> u.getIdLong() != selfUser.getIdLong()).
                                collect(Collectors.toList());

                        final Matcher m = Pattern.compile("^([\\s\\S]*)\\s+(\\d+)$").matcher(
                                rawText.replace(DM, "").replaceAll("<@.\\d+>", "").trim());
                        if (!m.find() || m.groupCount() != 2) {
                            throw MATCHER_ERROR;
                        }

                        final int limit = Math.max(Math.min(Integer.parseInt(m.group(2)), 100), 1);
                        final String text = m.group(1);
                        for (final User u : mentionedUsers) {
                            final RestAction<PrivateChannel> action = u.openPrivateChannel();
                            for (int i = 0; i < limit; i++) {
                                action.queue(channel -> channel.sendMessage(text).queue());
                            }
                        }
                    } else {
                        final String rawMessage = rawText.replaceFirst("<@.\\d+>", "").trim();
                        final Matcher m = Pattern.compile("^([\\s\\S]*)\\s+(\\d+)$").matcher(rawMessage);
                        if (!m.find() || m.groupCount() != 2) {
                            throw MATCHER_ERROR;
                        }

                        final TextChannel channel = event.getTextChannel();
                        channel.sendMessage("Prepare yourself\nYou will enjoy this.").complete().delete().queue();

                        final int limit = Math.max(Math.min(Integer.parseInt(m.group(2)), 100), 1);
                        final MessageAction action = channel.sendMessage(m.group(1));
                        for (int i = 0; i < limit; i++) {
                            action.complete().delete().queue();
                        }
                        channel.sendMessage("Trolling complete.").complete().delete().queue();
                    }
                }
            } catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e) {
                e.printStackTrace();
            } catch (CancellationException e) {
                try {
                    message.reply(e.getMessage()).queue();
                } catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e1) {
                    e1.printStackTrace();
                }
            } catch (Exception e) {
                try {
                    message.reply("There was an error handling the message:\n%s".formatted(e.getMessage())).queue();
                } catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e1) {
                    e1.printStackTrace();
                }
            }
        }
    }
}
