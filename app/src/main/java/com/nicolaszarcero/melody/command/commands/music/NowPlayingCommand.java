package com.nicolaszarcero.melody.command.commands.music;

import com.nicolaszarcero.lavaplayer.GuildMusicManager;
import com.nicolaszarcero.lavaplayer.PlayerManager;
import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.awt.*;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NowPlayingCommand implements ICommand {

    private String formatTime(long timeInMillis) {
        final long hours = timeInMillis / TimeUnit.HOURS.toMillis(1);
        final long minutes = timeInMillis / TimeUnit.MINUTES.toMillis(1);
        final long seconds = timeInMillis % TimeUnit.MINUTES.toMillis(1) / TimeUnit.SECONDS.toMillis(1);

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        PlayerManager playerManager = PlayerManager.getINSTANCE();
        GuildMusicManager musicManager = playerManager.getMusicManager(ctx.getGuild());
        AudioPlayer player = musicManager.audioPlayer;

        if (player.getPlayingTrack() == null) {
            channel.sendMessage("Melody is not playing any song!").queue();
            return;
        }

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

        final AudioPlayer audioPlayer = musicManager.audioPlayer;
        AudioTrack track = audioPlayer.getPlayingTrack();

        if (track == null) {
            channel.sendMessage("Melody couldn't find any track playing!").queue();
            return;
        }

        AudioTrackInfo info = track.getInfo();


        final long position = player.getPlayingTrack().getPosition();
        final long length = info.length;
        int Position = (int) (position / (length / 8.0));

        StringBuilder bar = new StringBuilder();

        bar.append("▶️ ");
        for (int i = 0; i < 8; i++) {
            if (i == Position) {
                bar.append("\uD83D\uDD18");
                continue;
            }
            bar.append("▬");
        }
        bar.append(" `[").append(formatTime(position))
                .append("/").append(formatTime(length)).append("]` ");
        bar.append("\uD83D\uDD0A");

        /*channel.sendMessageFormat("Now playing `%s` by `%s` (Link: <%s>)\n"
                + bar,
                info.title,
                info.author,
                info.uri
        ).queue();*/

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("**Now playing**")
                .setDescription(
                       "[" + info.title + "]" + "\n" + info.uri + "\n\n" + bar
                )
                .setColor(Color.BLUE);

        channel.sendMessage(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "nowplaying";
    }

    @Override
    public String getHelp() {
        return "Melody will show the currently playing song!";
    }

    @Override
    public List<String> getAliases() {
        return List.of("np");
    }
}
