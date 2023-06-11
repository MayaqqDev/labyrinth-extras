package dev.mayaqq.labyrinthextras.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.labyrinthextras.gui.KitsGui;
import net.minecraft.server.command.ServerCommandSource;

public class KitsCommand {
    public static int run(CommandContext<ServerCommandSource> context) {
        // TODO: For some reason, this doesn't work and throws an error when the command is run.
        KitsGui.gui(context.getSource().getPlayer());
        return 1;
    }
}
