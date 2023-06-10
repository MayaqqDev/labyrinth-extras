package dev.mayaqq.labyrinthextras.discord;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.jetbrains.annotations.NotNull;

public class MessageListener extends ListenerAdapter {
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getChannel().getId().equals(LabyrinthExtrasConfig.CONFIG.botChannel)) {
            LabyrinthExtras.SERVER.getPlayerManager().getPlayerList().forEach(player -> {
                player.sendMessage(
                        Text.literal("[discord] ")
                                .styled(style -> style
                                        .withColor(Formatting.DARK_BLUE)
                                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.labyrinthmc.world"))
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("§9Klikni pro připojení na Discord!")))
                                )
                            .append(event.getAuthor().getName()).styled(style -> style
                                        .withColor(Formatting.BLUE)
                                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("§9@" + event.getAuthor().getGlobalName())))
                                )
                            .append(": " + event.getMessage().getContentDisplay()).formatted(Formatting.WHITE)
                );
            });
        }
    }
}
