package com.nicolaszarcero.melody.command.commands;

import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;
import me.duncte123.botcommons.messaging.EmbedUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ServerInfoCommand implements ICommand {
    @Override
    public void handle(CommandContext ctx) {
        Guild guild = ctx.getGuild();

        String generalInfo = String.format(
                "**Owner**: <@%s>\n**Region**: %s\n**Creation Date**: %s",
                guild.getOwnerId(),
                guild.getRegion().getName(),
                guild.getTimeCreated().format(DateTimeFormatter.RFC_1123_DATE_TIME)
        );

        String memberInfo = String.format(
                "**Total Roles**: %s\n**Total Members**: %s\n**Online Members**: %s\n**Offline Members**: %s\n**Bot Count**: %s",
                guild.getRoles().size(),
                guild.getMemberCache().size(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.ONLINE).count(),
                guild.getMemberCache().stream().filter((m) -> m.getOnlineStatus() == OnlineStatus.OFFLINE).count(),
                guild.getMemberCache().stream().filter((m) -> m.getUser().isBot()).count()
        );

        EmbedBuilder embed = EmbedUtils.getDefaultEmbed()
                .setTitle("Server info for " + guild.getName())
                .setThumbnail(guild.getIconUrl())
                .addField("General Information", generalInfo, false)
                .addField("Role And Member Counts", memberInfo, false)
                .setColor(Color.black)
                ;

        ctx.getChannel().sendMessage(embed.build()).queue();
    }

    @Override
    public String getName() {
        return "serverinfo";
    }

    @Override
    public String getHelp() {
        return "Melody will show information about the server!";
    }

    @Override
    public List<String> getAliases() {
        return List.of("si");
    }
}
