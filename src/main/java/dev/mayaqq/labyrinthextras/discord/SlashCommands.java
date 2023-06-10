package dev.mayaqq.labyrinthextras.discord;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import org.jetbrains.annotations.NotNull;

public class SlashCommands extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        switch (event.getName()) {
            case "players" -> {
                SlashCommandInteraction interaction = event.getInteraction();
                StringBuilder players = new StringBuilder();
                LabyrinthExtras.SERVER.getPlayerManager().getPlayerList().forEach(player -> players.append(player.getName().getString()).append("\n"));
                players.deleteCharAt(players.length() - 1);
                players.append("```");
                interaction.reply("Players: **" + LabyrinthExtras.SERVER.getPlayerManager().getPlayerList().size() + "**```\n" + players).setEphemeral(true).queue();
            }
        }
    }
}
