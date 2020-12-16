package com.nicolaszarcero.melody.command.commands.music;

import com.nicolaszarcero.lavaplayer.GuildMusicManager;
import com.nicolaszarcero.lavaplayer.PlayerManager;
import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class LeaveCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {

        final Member member = ctx.getMember();
        final TextChannel channel = ctx.getChannel();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        Guild guild = ctx.getGuild();
        final AudioManager audioManager = guild.getAudioManager();
        final VoiceChannel memberChannel = memberVoiceState.getChannel();
        VoiceChannel voiceChannel = audioManager.getConnectedChannel();

        if (!audioManager.isConnected()) {
            channel.sendMessage("Melody isn't connected to a VC!").queue();
            return;
        }
        if (!memberVoiceState.inVoiceChannel()) {
            channel.sendMessage("Melody requires you to be in the same voice channel as the bot for this command to work!").queue();
            return;
        }
        if (!voiceChannel.getMembers().contains(ctx.getMember())) {
            channel.sendMessage("Melody requires you to be in the same voice channel as the bot for this command to work!").queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getINSTANCE().getMusicManager(guild);

        musicManager.scheduler.repeating = false;
        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();

        audioManager.closeAudioConnection();
        channel.sendMessageFormat("Disconnecting from %s...", memberChannel.getName()).queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getHelp() {
        return "Makes Melody leave your VC.";
    }

    @Override
    public List<String> getAliases() {
        return List.of("l");
    }
}