package com.nicolaszarcero.melody.command.commands.music;

import com.nicolaszarcero.lavaplayer.GuildMusicManager;
import com.nicolaszarcero.lavaplayer.PlayerManager;
import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class SkipCommand implements ICommand {
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
        final AudioPlayer audioPlayer = musicManager.audioPlayer;

        if (audioPlayer.getPlayingTrack() == null) {
            channel.sendMessage("Melody couldn't find any song playing!").queue();
            return;
        }

        musicManager.scheduler.nextTrack();
        channel.sendMessage("Melody skipped the current track!").queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getHelp() {
        return "Melody will skip the current track!";
    }

    @Override
    public List<String> getAliases() {
        return List.of("n");
    }
}
