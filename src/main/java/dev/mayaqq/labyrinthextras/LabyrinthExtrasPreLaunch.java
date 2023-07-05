package dev.mayaqq.labyrinthextras;

import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import dev.mayaqq.labyrinthextras.discord.Bot;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;

public class LabyrinthExtrasPreLaunch implements PreLaunchEntrypoint {
    @Override
    public void onPreLaunch() {
        LabyrinthExtrasConfig.load();
        LabyrinthExtras.LOGGER.info("Bot is starting...");
        Bot.createBot();
    }
}
