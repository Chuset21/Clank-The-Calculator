package org.chuset.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class BotMain {

    public static void main(String[] args) throws Exception {
        String jdaToken = System.getenv("JDA_TOKEN");
        JDA jda = JDABuilder.createDefault(jdaToken).build();
        jda.addEventListener(new MessageListener(jda.getSelfUser(), new CalculatorMessageHandler()));
    }
}
