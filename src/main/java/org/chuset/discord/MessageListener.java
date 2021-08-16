package org.chuset.discord;

import net.dv8tion.jda.api.entities.Message;
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
            final Message message = event.getMessage();
            try {
                if (message.isMentioned(selfUser)) {
                    message.reply(handler.handleMessage(
                            message.getContentRaw().replaceAll("<@.*>", "").trim())).queue();
                }
            } catch (net.dv8tion.jda.api.exceptions.InsufficientPermissionException e) {
                e.printStackTrace();
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
