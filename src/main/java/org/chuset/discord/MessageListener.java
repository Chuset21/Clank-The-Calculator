package org.chuset.discord;

import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class MessageListener extends ListenerAdapter {

    private final User selfUser;
    private final MessageTextHandler handler;

    public MessageListener(User selfUser, MessageTextHandler handler) {
        this.selfUser = selfUser;
        this.handler = handler;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println(event.getAuthor().getName());
        System.out.println(event.getMessage().getChannel().getName());
        System.out.println(event.getMessage().getContentRaw());
        System.out.println(event.getGuild().getName());

        if(selfUser.getIdLong() != event.getAuthor().getIdLong()) {
            MessageChannel channel = event.getChannel();
            try {
                channel.sendMessage(handler.handleMessage(event.getMessage().getContentRaw())).queue();
            } catch (Exception e) {
                e.printStackTrace();
                channel.sendMessage("There was an error handling the message.").queue();
            }
        }
    }
}
