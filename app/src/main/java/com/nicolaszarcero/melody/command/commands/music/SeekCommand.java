package com.nicolaszarcero.melody.command.commands.music;

import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SeekCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();


    }

    @Override
    public String getName() {
        return "seek";
    }

    @Override
    public String getHelp() {
        return "Let's you skip to a certain part ";
    }

    @Override
    public List<String> getAliases() {
        return List.of("s", "jump", "saltar");
    }
}
