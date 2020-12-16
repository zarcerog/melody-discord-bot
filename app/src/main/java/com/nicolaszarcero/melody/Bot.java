package com.nicolaszarcero.melody;

import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import javax.security.auth.login.LoginException;
import java.util.EnumSet;

public class Bot {

    private Bot() {

        try {
            JDABuilder.createDefault(
                    Config.get("token"),
                    GatewayIntent.GUILD_MEMBERS,
                    GatewayIntent.GUILD_MESSAGES,
                    GatewayIntent.GUILD_VOICE_STATES,
                    GatewayIntent.GUILD_EMOJIS
            )
                    .disableCache(EnumSet.of(
                            CacheFlag.CLIENT_STATUS,
                            CacheFlag.ACTIVITY,
                            CacheFlag.EMOTE
                    ))
                    .enableCache(CacheFlag.VOICE_STATE)
                    .addEventListeners(new Listener())
                    .setActivity(Activity.listening("| %help"))
                    .setStatus(OnlineStatus.ONLINE)
                    .build();
        } catch (LoginException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws LoginException {
        new Bot();
    }
}
