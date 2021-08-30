package org.chuset.discord;

import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.CancellationException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageListener extends ListenerAdapter {

    private final User selfUser;

    public MessageListener(final User selfUser) {
        this.selfUser = selfUser;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (selfUser.getIdLong() != event.getAuthor().getIdLong()) {
            final Message message = event.getMessage();
            try {
                if (message.isMentioned(selfUser)) {
                    final String rawMessage = message.getContentRaw().replaceFirst("<@.\\d+>", "").trim();
                    final Matcher m = Pattern.compile("^(.*)\\s+(\\d+)$").matcher(rawMessage);
                    if (!m.find() || m.groupCount() != 2) {
                        throw new RuntimeException("No Matches");
                    }

                    final TextChannel channel = event.getTextChannel();
                    channel.sendMessage("Prepare yourself\nYou will enjoy this.").complete().delete().complete();

                    final int limit = Math.max(Math.min(Integer.parseInt(m.group(2)), 100), 1);
                    final String text = m.group(1);
                    for (int i = 0; i < limit; i++) {
                        channel.sendMessage(text).complete().delete().complete();
                    }
                    channel.sendMessage("Trolling complete.").complete().delete().complete();
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
