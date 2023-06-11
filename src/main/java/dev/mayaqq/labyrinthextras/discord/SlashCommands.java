package dev.mayaqq.labyrinthextras.discord;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.commands.LinkCommand;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class SlashCommands extends ListenerAdapter {
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        SlashCommandInteraction interaction = event.getInteraction();
        switch (event.getName()) {
            case "players" -> {
                if (LabyrinthExtras.SERVER.getPlayerManager().getPlayerList().size() > 0) {
                    StringBuilder players = new StringBuilder();
                    LabyrinthExtras.SERVER.getPlayerManager().getPlayerList().forEach(player -> players.append(player.getName().getString()).append("\n"));
                    players.deleteCharAt(players.length() - 1);
                    players.append("```");
                    interaction.reply("Hráčí: **" + LabyrinthExtras.SERVER.getPlayerManager().getPlayerList().size() + "**```\n" + players).setEphemeral(true).queue();
                } else {
                    interaction.reply("Žádní hráči nejsou online!").setEphemeral(true).queue();
                }
            }
            case "link" -> {
                String code = interaction.getOption("kód").getAsString();
                String nick = interaction.getOption("nick").getAsString();
                ServerPlayerEntity player = LabyrinthExtras.SERVER.getPlayerManager().getPlayer(nick);
                if (Objects.equals(LinkCommand.links.get(player), code)) {
                    LinkCommand.links.remove(LabyrinthExtras.SERVER.getPlayerManager().getPlayer(nick));
                    ServerState.getPlayerState(player).rank = getRank(interaction.getMember());
                    ServerState.getPlayerState(player).discordId = interaction.getUser().getId();
                    try {
                        interaction.getGuild().modifyMemberRoles(interaction.getMember(), interaction.getGuild().getRolesByName("linked", true)).queue();
                    } catch (Exception ignored) {}
                    interaction.reply("Účet byl úspěšně propojen!").setEphemeral(true).queue();
                } else {
                    interaction.reply("Kód není správný!").setEphemeral(true).queue();
                }
            }
        }
    }
    public static String getRank(Member member) {
        AtomicReference<String> name = new AtomicReference<>("clovek");
        member.getRoles().forEach(role -> {
            String roleName = role.getName();
            switch (roleName) {
                case "Čaroděj" -> name.set("carodej");
                case "Trpaslík" -> name.set("trpaslik");
                case "Elf" -> name.set("elf");
                case "Skřet" -> name.set("skret");
                case "Hobit" -> name.set("hobit");
            }
        });
        return name.get();
    }
}
