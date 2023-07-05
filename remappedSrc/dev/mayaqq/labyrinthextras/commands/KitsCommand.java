package dev.mayaqq.labyrinthextras.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.labyrinthextras.gui.KitsGui;
import net.minecraft.server.command.ServerCommandSource;

public class KitsCommand {
    public static int run(CommandContext<ServerCommandSource> context) {
        KitsGui.gui(context.getSource().getPlayer());
        return 1;
    }
}
