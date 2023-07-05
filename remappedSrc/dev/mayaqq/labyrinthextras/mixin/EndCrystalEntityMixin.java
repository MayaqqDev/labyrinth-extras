package dev.mayaqq.labyrinthextras.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EndCrystalEntity.class)
public class EndCrystalEntityMixin {
    @Inject(method = "damage", at = @At("HEAD"), cancellable = true)
    private void labyrinthextras$onDamage(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        EndCrystalEntity self = (EndCrystalEntity) (Object) this;
        if (self.method_48926().getRegistryKey() != World.END) {
            self.remove(Entity.RemovalReason.KILLED);
            cir.setReturnValue(true);
        }
    }
}
