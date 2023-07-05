package dev.mayaqq.labyrinthextras.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Inject(method = "tickInVoid", at = @At("HEAD"), cancellable = true)
    private void LabyrinthExtras$tickInVoid(CallbackInfo ci) {
        LivingEntity self = (LivingEntity) (Object) this;
        self.teleport(self.getServer().getWorld(World.OVERWORLD), self.getX(), 500, self.getZ(), null, self.getYaw(), self.getPitch());
        self.addStatusEffect(new StatusEffectInstance(StatusEffects.NAUSEA, 100, 1, true, false));
        self.method_48926().playSound(null, self.getBlockPos(), SoundEvents.BLOCK_PORTAL_TRAVEL, SoundCategory.NEUTRAL, 1.0F, 1.0F);
        ci.cancel();
    }
}