package dev.mayaqq.labyrinthextras.discord;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

public class Bot {
    public static JDA discordBot;
    public static void createBot() {
        if (LabyrinthExtrasConfig.CONFIG.botToken.equals("")) {
            LabyrinthExtras.LOGGER.warn("Bot token is empty, not creating bot");
            return;
        }
        try {
            JDA bot = JDABuilder.createDefault(LabyrinthExtrasConfig.CONFIG.botToken)
                    .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                    .setStatus(OnlineStatus.IDLE)
            .build();

            bot.awaitReady();
            LabyrinthExtras.LOGGER.info("Bot is ready");
            bot.addEventListener(new MessageListener(), new SlashCommands());
            bot.updateCommands().addCommands(Commands.slash("players", "Zobrazí seznam hráčů na serveru")).queue();
            discordBot = bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
