package dev.mayaqq.labyrinthextras.entities;

import dev.mayaqq.labyrinthextras.registry.EntityRegistry;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import eu.pb4.polymer.core.api.entity.PolymerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Ownable;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.AbstractMinecartEntity;
import net.minecraft.entity.vehicle.MinecartEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class TempMinecartEntity extends MinecartEntity implements PolymerEntity, Ownable {
    public boolean hasBeenRidden = false;
    public PlayerEntity owner;
    public TempMinecartEntity(EntityType<?> entityType, World world) {
        super(entityType, world);
    }

    public TempMinecartEntity(World world, double x, double y, double z, PlayerEntity owner) {
        super(world, x, y, z);
        this.owner = owner;
        owner.startRiding(this);
        ServerState.getPlayerState(this.owner).hasMinecart = true;
    }

    @Override
    public Entity getOwner() {
        return this.owner;
    }

    @Override
    protected Item getItem() {
        return Items.AIR;
    }

    public ActionResult interact(PlayerEntity player, Hand hand) {
        if (player.shouldCancelInteraction()) {
            return ActionResult.PASS;
        } else if (this.hasPassengers()) {
            return ActionResult.PASS;
        } else if (player != this.owner) {
            return ActionResult.PASS;
        }
        else if (!this.getWorld().isClient) {
            hasBeenRidden = true;
            return player.startRiding(this) ? ActionResult.CONSUME : ActionResult.PASS;
        } else {
            return ActionResult.SUCCESS;
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.hasPassengers() && hasBeenRidden) {
            this.remove(RemovalReason.DISCARDED);
        }
    }

    @Override
    public void onActivatorRail(int x, int y, int z, boolean powered) {
    if (powered) {
        if (this.hasPassengers()) {
            this.removeAllPassengers();
        }

        if (this.getDamageWobbleTicks() == 0) {
            this.setDamageWobbleSide(-this.getDamageWobbleSide());
            this.setDamageWobbleTicks(10);
            this.setDamageWobbleStrength(50.0F);
            this.scheduleVelocityUpdate();
        }
    }

    }

    @Override
    public AbstractMinecartEntity.Type getMinecartType() {
        return Type.RIDEABLE;
    }

    @Override
    public EntityType<?> getPolymerEntityType(ServerPlayerEntity player) {
        return EntityType.MINECART;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        this.remove(RemovalReason.DISCARDED);
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        ServerState.getPlayerState(this.owner).hasMinecart = false;
        super.remove(reason);
    }
}
