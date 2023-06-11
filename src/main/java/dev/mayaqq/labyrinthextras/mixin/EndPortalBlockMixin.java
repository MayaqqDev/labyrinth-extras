package dev.mayaqq.labyrinthextras.mixin;

import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import net.minecraft.block.BlockState;
import net.minecraft.block.EndPortalBlock;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EndPortalBlock.class)
public class EndPortalBlockMixin {
    @Inject(method = "onEntityCollision", at = @At("HEAD"), cancellable = true)
    private void LabyrinthExtras$onEntityCollision(BlockState state, World world, BlockPos pos, Entity entity, CallbackInfo ci) {
        if (LabyrinthExtrasConfig.CONFIG.disableEndPortal) {
            if (entity instanceof ServerPlayerEntity && !((ServerPlayerEntity) entity).hasStatusEffect(StatusEffects.FIRE_RESISTANCE)) {
                ServerPlayerEntity player = (ServerPlayerEntity) entity;
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 100, 1));
                player.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 1, true, false));
            }
            ci.cancel();
        }
    }
}