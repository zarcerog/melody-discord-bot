package com.nicolaszarcero.melody.command.commands.music;

import com.nicolaszarcero.lavaplayer.GuildMusicManager;
import com.nicolaszarcero.lavaplayer.PlayerManager;
import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class RepeatCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (!selfVoiceState.inVoiceChannel()) {
            channel.sendMessage("Melody needs to be in a VC for this command to work!").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Melody requires you to be in a VC for this command to work!").queue();
            return;
        }

        if (!memberVoiceState.getChannel().equals(selfVoiceState.getChannel())) {
            channel.sendMessage("Melody requires you to be in the same VC as me for this command to work!").queue();
            return;
        }

        final GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(ctx.getGuild());
        final boolean newRepeating = !musicManager.scheduler.repeating;

        musicManager.scheduler.repeating = newRepeating;

        channel.sendMessageFormat("Melody %s", newRepeating ? "will" + " repeat this song when it ends!" : "won't" + " repeat this song when it ends!").queue();
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getHelp() {
        return "Loops the song that is playing.";
    }

    @Override
    public List<String> getAliases() {
        return List.of("r");
    }
}
