package com.nicolaszarcero.melody.command.commands;

import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;

public class InviteCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        String url = "https://discord.com/api/oauth2/authorize?client_id=777563702600925184&permissions=8&scope=bot";

        ctx.getEvent().getChannel().sendMessage(url).queue();

    }

    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getHelp() {
        return "Sends you an invitation to add Melody to your discord server!";
    }
}
