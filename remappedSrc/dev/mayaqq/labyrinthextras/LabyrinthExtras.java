package dev.mayaqq.labyrinthextras;

import dev.mayaqq.labyrinthextras.registry.CommandRegistry;
import dev.mayaqq.labyrinthextras.registry.EventRegistry;
import net.fabricmc.api.ModInitializer;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LabyrinthExtras implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("LabyrinthExtras");
    public static MinecraftServer SERVER;
    @Override
    public void onInitialize() {
        LOGGER.info("LabyrinthExtras is initializing...");
        EventRegistry.register();
        CommandRegistry.register();
    }
}
