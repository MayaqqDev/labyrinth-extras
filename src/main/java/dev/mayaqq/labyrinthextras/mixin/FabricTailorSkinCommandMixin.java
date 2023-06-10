package dev.mayaqq.labyrinthextras.mixin;

import com.mojang.authlib.properties.Property;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import net.minecraft.server.network.ServerPlayerEntity;
import org.samo_lego.fabrictailor.command.SkinCommand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Supplier;

@Mixin(SkinCommand.class)
public class FabricTailorSkinCommandMixin {
    @Inject(method = "setSkin(Lnet/minecraft/server/network/ServerPlayerEntity;Ljava/util/function/Supplier;)V", at = @At("HEAD"))
    private static void onSetSkin(ServerPlayerEntity player, Supplier<Property> skinProvider, CallbackInfo ci) {
        ServerState.getPlayerState(player).hasCustomSkin = true;
    }
}
