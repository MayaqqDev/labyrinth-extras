package dev.mayaqq.labyrinthextras;

import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import dev.mayaqq.labyrinthextras.discord.Bot;
import dev.mayaqq.labyrinthextras.registry.CommandRegistry;
import dev.mayaqq.labyrinthextras.registry.EventRegistry;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.entrypoint.PreLaunchEntrypoint;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabyrinthExtras implements ModInitializer, PreLaunchEntrypoint {
    public static final Logger LOGGER = LoggerFactory.getLogger("LabyrinthExtras");
    public static MinecraftServer SERVER;
    @Override
    public void onInitialize() {
        LOGGER.info("LabyrinthExtras is initializing...");
        EventRegistry.register();
        CommandRegistry.register();
    }

    @Override
    public void onPreLaunch() {
        LabyrinthExtrasConfig.load();
        LOGGER.info("Bot is starting...");
        Bot.createBot();
    }
}
