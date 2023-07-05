package dev.mayaqq.labyrinthextras.registry;

import com.mojang.brigadier.tree.LiteralCommandNode;
import com.mojang.brigadier.tree.RootCommandNode;
import dev.mayaqq.labyrinthextras.commands.EndCommand;
import dev.mayaqq.labyrinthextras.commands.KitsCommand;
import dev.mayaqq.labyrinthextras.commands.LinkCommand;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;

public class CommandRegistry {
    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated, environment) -> {

            LiteralCommandNode<ServerCommandSource> kitsNode = CommandManager.literal("kits").executes(KitsCommand::run).build();

            LiteralCommandNode<ServerCommandSource> linkNode = CommandManager.literal("link").executes(LinkCommand::run).build();

            LiteralCommandNode<ServerCommandSource> endNode = CommandManager.literal("end").requires(Permissions.require("labyrinthextras.command.end")).build();

            LiteralCommandNode<ServerCommandSource> endEnableNode = CommandManager.literal("enable").executes(EndCommand::enable).build();
            LiteralCommandNode<ServerCommandSource> endDisableNode = CommandManager.literal("disable").executes(EndCommand::disable).build();

            // Add commands to root
            RootCommandNode<ServerCommandSource> root = dispatcher.getRoot();

            // root commands
            LiteralCommandNode[] nodes = new LiteralCommandNode[]{
                    endNode, kitsNode, linkNode
            };
            for (LiteralCommandNode node : nodes) {
                root.addChild(node);
            }
            // apended commands
            endNode.addChild(endEnableNode);
            endNode.addChild(endDisableNode);
        });
    }
}
