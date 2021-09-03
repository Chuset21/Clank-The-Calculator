package org.chuset.discord;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.List;
import java.util.concurrent.CancellationException;
import java.util.stream.Collectors;

public class MessageListener extends ListenerAdapter {

    private final User selfUser;

    public MessageListener(final User selfUser) {
        this.selfUser = selfUser;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        final Message message = event.getMessage();
        try {
            final String text;
            if (message.isMentioned(selfUser)) {
                text = message.getContentRaw().replaceAll("<@.\\d+>", "").trim();
                final List<User> mentionedUsers = message.getMentionedMembers().stream().
                        map(Member::getUser).
                        filter(u -> u.getIdLong() != selfUser.getIdLong()).
                        collect(Collectors.toList());
                for (final User u : mentionedUsers) {
                    u.openPrivateChannel().queue(channel -> channel.sendMessage(text).queue());
                }
            } else if (event.isFromType(ChannelType.PRIVATE)) {
                text = message.getContentRaw().trim();
                event.getChannel().sendMessage(text).queue();
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
