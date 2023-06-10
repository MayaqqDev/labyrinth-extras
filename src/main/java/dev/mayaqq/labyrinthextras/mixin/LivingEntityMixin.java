package dev.mayaqq.labyrinthextras.mixin;

import net.minecraft.entity.LivingEntity;
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
        ci.cancel();
    }
}