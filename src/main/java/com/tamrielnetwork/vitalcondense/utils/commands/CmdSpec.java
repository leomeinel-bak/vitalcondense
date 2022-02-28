/*
 * VitalCondense is a Spigot Plugin that gives players the ability to condense items in their inventory.
 * Copyright © 2022 Leopold Meinel
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see https://github.com/TamrielNetwork/VitalCompact/blob/main/LICENSE
 */

package com.tamrielnetwork.vitalcondense.utils.commands;

import com.tamrielnetwork.vitalcondense.VitalCondense;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmdSpec {

	private static final VitalCondense main = JavaPlugin.getPlugin(VitalCondense.class);

	public static void doCondense(@NotNull Player senderPlayer) {

		Inventory senderInventory = senderPlayer.getInventory();
		ItemStack[] inventoryItemStacks = senderPlayer.getInventory().getContents();
		HashMap<Integer, List<ItemStack>> validItemsMap = CmdSpec.getValidInventoryItemStacks(inventoryItemStacks);

		for (List<ItemStack> validItems : validItemsMap.values()) {
			for (ItemStack validItem : validItems) {
				int validItemAmount = validItem.getAmount();
				if (validItemsMap.containsKey(4) && validItemAmount >= 4) {
					if (validItemsMap.get(4).contains(validItem)) {

						int itemAmountToTake = validItemAmount - validItemAmount % 4;
						ItemStack itemsToTake = new ItemStack(validItem.getType(), itemAmountToTake);
						ItemStack itemsToGive = new ItemStack(CmdSpec.getGiveMaterial(validItem), itemAmountToTake / 4);

						senderInventory.removeItem(itemsToTake);
						senderInventory.addItem(itemsToGive);
					}
				}
				if (validItemsMap.containsKey(9) && validItemAmount >= 9) {
					if (validItemsMap.get(9).contains(validItem)) {

						int itemAmountToTake = validItemAmount - validItemAmount % 9;
						ItemStack itemsToTake = new ItemStack(validItem.getType(), itemAmountToTake);
						ItemStack itemsToGive = new ItemStack(CmdSpec.getGiveMaterial(validItem), itemAmountToTake / 9);

						senderInventory.removeItem(itemsToTake);
						senderInventory.addItem(itemsToGive);
					}
				}
			}
		}
	}

	public static HashMap<Integer, List<ItemStack>> getValidInventoryItemStacks(ItemStack[] inventoryItemStacks) {

		List<ItemStack> validInventoryCraftItems = new ArrayList<>();
		List<ItemStack> validWorkbenchCraftItems = new ArrayList<>();
		HashMap<Integer, List<ItemStack>> validItemsMap = new HashMap<>();

		for (Material material : main.getValidItemStorage().loadValidItems().get(4)) {
			for (ItemStack inventoryItemStack : inventoryItemStacks) {
				if (isInvalidItemStack(inventoryItemStack, material)) {
					continue;
				}
				validInventoryCraftItems.add(inventoryItemStack);
			}
		}
		for (Material material : main.getValidItemStorage().loadValidItems().get(9)) {
			for (ItemStack inventoryItemStack : inventoryItemStacks) {
				if (isInvalidItemStack(inventoryItemStack, material)) {
					continue;
				}
				validWorkbenchCraftItems.add(inventoryItemStack);
			}
		}
		validItemsMap.put(4, validInventoryCraftItems);
		validItemsMap.put(9, validWorkbenchCraftItems);
		return validItemsMap;
	}

	public static Material getGiveMaterial(ItemStack itemStack) {

		Material itemStackMaterial = itemStack.getType();
		return main.getValidRecipeStorage().loadValidRecipes().get(itemStackMaterial);
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {

		if (Cmd.isInvalidSender(sender)) {
			return true;
		}
		return Cmd.isNotPermitted(sender, perm);
	}

	private static boolean isInvalidItemStack(ItemStack inventoryItemStack, Material material) {

		if (inventoryItemStack == null) {
			return true;
		}
		return !(inventoryItemStack.getType() == material);
	}

}
