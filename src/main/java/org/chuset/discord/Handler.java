package org.chuset.discord;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.RestAction;
import net.dv8tion.jda.api.requests.restaction.MessageAction;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.CancellationException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Handler extends ListenerAdapter {

    private static final String MENTION_REGEX = "<@.\\d+>";
    private static final String PATTERN = "^%s\\s+%s\\s+.*\\s*.*";
    private final static long CHUSET_ID = 241245334767009793L;
    private final static long KROZO_ID = 243411239861092352L;
    private static final RuntimeException MATCHER_ERROR =
            new RuntimeException("Provide the text to be sent and the number of times for it to be repeated.");

    private final User selfUser;
    private final static Set<User> DELETE_USER_MESSAGE_SET = new HashSet<>();
    private final static Map<Long, List<String>> USER_REACTION_MAP = new HashMap<>();
    private static final Map<Command, Set<String>> COMMAND_LIST_MAP = new HashMap<>();
    private static final Map<Long, Set<String>> GUILD_EMOJI_MAP = new HashMap<>(); // Command -> '!set <emoji> (on|off) ?(users)

    static {
        USER_REACTION_MAP.put(KROZO_ID, new ArrayList<>() {{
            add("<:pepe_clown:881911897237123133");
        }}); // Krozo
        USER_REACTION_MAP.put(349654731305779231L, new ArrayList<>() {{
            add("<:child_mortality:921550210260434994");
        }}); // Mickey
        USER_REACTION_MAP.put(671008061938204685L, new ArrayList<>() {{
            add("<:snailbob:938390896884461648");
        }}); // Angry Lady
        USER_REACTION_MAP.put(661924517173657622L, new ArrayList<>() {{
            add("\uD83D\uDCE2");
            add("\uD83D\uDCA8");
        }}); // baZoo
        USER_REACTION_MAP.put(167321102438105088L, new ArrayList<>() {{
            add("\uD83C\uDF44");
        }}); // Pogloser
        USER_REACTION_MAP.put(195633376701579264L, new ArrayList<>() {{
            add("\uD83E\uDD5D");
        }}); // Kiwi

        COMMAND_LIST_MAP.put(Command.DM, new HashSet<>() {{
            add("-dm");
        }});
        COMMAND_LIST_MAP.put(Command.SET, new HashSet<>() {{
            add("--set");
            add("-s");
        }});
        COMMAND_LIST_MAP.put(Command.ON, new HashSet<>() {{
            add("--on");
            add("--true");
            add("-t");
        }});
        COMMAND_LIST_MAP.put(Command.OFF, new HashSet<>() {{
            add("--off");
            add("--false");
            add("-f");
        }});
        COMMAND_LIST_MAP.put(Command.DELETE, new HashSet<>() {{
            add("--delete");
            add("-d");
        }});
    }

    private enum Command {
        SET,
        DM,
        ON,
        OFF,
        DELETE
    }

    public Handler(final User selfUser) {
        this.selfUser = selfUser;
    }

    @Override
    public void onGuildVoiceJoin(GuildVoiceJoinEvent event) {
        final Member member = event.getMember();
        final Guild guild = event.getGuild();
        final Random r = new Random();

        if (r.nextInt(5) == 0) {
            final String nickname = member.getNickname();
            final VoiceChannel vcKick = guild.createVoiceChannel(
                    "bye bye %s".formatted(nickname == null ? member.getUser().getName() : nickname)).complete();
            guild.moveVoiceMember(event.getMember(), vcKick).complete();
            vcKick.delete().completeAfter(1, TimeUnit.SECONDS);
        }
    }

//    @Override
//    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent event) {
//        final Member member = event.getMember();
//        final Guild guild = event.getGuild();
//        final Random r = new Random();
//
//        if (!event.getChannelJoined().getName().contains("bye bye") &&
//                event.getMember().getUser().getIdLong() == KROZO_ID) {
//            final VoiceChannel vcKick = guild.createVoiceChannel(
//                    String.format("bye bye %s -%d", member.getNickname(), r.nextLong())).complete();
//            guild.moveVoiceMember(event.getMember(), vcKick).complete();
//
//            guild.moveVoiceMember(event.getMember(), event.getChannelJoined()).completeAfter(100, TimeUnit.MILLISECONDS);
//            vcKick.delete().complete();
//        }
//    }

//    @Override
//    public void onGuildVoiceDeafen(@Nonnull GuildVoiceDeafenEvent event) {
//        if (event.getMember().getUser().getIdLong() == KROZO_ID) {
//            event.getMember().deafen(!event.isDeafened()).completeAfter(1, TimeUnit.SECONDS);
//        }
//    }
//
//    @Override
//    public void onGuildVoiceGuildMute(@Nonnull GuildVoiceGuildMuteEvent event) {
//        if (event.getMember().getUser().getIdLong() == KROZO_ID) {
//            event.getMember().mute(!event.isGuildMuted()).completeAfter(1, TimeUnit.SECONDS);
//        }
//    }

    private void handleDeleteMessageCommand(MessageReceivedEvent event, String commandString) {
        if (event.getAuthor().getIdLong() == CHUSET_ID) {
            final String option = deleteCommand(Command.SET, deleteCommand(Command.DELETE, commandString)).
                    replaceAll(MENTION_REGEX, "").trim();

            final List<User> mentionedUsers = getMentionedUsersBarSelfUser(event.getMessage());

            if (equalsGivenCommand(Command.ON, option)) {
                DELETE_USER_MESSAGE_SET.addAll(mentionedUsers);
                event.getChannel().sendMessage("Deleting all further messages from %s.".
                        formatted(mentionedUsers.stream().map(User::getName).
                                collect(Collectors.joining(", ")))).complete();
            } else if (equalsGivenCommand(Command.OFF, option)) {
                mentionedUsers.forEach(DELETE_USER_MESSAGE_SET::remove);
                event.getChannel().sendMessage("Giving %s a chance to speak.".
                        formatted(mentionedUsers.stream().map(User::getName).
                                collect(Collectors.joining(", ")))).complete();
            } else {
                event.getMessage().reply("Options for this command are: %s or %s".
                        formatted(buildCommandRegex(Command.ON), buildCommandRegex(Command.OFF))).complete();
            }
        } else {
            event.getMessage().reply("You do not have permissions to use this command.").complete();
        }
    }

    private void deleteUserMessage(MessageReceivedEvent event) {
        if (DELETE_USER_MESSAGE_SET.contains(event.getAuthor())) {
            try {
                event.getMessage().delete().queue();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean startsWithGivenCommand(Command command, String string) {
        return COMMAND_LIST_MAP.get(command).stream().anyMatch(string::startsWith);
    }

    private boolean equalsGivenCommand(Command command, String string) {
        return COMMAND_LIST_MAP.get(command).stream().anyMatch(string::equals);
    }

    private boolean containsGivenCommand(Command command, String string) {
        return COMMAND_LIST_MAP.get(command).stream().anyMatch(string::contains);
    }

    private String buildCommandRegex(Command command) {
        return "(%s)".formatted(String.join("|", COMMAND_LIST_MAP.get(command)));
    }

    private String replaceCommand(Command command, String string, String replacement) {
        String result = string;

        for (String target : COMMAND_LIST_MAP.get(command)) {
            result = result.replace(target, replacement);
        }

        return result;
    }

    private String deleteCommand(Command command, String string) {
        return replaceCommand(command, string, "");
    }

    @Override
    public void onMessageReceived(final @NotNull MessageReceivedEvent event) {
        final Message message = event.getMessage();
        final String lowercaseText = message.getContentRaw().toLowerCase(Locale.ROOT);

        final long guildId = event.getGuild().getIdLong();

        if (startsWithGivenCommand(Command.SET, lowercaseText)) {
            if (containsGivenCommand(Command.DELETE, lowercaseText)) {
                handleDeleteMessageCommand(event, lowercaseText);
            } else {
                mutateReactionMaps(message, lowercaseText, guildId);
            }
        } else {
            new Thread(() -> harass(event)).start();
        }

        reactWithReactionMaps(event, message, guildId);
        deleteUserMessage(event);
    }

    private void reactWithReactionMaps(@NotNull MessageReceivedEvent event, Message message, long guildId) {
        try {
            USER_REACTION_MAP.getOrDefault(event.getAuthor().getIdLong(), Collections.emptyList()). // Emoji List
                    forEach(emoji -> message.addReaction(emoji).complete());

            GUILD_EMOJI_MAP.getOrDefault(guildId, Collections.emptySet()).
                    forEach(emote -> message.addReaction(emote).complete());
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

    private void mutateReactionMaps(Message message, String lowercase, long guildId) {
        final List<User> mentionedUsers = new ArrayList<>(message.getMentionedUsers());

        final String emojiCommand = deleteCommand(Command.SET,
                lowercase.replaceAll("<@.\\d+>", "").replace(">", ""));

        if (lowercase.matches(PATTERN.formatted(buildCommandRegex(Command.SET), buildCommandRegex(Command.ON)))) {
            final String emoji = deleteCommand(Command.ON, emojiCommand).trim();

            if (!mentionedUsers.isEmpty()) {
                mentionedUsers.stream().map(ISnowflake::getIdLong).
                        forEach(id -> USER_REACTION_MAP.compute(id, (k, v) -> {
                            if (v == null) {
                                v = new ArrayList<>();
                            }
                            v.add(emoji);
                            return v;
                        }));
                message.reply("Setting reaction: %s for %s".formatted(emoji, mentionedUsers.stream().map(User::getName).
                        collect(Collectors.joining(", ")))).complete();
            } else {
                GUILD_EMOJI_MAP.compute(guildId, (k, v) -> {
                    if (v == null) {
                        v = new HashSet<>();
                    }
                    v.add(emoji);
                    return v;
                });
                message.reply("Setting reaction: %s for this server".formatted(emoji)).complete();
            }
        } else if (lowercase.matches(PATTERN.
                formatted(buildCommandRegex(Command.SET), buildCommandRegex(Command.OFF)))) {
            final String emoji = deleteCommand(Command.OFF, emojiCommand).trim();

            if (!mentionedUsers.isEmpty()) {
                mentionedUsers.stream().map(ISnowflake::getIdLong).
                        forEach(id -> USER_REACTION_MAP.getOrDefault(id, new ArrayList<>()).remove(emoji));
                message.reply("Taking off reaction: %s for %s".formatted(emoji, mentionedUsers.stream().map(User::getName).
                        collect(Collectors.joining(", ")))).complete();
            } else {
                GUILD_EMOJI_MAP.getOrDefault(guildId, new HashSet<>()).remove(emoji);
                message.reply("Taking off reaction: %s for this server".formatted(emoji)).complete();
            }
        }
    }

    public void harass(final MessageReceivedEvent event) {
        if (selfUser.getIdLong() != event.getAuthor().getIdLong()) {
            final Message message = event.getMessage();
            try {
                final String rawText = message.getContentRaw();

                if (message.isMentioned(selfUser)) {
                    if (containsGivenCommand(Command.DM, rawText)) {
                        final List<User> mentionedUsers = getMentionedUsersBarSelfUser(message);

                        final Matcher m = Pattern.compile("^([\\s\\S]*)\\s+(\\d+)$").matcher(
                                deleteCommand(Command.DM, rawText).replaceAll(MENTION_REGEX, "").trim());
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

    @NotNull
    private List<User> getMentionedUsersBarSelfUser(Message message) {
        return message.getMentionedMembers().stream().
                map(Member::getUser).
                filter(u -> u.getIdLong() != selfUser.getIdLong()).
                collect(Collectors.toList());
    }
}
