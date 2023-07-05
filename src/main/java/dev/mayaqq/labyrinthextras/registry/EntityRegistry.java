package dev.mayaqq.labyrinthextras.registry;

import dev.mayaqq.labyrinthextras.entities.TempMinecartEntity;
import eu.pb4.polymer.core.api.entity.PolymerEntityUtils;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;

public class EntityRegistry {
    public static final EntityType<TempMinecartEntity> TEMP_MINECART = Registry.register(Registries.ENTITY_TYPE, "labyrinthextras:temp_minecart", FabricEntityTypeBuilder.create(SpawnGroup.MISC, new EntityType.EntityFactory<TempMinecartEntity>() {
        @Override
        public TempMinecartEntity create(EntityType<TempMinecartEntity> type, net.minecraft.world.World world) {
            return new TempMinecartEntity(type, world);
        }
    }).build());
    public static void register() {
        PolymerEntityUtils.registerType(TEMP_MINECART);
    }
}