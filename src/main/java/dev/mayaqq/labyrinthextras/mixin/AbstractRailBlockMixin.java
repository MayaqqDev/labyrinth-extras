package dev.mayaqq.labyrinthextras.mixin;

import dev.mayaqq.labyrinthextras.entities.TempMinecartEntity;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import net.minecraft.block.AbstractRailBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(AbstractRailBlock.class)
public class AbstractRailBlockMixin extends Block {
    public AbstractRailBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!world.isClient() && player.isSneaking() && player.getStackInHand(hand).isEmpty()) {
            if (ServerState.getPlayerState(player).hasMinecart) {
                return ActionResult.PASS;
            } else {
                TempMinecartEntity minecart = new TempMinecartEntity(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, player);
                world.spawnEntity(minecart);
                return ActionResult.SUCCESS;
            }
        } else {
            return ActionResult.PASS;
        }
    }
}
