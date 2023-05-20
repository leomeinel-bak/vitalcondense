/*
 * File: CmdSpec.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalcondense.utils.commands;

import dev.meinel.leo.vitalcondense.VitalCondense;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CmdSpec {

    private static final VitalCondense main = JavaPlugin.getPlugin(VitalCondense.class);

    private CmdSpec() {
        throw new IllegalStateException("Utility class");
    }

    public static void handleCondense(@NotNull Player senderPlayer) {
        Inventory senderInventory = senderPlayer.getInventory();
        ItemStack[] inventoryItems = senderInventory.getStorageContents();
        HashMap<Integer, List<ItemStack>> validItemsMap = getValidInventoryItems(inventoryItems);
        for (List<ItemStack> validItems : validItemsMap.values()) {
            for (ItemStack validItem : validItems) {
                int validItemAmount = validItem.getAmount();
                doCondense(senderInventory, validItemsMap, validItem, validItemAmount, 4);
                doCondense(senderInventory, validItemsMap, validItem, validItemAmount, 9);
            }
        }
    }

    public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {
        return Cmd.isInvalidSender(sender) || Cmd.isNotPermitted(sender, perm);
    }

    private static void doCondense(@NotNull Inventory senderInventory,
            @NotNull HashMap<Integer, List<ItemStack>> validItemsMap, @NotNull ItemStack validItem,
            int validItemAmount, int gridSize) {
        if (validItemsMap.containsKey(gridSize) && validItemAmount >= gridSize
                && validItemsMap.get(gridSize).contains(validItem)) {
            int itemAmountToTake = validItemAmount - validItemAmount % gridSize;
            ItemStack itemsToTake = new ItemStack(validItem.getType(), itemAmountToTake);
            ItemStack itemsToGive =
                    new ItemStack(getGiveMaterial(validItem), itemAmountToTake / gridSize);
            senderInventory.removeItem(itemsToTake);
            senderInventory.addItem(itemsToGive);
        }
    }

    private static void calculateAmount(@NotNull ItemStack[] inventoryItemStacks,
            @NotNull EnumMap<Material, Integer> amountsMap, @NotNull Material material) {
        for (ItemStack inventoryItems : inventoryItemStacks) {
            if (isInvalidItem(inventoryItems, material)) {
                continue;
            }
            if (amountsMap.containsKey(inventoryItems.getType())) {
                amountsMap.put(inventoryItems.getType(),
                        (amountsMap.get(inventoryItems.getType()) + inventoryItems.getAmount()));
            } else {
                amountsMap.put(inventoryItems.getType(), inventoryItems.getAmount());
            }
        }
    }

    private static HashMap<Integer, List<ItemStack>> getValidInventoryItems(
            @NotNull ItemStack[] inventoryItems) {
        List<ItemStack> smallGridItems = new ArrayList<>();
        List<ItemStack> bigGridItems = new ArrayList<>();
        HashMap<Integer, List<ItemStack>> validItemsMap = new HashMap<>();
        EnumMap<Material, Integer> smallGridAmountsMap = new EnumMap<>(Material.class);
        EnumMap<Material, Integer> bigGridAmountsMap = new EnumMap<>(Material.class);
        for (Material material : main.getValidItemStorage().loadValidItems().get(4)) {
            calculateAmount(inventoryItems, smallGridAmountsMap, material);
        }
        for (Map.Entry<Material, Integer> entrySet : smallGridAmountsMap.entrySet()) {
            smallGridItems.add(new ItemStack(entrySet.getKey(), entrySet.getValue()));
        }
        for (Material material : main.getValidItemStorage().loadValidItems().get(9)) {
            calculateAmount(inventoryItems, bigGridAmountsMap, material);
        }
        for (Map.Entry<Material, Integer> entrySet : bigGridAmountsMap.entrySet()) {
            bigGridItems.add(new ItemStack(entrySet.getKey(), entrySet.getValue()));
        }
        validItemsMap.put(4, smallGridItems);
        validItemsMap.put(9, bigGridItems);
        return validItemsMap;
    }

    private static Material getGiveMaterial(@NotNull ItemStack itemStack) {
        Material material = itemStack.getType();
        return main.getValidRecipeStorage().loadValidRecipes().get(material);
    }

    private static boolean isInvalidItem(ItemStack inventoryItem, @NotNull Material material) {
        return inventoryItem == null || inventoryItem.getType() != material;
    }
}
