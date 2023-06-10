package dev.mayaqq.labyrinthextras.utils;

import com.mojang.authlib.properties.Property;
import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import net.minecraft.server.network.ServerPlayerEntity;
import org.samo_lego.fabrictailor.FabricTailor;
import org.samo_lego.fabrictailor.casts.TailoredPlayer;

import java.util.function.Supplier;

public class FabricTaylorUtils {
    public static void setSkin(ServerPlayerEntity player, Supplier<Property> skinProvider) {
        long lastChange = ((TailoredPlayer) player).getLastSkinChange();
        long now = System.currentTimeMillis();
        if (now - lastChange <= FabricTailor.config.skinChangeTimer * 1000L && lastChange != 0L) {
            LabyrinthExtras.LOGGER.warn("Player {} tried to change skin too fast!", player.getName().getString());
        } else {
            FabricTailor.THREADPOOL.submit(() -> {
                Property skinData = skinProvider.get();
                if (skinData == null) {
                    LabyrinthExtras.LOGGER.error("Failed to fetch skin data for {}", player.getName().getString());
                } else {
                    ((TailoredPlayer)player).setSkin(skinData, true);
                }
            });
        }
    }
}