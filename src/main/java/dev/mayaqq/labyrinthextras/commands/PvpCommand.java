package dev.mayaqq.labyrinthextras.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

public class PvpCommand {
    public static int toggle(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        boolean pvpState = ServerState.getPlayerState(player).hasPvpEnabled;
        if (pvpState) {
            toggleOff(context);
            return 0;
        } else {
            toggleOn(context);
            return 1;
        }
    }
    public static int toggleOn(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        boolean pvpState = ServerState.getPlayerState(player).hasPvpEnabled;
        if (!pvpState) {
            player.sendMessage(Text.of("§6PvP bylo zapnuto!"), true);
            ServerState.getPlayerState(player).hasPvpEnabled = true;
            return 0;
        } else {
            player.sendMessage(Text.of("§6PvP je už zaplé!"), true);
            return 1;
        }
    }
    public static int toggleOff(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        boolean pvpState = ServerState.getPlayerState(player).hasPvpEnabled;
        if (pvpState) {
            player.sendMessage(Text.of("§6PvP bylo vypnuto!"), true);
            ServerState.getPlayerState(player).hasPvpEnabled = false;
            return 0;
        } else {
            player.sendMessage(Text.of("§6PvP je už vypnuté!"), true);
            return 1;
        }
    }
    public static int toggleAllOn(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        LabyrinthExtrasConfig.CONFIG.players.forEach((uuid, bool) -> {
            ServerState.getPlayerState(player.getServer().getPlayerManager().getPlayer(uuid)).hasPvpEnabled = true;
        });
        player.sendMessage(Text.of("§6PvP bylo zapnuto pro všechny!"), true);
        return 0;
    }
    public static int toggleAllOff(CommandContext<ServerCommandSource> context) {
        ServerPlayerEntity player = context.getSource().getPlayer();
        LabyrinthExtrasConfig.CONFIG.players.forEach((uuid, bool) -> {
            ServerState.getPlayerState(player.getServer().getPlayerManager().getPlayer(uuid)).hasPvpEnabled = false;
        });
        player.sendMessage(Text.of("§6PvP bylo vypnuto pro všechny!"), true);
        return 0;
    }
}
