package com.nicolaszarcero.melody.command.commands.music;

import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchResult;
import com.nicolaszarcero.lavaplayer.PlayerManager;
import com.nicolaszarcero.melody.Config;
import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import javax.annotation.Nullable;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class PlayCommand implements ICommand {
    private final YouTube youTube;

    public PlayCommand() {
        YouTube temp = null;

        try {
            temp = new YouTube.Builder(
                    GoogleNetHttpTransport.newTrustedTransport(),
                    JacksonFactory.getDefaultInstance(),
                    null
            )
                    .setApplicationName("Melody")
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
        }

        youTube = temp;
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void handle(CommandContext ctx) {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if (ctx.getArgs().isEmpty()) {
            channel.sendMessage("The correct usage of this command is: %play [youtube link]'!").queue();
            return;
        }

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

        if (!self.getVoiceState().inVoiceChannel()) {
            if (member.getVoiceState().inVoiceChannel()) {
                member.getGuild().getAudioManager().openAudioConnection(member.getVoiceState().getChannel());
            }
        }

        String input = String.join(" ", ctx.getArgs());

        if (!isUrl(input)) {
            String ytSearched = searchYoutube(input);

            if (ytSearched == null) {
                channel.sendMessage("Youtube returned no results!").queue();

                return;
            }

            input = ytSearched;
        }
        PlayerManager manager = PlayerManager.getINSTANCE();
        manager.loadAndPlay(ctx.getChannel(), input);
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Plays a song!\n" + "You can use it by typing: '%play [youtube link]'!";
    }

    private boolean isUrl(String input) {
        try {
            new URL(input);

            return true;
        }
        catch (MalformedURLException ignored) {
            return false;
        }
    }

    @Nullable
    private String searchYoutube(String input) {
        try {
            List<SearchResult> results = youTube.search()
                    .list("id, snippet")
                    .setQ(input)
                    .setMaxResults(1L)
                    .setType("video")
                    .setFields("items(id/kind,id/videoId,snippet/title,snippet/thumbnails/default/url)")
                    .setKey(Config.get("youtubeapikey"))
                    .execute()
                    .getItems();

            if (!results.isEmpty()) {
                String videoId = results.get(0).getId().getVideoId();

                return "https://www.youtube.com/watch?v=" + videoId;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public List<String> getAliases() {
        return List.of("p");
    }
}
