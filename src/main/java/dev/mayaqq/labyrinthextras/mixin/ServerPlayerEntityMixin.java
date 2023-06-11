package dev.mayaqq.labyrinthextras.mixin;

import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import dev.mayaqq.labyrinthextras.discord.Bot;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ServerPlayerEntity.class)
public class ServerPlayerEntityMixin {
    @Inject(method = "shouldDamagePlayer", at = @At("HEAD"), cancellable = true)
    private void shouldDamagePlayer(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (!ServerState.getPlayerState(player).hasPvpEnabled) {
            cir.setReturnValue(false);
        }
    }
    @Inject(method = "onDeath", at = @At("HEAD"), cancellable = true)
    private void onDeath(DamageSource damageSource, CallbackInfo ci) {
        ServerPlayerEntity player = (ServerPlayerEntity) (Object) this;
        if (Bot.discordBot == null) return;
        Bot.discordBot.getGuilds().forEach(guild -> {
            guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("\uD83D\uDC80 " + damageSource.getDeathMessage(player).getString()).queue();
        });
    }
}
