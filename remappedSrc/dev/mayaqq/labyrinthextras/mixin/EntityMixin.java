package dev.mayaqq.labyrinthextras.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public class EntityMixin {
    @Inject(method = "tickInVoid", at = @At("HEAD"), cancellable = true)
    private void LabyrinthExtras$tickInVoid(CallbackInfo ci) {
        Entity self = (Entity) (Object) this;
        self.teleport(self.getServer().getWorld(World.OVERWORLD), self.getX(), 500, self.getZ(), null, self.getYaw(), self.getPitch());
        ci.cancel();
    }
}