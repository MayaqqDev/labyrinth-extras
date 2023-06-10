package dev.mayaqq.labyrinthextras.registry;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.config.LabyrinthExtrasConfig;
import dev.mayaqq.labyrinthextras.discord.Bot;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import dev.mayaqq.labyrinthextras.utils.FabricTaylorUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.message.v1.ServerMessageEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;
import net.minecraft.server.network.ServerPlayerEntity;
import org.samo_lego.fabrictailor.util.SkinFetcher;

import java.awt.*;

public class EventRegistry {
    public static void register() {
        ServerPlayConnectionEvents.JOIN.register((handler, sender, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            // set the skin of the player to their name
            String name = player.getName().getString();
            if (name.startsWith(".") && ServerState.getPlayerState(player).hasCustomSkin) return;
            FabricTaylorUtils.setSkin(player, () -> SkinFetcher.fetchSkinByName(name));
            if (Bot.discordBot == null) return;
            Bot.discordBot.getPresence();
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("Připojil se: **" + player.getDisplayName() + "**!").queue();
            });
            Bot.discordBot.getPresence().setActivity(Activity.playing("S " + server.getPlayerManager().getPlayerList().size() + "/" + server.getPlayerManager().getMaxPlayerCount() + " hráči!"));
        });
        ServerPlayConnectionEvents.DISCONNECT.register((handler, server) -> {
            ServerPlayerEntity player = handler.getPlayer();
            if (Bot.discordBot == null) return;
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("Odpojil se: **" + player.getDisplayName() + "**!").queue();
            });
            Bot.discordBot.getPresence().setActivity(Activity.playing("S " + server.getPlayerManager().getPlayerList().size() + "/" + server.getPlayerManager().getMaxPlayerCount() + " hráči!"));
        });
        ServerMessageEvents.CHAT_MESSAGE.register((message, sender, params) -> {
            if (Bot.discordBot == null) return;
            Bot.discordBot.getGuilds().forEach(guild -> {
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessage("**" + sender.getDisplayName() + "**: " + message.toString()).queue();
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
                eb.setImage(server.getIconFile().get().toAbsolutePath().toString());
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessageEmbeds(eb.build()).queue();
            });
        });
        ServerLifecycleEvents.SERVER_STARTING.register(server -> {
            if (Bot.discordBot == null) return;
            Bot.discordBot.getPresence().setStatus(OnlineStatus.IDLE);
        });
        ServerLifecycleEvents.SERVER_STOPPED.register(server -> {
            if (Bot.discordBot == null) return;
            Bot.discordBot.getPresence().setStatus(OnlineStatus.OFFLINE);
            Bot.discordBot.getGuilds().forEach(guild -> {
                EmbedBuilder eb = new EmbedBuilder();
                eb.setTitle("Server je vypnutý!");
                eb.setDescription("Počkej na to až se znovu zapne!");
                eb.setColor(Color.RED);
                eb.setImage(server.getIconFile().get().toAbsolutePath().toString());
                guild.getChannelById(TextChannel.class, LabyrinthExtrasConfig.CONFIG.botChannel).sendMessageEmbeds(eb.build()).queue();
            });
        });
        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            if (Bot.discordBot == null) return;
            Bot.discordBot.getPresence().setStatus(OnlineStatus.DO_NOT_DISTURB);
        });
    }
}
