package org.chuset.discord;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;

public class BotMain {

    public static void main(String[] args) throws Exception {
        final String jdaToken = System.getenv("JDA_TOKEN");
        final JDA jda = JDABuilder.createDefault(jdaToken)
                .setChunkingFilter(ChunkingFilter.ALL) // enable member chunking for all guilds
                .setMemberCachePolicy(MemberCachePolicy.ALL) // ignored if chunking enabled
                .enableIntents(GatewayIntent.GUILD_MEMBERS)
                .build();
        jda.addEventListener(new Handler(jda.getSelfUser()));
    }
}
