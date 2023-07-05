package dev.mayaqq.labyrinthextras.commands;

import com.mojang.brigadier.context.CommandContext;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

public class LinkCommand {
    public static HashMap<String, UUID> links = new HashMap<>();
    public static int run(CommandContext<ServerCommandSource> context) {
        // generate a random 10 character string
        ServerPlayerEntity player = context.getSource().getPlayer();
        String link = generateRandomString(6);
        links.put(link, player.getUuid());
        player.sendMessage(Text.literal("Tvůj link kód je: ").formatted(Formatting.GOLD).append(Text.literal(link).styled(style -> style
                        .withColor(Formatting.GREEN).withBold(true)
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, Text.of("Klikni pro zkopírování kódu!")))
                        .withClickEvent(new ClickEvent(ClickEvent.Action.COPY_TO_CLIPBOARD, link))
                )).append(Text.literal(". Napiš /link <kód> v #server-chat v discordu!")).formatted(Formatting.GOLD)
                , false);

        return 0;
    }
    public static String generateRandomString(int length) {
        String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(length);
        Random random = new Random();

        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(characters.length());
            char randomChar = characters.charAt(randomIndex);
            sb.append(randomChar);
        }

        return sb.toString();
    }
}
