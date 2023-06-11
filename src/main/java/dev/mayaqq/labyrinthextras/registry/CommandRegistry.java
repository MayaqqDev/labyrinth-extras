package dev.mayaqq.labyrinthextras.registry;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.mayaqq.labyrinthextras.commands.KitsCommand;
import dev.mayaqq.labyrinthextras.commands.LinkCommand;
import dev.mayaqq.labyrinthextras.commands.PvpCommand;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {

            LiteralCommandNode<ServerCommandSource> pvpNode = CommandManager.literal("pvp").requires(Permissions.require("labyrinthextras.command.pvp")).executes(PvpCommand::toggle).build();
            LiteralCommandNode<ServerCommandSource> pvpOnNode = CommandManager.literal("on").requires(Permissions.require("labyrinthextras.command.pvp")).executes(PvpCommand::toggleOn).build();
            LiteralCommandNode<ServerCommandSource> pvpOffNode = CommandManager.literal("off").requires(Permissions.require("labyrinthextras.command.pvp")).executes(PvpCommand::toggleOff).build();
            LiteralCommandNode<ServerCommandSource> pvpAllOnNode = CommandManager.literal("all").requires(Permissions.require("labyrinthextras.command.pvp.admin")).executes(PvpCommand::toggleAllOn).build();
            LiteralCommandNode<ServerCommandSource> pvpAllOffNode = CommandManager.literal("all").requires(Permissions.require("labyrinthextras.command.pvp.admin")).executes(PvpCommand::toggleAllOff).build();

            LiteralCommandNode<ServerCommandSource> kitsNode = CommandManager.literal("kits").executes(KitsCommand::run).build();

            LiteralCommandNode<ServerCommandSource> linkNode = CommandManager.literal("link").executes(LinkCommand::run).build();

            // Add commands to root
            RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();

            // root commands
            LiteralCommandNode[] nodes = new LiteralCommandNode[]{
                    pvpNode, kitsNode, linkNode
            };
            for (LiteralCommandNode node : nodes) {
                root.addChild(node);
            }
            // apended commands
            pvpNode.addChild(pvpOnNode);
                pvpOnNode.addChild(pvpAllOnNode);
            pvpNode.addChild(pvpOffNode);
                pvpOffNode.addChild(pvpAllOffNode);
        });
    }
}
