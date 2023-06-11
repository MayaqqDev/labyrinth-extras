package dev.mayaqq.labyrinthextras.gui;

import dev.mayaqq.labyrinthextras.storage.ServerState;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;

import java.util.HashMap;

public class KitsGui {
    public static void gui(ServerPlayerEntity player) {
        HashMap<String, Boolean> kits = ServerState.getPlayerState(player).collectedKits;
        SimpleGui gui = new SimpleGui(ScreenHandlerType.GENERIC_9X3, player, false) {};
        // gui title
        gui.setTitle(Text.of("Kity"));
        // gui background
        for (int i = 0; i < 27; i++) {
            gui.setSlot(i, new GuiElementBuilder()
                            .setName(Text.of(" "))
                            .setItem(Items.GRAY_STAINED_GLASS_PANE)
                    .build()
            );
        }
        gui.setSlot(24, new GuiElementBuilder()
                        .setName(Text.of("Zavřít"))
                        .setItem(Items.BARRIER)
                        .enchant(Enchantments.KNOCKBACK, 1)
                        .hideFlags()
                .build()
        );

        if (!kits.get("1")) {
            gui.setSlot(5, new GuiElementBuilder()
                            .setName(Text.of("§aČlověk"))
                            .setItem(Items.WOODEN_SWORD)
                            .hideFlags()
                    .build()
            );
        } else {
            gui.setSlot(5, new GuiElementBuilder()
                            .setName(Text.of("§cČlověk"))
                            .setItem(Items.BARRIER)
                            .hideFlags()
                    .build()
            );
        }
        if (!kits.get("2") && Permissions.check(player, "labyrinthextras.rank.hobbit")) {
            gui.setSlot(11, new GuiElementBuilder()
                            .setName(Text.of("§aHobit"))
                            .setItem(Items.STONE_SWORD)
                            .hideFlags()
                    .build()
            );
        } else {
            gui.setSlot(11, new GuiElementBuilder()
                            .setName(Text.of("§cHobit"))
                            .setItem(Items.BARRIER)
                            .hideFlags()
                    .build()
            );
        }
        if (!kits.get("3") && Permissions.check(player, "labyrinthextras.rank.skret")) {
            gui.setSlot(12, new GuiElementBuilder()
                            .setName(Text.of("§aSkřet"))
                            .setItem(Items.IRON_SWORD)
                            .hideFlags()
                    .build()
            );
        } else {
            gui.setSlot(12, new GuiElementBuilder()
                            .setName(Text.of("§cSkřet"))
                            .setItem(Items.BARRIER)
                            .hideFlags()
                    .build()
            );
        }
        if (!kits.get("4") && Permissions.check(player, "labyrinthextras.rank.elf")) {
            gui.setSlot(13, new GuiElementBuilder()
                            .setName(Text.of("§aElf"))
                            .setItem(Items.GOLDEN_SWORD)
                            .hideFlags()
                    .build()
            );
        } else {
            gui.setSlot(13, new GuiElementBuilder()
                            .setName(Text.of("§cElf"))
                            .setItem(Items.BARRIER)
                            .hideFlags()
                    .build()
            );
        }
        if (!kits.get("5") && Permissions.check(player, "labyrinthextras.rank.trpaslik")) {
            gui.setSlot(14, new GuiElementBuilder()
                            .setName(Text.of("§aTrpaslík"))
                            .setItem(Items.DIAMOND_SWORD)
                            .hideFlags()
                    .build()
            );
        } else {
            gui.setSlot(14, new GuiElementBuilder()
                            .setName(Text.of("§cTrpaslík"))
                            .setItem(Items.BARRIER)
                            .hideFlags()
                    .build()
            );
        }
        if (!kits.get("6") && Permissions.check(player, "labyrinthextras.rank.carodej")) {
            gui.setSlot(15, new GuiElementBuilder()
                            .setName(Text.of("§aČaroděj"))
                            .setItem(Items.NETHERITE_SWORD)
                            .hideFlags()
                    .build()
            );
        } else {
            gui.setSlot(15, new GuiElementBuilder()
                            .setName(Text.of("§cČaroděj"))
                            .setItem(Items.BARRIER)
                            .hideFlags()
                    .build()
            );
        }

        gui.open();
    }
}
