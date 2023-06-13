package dev.mayaqq.labyrinthextras.utils;

import dev.mayaqq.labyrinthextras.storage.ServerState;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.minecraft.server.network.ServerPlayerEntity;

public class RankUtils {
    public static void recheckRank(ServerPlayerEntity player) {
        ServerState.PlayerState state = ServerState.getPlayerState(player);
        if (state.rank != null || state.rank.equals("clovek")) {
            LuckPerms api = LuckPermsProvider.get();
            User user = api.getPlayerAdapter(ServerPlayerEntity.class).getUser(player);
            for (Node node : user.data().toCollection()) {
                String[] ranks = {"carodej", "trpaslik", "elf", "skret", "hobit", "default"};
                for (String rank : ranks) {
                    if (node.getKey().equals("group." + rank)) {
                        user.data().remove(node);
                    }
                }
            }
            if (!state.rank.equals("clovek")) {
                user.data().add(Node.builder("group." + state.rank).build());
            } else {
                user.data().add(Node.builder("group.default").build());
            }
            api.getUserManager().saveUser(user);
        }
    }
}