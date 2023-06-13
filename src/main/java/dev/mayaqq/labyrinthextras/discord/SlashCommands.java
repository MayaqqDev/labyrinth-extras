package dev.mayaqq.labyrinthextras.discord;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.commands.LinkCommand;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import dev.mayaqq.labyrinthextras.utils.RankUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.SlashCommandInteraction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.UUID;
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
                try {
                    String code = interaction.getOption("kód").getAsString();
                    ServerPlayerEntity player = LabyrinthExtras.SERVER.getPlayerManager().getPlayer(LinkCommand.links.get(code));
                    if (Objects.equals(LinkCommand.links.get(code), player.getUuid())) {
                        LinkCommand.links.remove(code);
                        ServerState.getPlayerState(player).rank = getRank(interaction.getMember());
                        ServerState.getPlayerState(player).discordId = interaction.getUser().getId();
                        RankUtils.recheckRank(player);
                        try {
                            interaction.getGuild().modifyMemberRoles(interaction.getMember(), interaction.getGuild().getRolesByName("linked", true)).queue();
                        } catch (Exception ignored) {}
                        interaction.reply("Účet byl úspěšně propojen!").setEphemeral(true).queue();
                    } else {
                        interaction.reply("Kód není správný!").setEphemeral(true).queue();
                    }
                } catch (Exception e) {
                    interaction.reply("Kód není správný!").setEphemeral(true).queue();
                }
            }
            case "rank" -> {
                Role role = interaction.getOption("rank").getAsRole();
                Member member = interaction.getOption("hráč").getAsMember();
                UUID uuid = UUID.fromString(interaction.getOption("uuid").getAsString());
                if (interaction.getMember().hasPermission(Permission.ADMINISTRATOR)) {
                    try {
                        interaction.getGuild().modifyMemberRoles(member, role).queue();
                        interaction.reply("Hráči byla změněna role!").setEphemeral(true).queue();
                        ServerPlayerEntity player = LabyrinthExtras.SERVER.getPlayerManager().getPlayer(uuid);
                        ServerState.getPlayerState(player).manualRankTime = (long) (System.currentTimeMillis() + 2.628e+9);
                    } catch (Exception e) {
                        interaction.reply("Hráči se nepodařilo změnit roli!").setEphemeral(true).queue();
                    }
                } else {
                    interaction.reply("Nemáš oprávnění!").setEphemeral(true).queue();
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
