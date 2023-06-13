package dev.mayaqq.labyrinthextras.storage;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateManager;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.UUID;

public class ServerState extends PersistentState {

    public HashMap<UUID, PlayerState> players = new HashMap<>();

    @Override
    public NbtCompound writeNbt(NbtCompound nbt) {
        // Putting the 'players' hashmap, into the 'nbt' which will be saved.
        NbtCompound playersNbtCompound = new NbtCompound();
        players.forEach((UUID, playerSate) -> {
            NbtCompound playerStateNbt = new NbtCompound();

            // ANYTIME YOU PUT NEW DATA IN THE PlayerState CLASS YOU NEED TO REFLECT THAT HERE!!!
            playerStateNbt.putBoolean("hasCustomSkin", playerSate.hasCustomSkin);
            playerStateNbt.putBoolean("hasPvpEnabled", playerSate.hasPvpEnabled);
            playerStateNbt.putString("rank", playerSate.rank);
            playerStateNbt.putString("discordId", playerSate.discordId);
            playerStateNbt.putLong("manualRankTime", playerSate.manualRankTime);

            NbtCompound kitsNbtCompound = new NbtCompound();
            playerSate.collectedKits.forEach(kitsNbtCompound::putBoolean);
            playerStateNbt.put("collectedKits", kitsNbtCompound);

            playersNbtCompound.put(String.valueOf(UUID), playerStateNbt);
        });
        nbt.put("players", playersNbtCompound);
        return nbt;
    }
    public static ServerState createFromNbt(NbtCompound tag) {
        ServerState serverState = new ServerState();
        NbtCompound playersTag = tag.getCompound("players");
        playersTag.getKeys().forEach(key -> {
            PlayerState playerState = new PlayerState();

            playerState.hasCustomSkin = playersTag.getCompound(key).getBoolean(" hasCustomSkin");
            playerState.hasPvpEnabled = playersTag.getCompound(key).getBoolean("hasPvpEnabled");
            playerState.rank = playersTag.getCompound(key).getString("rank");
            playerState.discordId = playersTag.getCompound(key).getString("discordId");
            playerState.manualRankTime = playersTag.getCompound(key).getLong("manualRankTime");

            NbtCompound kitsTag = playersTag.getCompound(key).getCompound("collectedKits");
            kitsTag.getKeys().forEach(kitKey -> {
                playerState.collectedKits.put(kitKey, kitsTag.getBoolean(kitKey));
            });

            UUID uuid = UUID.fromString(key);
            serverState.players.put(uuid, playerState);
        });
        return serverState;
    }

    public static ServerState getServerState(MinecraftServer server) {
        // First we get the persistentStateManager for the OVERWORLD
        PersistentStateManager persistentStateManager = server.getWorld(World.OVERWORLD).getPersistentStateManager();

        // Calling this reads the file from the disk if it exists, or creates a new one and saves it to the disk
        // You need to use a unique string as the key. You should already have a MODID variable defined by you somewhere in your code. Use that.
        ServerState serverState = persistentStateManager.getOrCreate(ServerState::createFromNbt, ServerState::new, "labyrinthextras");

        serverState.markDirty(); // makes stuff work

        return serverState;
    }

    @Override
    public boolean isDirty() {
        return true;
    }

    public static PlayerState getPlayerState(LivingEntity player) {
        ServerState serverState = getServerState(player.getServer());
        serverState.markDirty();
        return serverState.players.computeIfAbsent(player.getUuid(), uuid -> new PlayerState());
    }
    public static class PlayerState {
        public boolean hasCustomSkin = false;
        public boolean hasPvpEnabled = true;
        public HashMap<String, Boolean> collectedKits = new HashMap<>();
        public String rank = "clovek";
        public String discordId = "";
        public Long manualRankTime = 0L;
    }
}
