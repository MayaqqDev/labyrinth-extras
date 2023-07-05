package dev.mayaqq.labyrinthextras.commands;

import com.mojang.brigadier.context.CommandContext;
import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class EndCommand {
    public static int enable(CommandContext<ServerCommandSource> context) {
        LabyrinthExtrasConfig.CONFIG.disableEndPortal = false;
        LabyrinthExtrasConfig.save();
        context.getSource().sendFeedback(() -> Text.of("§6End Portal §3Enabled"), false);
        return 1;
    }
    public static int disable(CommandContext<ServerCommandSource> context) {
        LabyrinthExtrasConfig.CONFIG.disableEndPortal = true;
        LabyrinthExtrasConfig.save();
        context.getSource().sendFeedback(() -> Text.of("§6End Portal §3Disabled"), false);
        return 1;
    }
}
