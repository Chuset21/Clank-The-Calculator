package org.chuset.discord;

public class RespondMessageHandler implements MessageTextHandler{
    @Override
    public String handleMessage(String message) throws Exception {
        return "You said %s".formatted(message);
    }
}
