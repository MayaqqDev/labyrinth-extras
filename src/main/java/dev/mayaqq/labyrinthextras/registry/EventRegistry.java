package dev.mayaqq.labyrinthextras.registry;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import dev.mayaqq.labyrinthextras.discord.Bot;
import dev.mayaqq.labyrinthextras.discord.SlashCommands;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import dev.mayaqq.labyrinthextras.utils.FabricTaylorUtils;
import dev.mayaqq.labyrinthextras.utils.RankUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.samo_lego.fabrictailor.util.SkinFetcher;

import java.awt.*;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class EventRegistry {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            LabyrinthExtrasConfig.CONFIG.players.putIfAbsent(player.getUuid(), true);
            LabyrinthExtrasConfig.save();

            // for when the player got the rank assigned by the /rank command in discord
            try {
                ServerState.PlayerState state = ServerState.getPlayerState(player);
                Long time = state.manualRankTime;
                if (time != 0) {
                    if (System.currentTimeMillis() > time) {
                        Bot.discordBot.getGuilds().forEach(guild -> {
                            Member member = guild.getMemberById(state.discordId);
                            String[] roleNames = {"Čaroděj", "Trpaslík", "Elf", "Skřet", "Hobit"};
                            member.getRoles().forEach(role -> {
                                for (String roleName : roleNames) {
                                    if (role.getName().equals(roleName)) {
                                        guild.removeRoleFromMember(member, role).queue();
                                    }
                                }
                            });
                        });

                        state.rank = "clovek";
                        state.manualRankTime = 0L;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            // set the skin of the player to their name
            String name = player.getName().getString();
            if (!(name.startsWith(".") || ServerState.getPlayerState(player).hasCustomSkin)) {
                FabricTaylorUtils.setSkin(player, () -> SkinFetcher.fetchSkinByName(name));
            }
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
            RankUtils.recheckRank(player);
            // send message to discord that the player joined
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("Připojil/a/o se: **" + player.getDisplayName().getString() + "**!").queue();
            });
            // set the bot's status to the amount of players online
            Bot.discordBot.getPresence().setActivity(Activity.playing("S " + (server.getPlayerManager().getPlayerList().size() + 1) + "/" + server.getPlayerManager().getMaxPlayerCount() + " hráči!"));
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            // send message to discord that the player left
            ServerPlayerEntity player = handler.getPlayer();
            if (Bot.discordBot == null) return;
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("Odpojil/a/o se: **" + player.getDisplayName().getString() + "**!").queue();
            });
            Bot.discordBot.getPresence().setActivity(Activity.playing("S " + (server.getPlayerManager().getPlayerList().size() + 1) + "/" + server.getPlayerManager().getMaxPlayerCount() + " hráči!"));
        });
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            // send message to discord when someone sends a message in chat
            if (Bot.discordBot == null) return;
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("**" + sender.getDisplayName().getString() + "**: " + message.getContent().getString()).queue();
            });
        });
        // bot stuff
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            // more discord stuff, also set the server instance in the Main class
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
            // sets the bots status to idle when the server is starting
            if (Bot.discordBot == null) return;
            Bot.discordBot.getPresence().setStatus(OnlineStatus.IDLE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            // more discord stuff
            if (Bot.discordBot == null) return;
            try {
                Bot.discordBot.shutdownNow();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            // more discord stuff
            if (Bot.discordBot == null) return;
            try {
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
