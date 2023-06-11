package dev.mayaqq.labyrinthextras.gui;

import dev.mayaqq.labyrinthextras.LabyrinthExtras;
import dev.mayaqq.labyrinthextras.storage.ServerState;
import eu.pb4.sgui.api.elements.GuiElementBuilder;
import eu.pb4.sgui.api.gui.SimpleGui;
import me.lucko.fabric.api.permissions.v0.Permissions;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import java.util.HashMap;

import static dev.mayaqq.labyrinthextras.LabyrinthExtras.LOGGER;

public class KitsGui {
    public static void gui(ServerPlayerEntity player) {
        HashMap<String, Boolean> kits = ServerState.getPlayerState(player).collectedKits;
        try {
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
            // close button
            gui.setSlot(22, new GuiElementBuilder()
                    .setName(Text.of("§4§lZavřít"))
                    .setItem(Items.BARRIER)
                    .enchant(Enchantments.KNOCKBACK, 1)
                    .hideFlags()
                    .setCallback((index, clickType, actionType) -> {
                        gui.close();
                    })
                    .build()
            );

            String[] kitNames = {"Člověk", "Hobit", "Skřet", "Elf", "Trpaslík", "Čaroděj"};
            Item[] kitItems = {Items.WOODEN_SWORD, Items.STONE_SWORD, Items.IRON_SWORD, Items.GOLDEN_SWORD, Items.DIAMOND_SWORD, Items.NETHERITE_SWORD};
            String[] kitPermissions = {"labyrinthextras.rank.clovek", "labyrinthextras.rank.hobit", "labyrinthextras.rank.skret", "labyrinthextras.rank.elf", "labyrinthextras.rank.trpaslik", "labyrinthextras.rank.carodej"};
            KitItem[][] kit = {
                    {
                        getItem(Items.WOODEN_PICKAXE, 1, null, 0),
                        getItem(Items.LEATHER_BOOTS, 1, null, 0),
                        getItem(Items.LEATHER_CHESTPLATE, 1, null, 0),
                        getItem(Items.BREAD, 16, null, 0)
                    },
                    {
                        getItem(Items.STONE_PICKAXE, 1, null, 0),
                        getItem(Items.CHAINMAIL_BOOTS, 1, null, 0),
                        getItem(Items.LEATHER_CHESTPLATE, 1, Enchantments.PROTECTION, 2),
                        getItem(Items.BREAD, 32, null, 0)
                    },
                    {
                        getItem(Items.STONE_PICKAXE, 1, Enchantments.EFFICIENCY, 1),
                        getItem(Items.CHAINMAIL_BOOTS, 1, Enchantments.PROTECTION, 1),
                        getItem(Items.CHAINMAIL_CHESTPLATE, 1, Enchantments.PROTECTION, 1),
                        getItem(Items.COOKED_CHICKEN, 16, null, 0)
                    },
                    {
                        getItem(Items.IRON_PICKAXE, 1, null, 0),
                        getItem(Items.IRON_BOOTS, 1, Enchantments.PROTECTION, 1),
                        getItem(Items.IRON_CHESTPLATE, 1, Enchantments.PROTECTION, 2),
                        getItem(Items.COOKED_BEEF, 32, null, 0)
                    },
                    {
                        getItem(Items.IRON_PICKAXE, 1, Enchantments.EFFICIENCY, 1),
                        getItem(Items.IRON_BOOTS, 1, Enchantments.PROTECTION, 2),
                        getItem(Items.IRON_CHESTPLATE, 1, Enchantments.PROTECTION, 2),
                        getItem(Items.COOKED_BEEF, 48, null, 0)
                    },
                    {
                        getItem(Items.DIAMOND_PICKAXE, 1, Enchantments.EFFICIENCY, 2),
                        getItem(Items.DIAMOND_BOOTS, 1, Enchantments.PROTECTION, 1),
                        getItem(Items.DIAMOND_CHESTPLATE, 1, Enchantments.PROTECTION, 1),
                        getItem(Items.GOLDEN_CARROT, 32, null, 0)
                    },
            };

            for (int i = 0; i < kitNames.length; i++) {
                boolean hasPermission = Permissions.check(player, kitPermissions[i]);
                boolean hasKit = kits.getOrDefault(String.valueOf(i), false);
                boolean hasPerms;

                Text nameText = Text.of("§a" + kitNames[i]);
                Item kitItem = kitItems[i];
                if (hasKit || !hasPermission) {
                    hasPerms = false;
                    nameText = Text.of("§c" + kitNames[i]);
                    kitItem = Items.BARRIER;
                } else {
                    hasPerms = true;
                }
                int bonus = 4;
                if (i != 0) {
                     bonus += 6;
                }
                int finalI = i;
                gui.setSlot(i + bonus, new GuiElementBuilder()
                        .setName(nameText)
                        .setItem(kitItem)
                        .addLoreLine(Text.of("§7Pro lepší kit si kupte §6§lRank §7na §6§lrank.labyrinthmc.world"))
                        .hideFlags()
                        .setCallback((index, clickType, actionType) -> {
                            if (!hasPerms) {
                                // open a link to buy rank
                                player.sendMessage(Text.literal("Rank: ").formatted(Formatting.GOLD)
                                        .append(Text.literal("https://rank.labyrinthmc.world")
                                                .styled(style -> style
                                                        .withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://rank.labyrinthmc.world"))))
                                        .append(Text.literal(" pro lepší kit!").formatted(Formatting.GOLD)));
                                return;
                            }
                            gui.close();
                            for (KitItem item : kit[finalI]) {
                                player.giveItemStack(item.getItemStack());
                            }
                            ServerState.getPlayerState(player).collectedKits.put(String.valueOf(finalI), true);
                        })
                        .build()
                );
            }

            gui.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static KitItem getItem(Item item, int amount, Enchantment enchantment, int enchantmentLevel) {
        return new KitItem(item, amount, enchantment, enchantmentLevel);
    }
    public static class KitItem {
        public Item item;
        public ItemStack itemStack;
        public int amount;
        public KitItem(Item item, int amount, Enchantment enchantment, int enchantmentLevel) {
            this.item = item;
            this.itemStack = new ItemStack(item, amount);
            this.amount = amount;
            if (enchantment != null) {
                this.itemStack.addEnchantment(enchantment, enchantmentLevel);

            }
        }
        public ItemStack getItemStack() {
            return this.itemStack;
        }
    }
}
