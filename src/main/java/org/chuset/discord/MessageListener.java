package org.chuset.discord;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private final User selfUser;
    private final MessageTextHandler handler;

    public MessageListener(final User selfUser, final MessageTextHandler handler) {
        this.selfUser = selfUser;
        this.handler = handler;
    }

    @Override
    public void onMessageReceived(final MessageReceivedEvent event) {
        if (selfUser.getIdLong() != event.getAuthor().getIdLong()) {
            final MessageChannel channel = event.getChannel();
            try {
                final Message message = event.getMessage();
                if (message.isMentioned(selfUser)) {
                    channel.sendMessage(handler.handleMessage(
                            message.getContentRaw().replaceAll("<@.*>", "").trim())).queue();
                }
            } catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e) {
                e.printStackTrace();
            } catch (Exception ignored) {
                try {
                    channel.sendMessage("There was an error handling the message.").queue();
                } catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
