package dev.mayaqq.labyrinthextras.discord;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;

import java.util.EnumSet;

public class Bot {
    public static JDA discordBot;
    public static void createBot() {
        if (LabyrinthExtrasConfig.CONFIG.botToken.equals("")) {
            LabyrinthExtras.LOGGER.warn("Bot token is empty, not creating bot");
            return;
        }
        try {
            JDA bot = JDABuilder.createDefault(LabyrinthExtrasConfig.CONFIG.botToken, EnumSet.allOf(GatewayIntent.class))
                    .disableCache(CacheFlag.VOICE_STATE, CacheFlag.EMOJI, CacheFlag.STICKER, CacheFlag.SCHEDULED_EVENTS)
                    .setStatus(OnlineStatus.IDLE)
            .build();

            bot.awaitReady();
            LabyrinthExtras.LOGGER.info("Bot is ready");
            bot.addEventListener(new MessageListener(), new SlashCommands());
            bot.updateCommands()
                    .addCommands(Commands.slash("players", "Zobrazí seznam hráčů na serveru"))
                    .addCommands(Commands.slash("link", "Propojí discord účet s mc účtem, udělej /link ve hře a pak sem napiš /link <kód> ze hry!")
                            .addOption(OptionType.STRING, "kód", "Kód ze hry", true)
                    )
                    .addCommands(Commands.slash("rank", "Dej někomu rank!")
                            .setDefaultPermissions(DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR))
                            .addOption(OptionType.USER, "hráč", "Hráč, kterému chceš dát rank", true)
                            .addOption(OptionType.ROLE, "rank", "Rank, který chceš dát", true)
                            .addOption(OptionType.STRING, "uuid", "UUID hráče", true)
                    )
                .queue();
            discordBot = bot;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}