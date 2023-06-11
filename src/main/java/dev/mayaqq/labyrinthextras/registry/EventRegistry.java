package dev.mayaqq.labyrinthextras.registry;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import dev.mayaqq.labyrinthextras.discord.Bot;
import dev.mayaqq.labyrinthextras.discord.SlashCommands;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import dev.mayaqq.labyrinthextras.utils.FabricTaylorUtils;
import me.lucko.fabric.api.permissions.v0.PermissionCheckEvent;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.fabricmc.fabric.api.util.TriState;
import net.minecraft.server.network.ServerPlayerEntity;
import org.samo_lego.fabrictailor.util.SkinFetcher;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class EventRegistry {
    public static void register() {
        PermissionCheckEvent.EVENT.register((source, permission) -> {
            if (source.hasPermissionLevel(4)) {
                return TriState.TRUE;
            }
            AtomicReference<TriState> state = new AtomicReference<>(TriState.DEFAULT);
            source.getPlayerNames().forEach(name -> {
                ServerPlayerEntity player = LabyrinthExtras.SERVER.getPlayerManager().getPlayer(name);
                if (ServerState.getPlayerState(player).rank == null) return;
                switch (ServerState.getPlayerState(player).rank) {
                    case "carodej": {
                        if (permission.equals("labyrinthextras.rank.carodej")) {
                            state.set(TriState.TRUE);
                        }
                    }
                    case "trpaslik": {
                        if (permission.equals("labyrinthextras.rank.trpaslik")) {
                            state.set(TriState.TRUE);
                        }
                    }
                    case "elf": {
                        if (permission.equals("labyrinthextras.rank.elf")) {
                            state.set(TriState.TRUE);
                        }
                    }
                    case "skret": {
                        if (permission.equals("labyrinthextras.rank.skret")) {
                            state.set(TriState.TRUE);
                        }
                    }
                    case "hobit": {
                        if (permission.equals("labyrinthextras.rank.hobit")) {
                            state.set(TriState.TRUE);
                        }
                    }
                }
            });
            return state.get();
        });

        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            LabyrinthExtrasConfig.CONFIG.players.putIfAbsent(player.getUuid(), true);
            LabyrinthExtrasConfig.save();


            // set the skin of the player to their name
            String name = player.getName().getString();
            if (name.startsWith(".") && ServerState.getPlayerState(player).hasCustomSkin) return;
            FabricTaylorUtils.setSkin(player, () -> SkinFetcher.fetchSkinByName(name));
            if (Bot.discordBot == null) return;
            Bot.discordBot.getPresence();
            try {
                if (!Objects.equals(ServerState.getPlayerState(player).discordId, "") && !Objects.equals(ServerState.getPlayerState(player).discordId, null)) {
                    AtomicReference<Member> member = new AtomicReference<>();
                    Bot.discordBot.getGuilds().forEach(guild -> member.set(guild.getMemberById(ServerState.getPlayerState(player).discordId)));
                    ServerState.getPlayerState(player).rank = SlashCommands.getRank(member.get());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("Připojil se: **" + player.getDisplayName().getString() + "**!").queue();
            });
            Bot.discordBot.getPresence().setActivity(Activity.playing("S " + (server.getPlayerManager().getPlayerList().size() + 1) + "/" + server.getPlayerManager().getMaxPlayerCount() + " hráči!"));
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            if (Bot.discordBot == null) return;
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("Odpojil se: **" + player.getDisplayName().getString() + "**!").queue();
            });
            Bot.discordBot.getPresence().setActivity(Activity.playing("S " + (server.getPlayerManager().getPlayerList().size() + 1) + "/" + server.getPlayerManager().getMaxPlayerCount() + " hráči!"));
        });
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            if (Bot.discordBot == null) return;
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("**" + sender.getDisplayName().getString() + "**: " + message.getContent().getString()).queue();
            });
        });
        // bot stuff
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            LabyrinthExtras.SERVER = server;
            if (Bot.discordBot == null) return;
            Bot.discordBot.getPresence().setStatus(OnlineStatus.ONLINE);
            Bot.discordBot.getGuilds().forEach(guild -> {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Server je zapnutý!");
                eb.setDescription("Můžeš se připojit na server!");
                eb.setColor(Color.GREEN);
                eb.setImage("https://images-ext-2.discordapp.net/external/WWF21ZalOWLd7B4ZDKQFMyTwH3xyk7SmpowVl5FJ_x4/%3Fsize%3D2048/https/cdn.discordapp.com/icons/1011009145891201054/8fb601dd7946151042cac9eed5fa4556.png?width=778&height=778");
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessageEmbeds(eb.build()).queue();
            });
        });
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            if (Bot.discordBot == null) return;
            Bot.discordBot.getPresence().setStatus(OnlineStatus.IDLE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            if (Bot.discordBot == null) return;
            try {
                Bot.discordBot.shutdownNow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (Bot.discordBot == null) return;
            try {
                Bot.discordBot.getPresence().setStatus(OnlineStatus.OFFLINE);
                // TODO: fix this, right now it breaks server shutdown
                /*
                Bot.discordBot.getGuilds().forEach(guild -> {
                    EmbedBuilder eb = new EmbedBuilder();
                    eb.setTitle("Server se vypíná!");
                    eb.setDescription("Počkej na to až se znovu zapne!");
                    eb.setColor(Color.RED);
                    eb.setImage("https://images-ext-2.discordapp.net/external/WWF21ZalOWLd7B4ZDKQFMyTwH3xyk7SmpowVl5FJ_x4/%3Fsize%3D2048/https/cdn.discordapp.com/icons/1011009145891201054/8fb601dd7946151042cac9eed5fa4556.png?width=778&height=778");
                    guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessageEmbeds(eb.build()).queue();
                });

                 */
            } catch (Exception e) {
                e.printStackTrace();
            }
            Bot.discordBot.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        });
    }
}
