package com.nicolaszarcero.melody.command.commands;

import com.nicolaszarcero.melody.CommandManager;
import com.nicolaszarcero.melody.Config;
import com.nicolaszarcero.melody.command.CommandContext;
import com.nicolaszarcero.melody.command.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public class HelpCommands implements ICommand {
    private final CommandManager manager;

    public HelpCommands(CommandManager manager) {
        this.manager = manager;
    }

    @Override
    public void handle(CommandContext ctx) {

        if (ctx.getArgs().isEmpty()) {
            generateAndSendEmbed(ctx.getEvent());
            return;
        }

        String joined = String.join("", ctx.getArgs());

        ICommand command = manager.getCommand(joined);

            if (command == null) {
                ctx.getEvent().getChannel().sendMessage("Melody couldn't find the command" + joined + "!\n" +
                        "Use `" + Config.get("prefix") + getName() + "` for a list of commands!").queue();
                return;
            }

        String message = "Command help for `" + command.getName() + "`\n" + command.getHelp();

        ctx.getEvent().getChannel().sendMessage(message).queue();

    }

    private void generateAndSendEmbed(GuildMessageReceivedEvent event) {

        EmbedBuilder builder = new EmbedBuilder()
                .setTitle("**__List of Melody's commands!__**")
                .setDescription("*Use '%help [command]' for more info about every command!*\n")
                .setColor(Color.cyan)
                .setThumbnail("https://static.wikia.nocookie.net/minecraft/images/e/e5/JukeBoxNew.png/revision/latest?cb=20190914170924")
                .setAuthor("Z    e    t    a#6995")
                .setFooter("Melody")
                ;

        StringBuilder descriptionBuilder = builder.getDescriptionBuilder();

        manager.getCommands().forEach(
                (command) -> descriptionBuilder.append('`').append(command.getName()).append("`\n")
                );

        event.getChannel().sendMessage(builder.build()).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getHelp() {
        return "Shows the list of commands Melody has!\n" +
                "You can use it by typing: '%help [command]' or just '%help' to show all of them!";
    }

    @Override
    public List<String> getAliases() {
        return List.of("commands","cmds","cmd","ayuda","command");
    }
}
