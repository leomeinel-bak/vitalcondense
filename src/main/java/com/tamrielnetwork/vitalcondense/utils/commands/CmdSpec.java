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
		ItemStack[] inventoryItemStacks = senderInventory.getStorageContents();

		List<ItemStack> validItems = getValidInventoryItemStacks(inventoryItemStacks);
		HashMap<Integer, List<Material>> validMaterialsMap = getValidInventoryMaterials(inventoryItemStacks);
		HashMap<Material, Integer> amountsMap = new HashMap<>();
		List<ItemStack> itemStacks = new ArrayList<>();

		for (ItemStack validItem : validItems) {
			if (amountsMap.containsKey(validItem.getType())) {
				amountsMap.put(validItem.getType(), (amountsMap.get(validItem.getType()) + validItem.getAmount()));
			} else {
				amountsMap.put(validItem.getType(), validItem.getAmount());
			}
		}

		for (Material material : amountsMap.keySet()) {
			itemStacks.add(new ItemStack(material, amountsMap.get(material)));
		}

		for (ItemStack itemStack : itemStacks) {
			if (validMaterialsMap.containsKey(4) && itemStack.getAmount() >= 4) {
				if (validMaterialsMap.get(4).stream().anyMatch(amountsMap.keySet()::contains)) {

					int itemAmountToTake = itemStack.getAmount() - itemStack.getAmount() % 4;
					ItemStack itemsToTake = new ItemStack(itemStack.getType(), itemAmountToTake);
					ItemStack itemsToGive = new ItemStack(getGiveMaterial(itemStack), itemAmountToTake / 4);

					senderInventory.removeItem(itemsToTake);
					senderInventory.addItem(itemsToGive);
				}
			}
			if (validMaterialsMap.containsKey(9) && itemStack.getAmount() >= 9) {
				if (validMaterialsMap.get(9).stream().anyMatch(amountsMap.keySet()::contains)) {

					int itemAmountToTake = itemStack.getAmount() - itemStack.getAmount() % 9;
					ItemStack itemsToTake = new ItemStack(itemStack.getType(), itemAmountToTake);
					ItemStack itemsToGive = new ItemStack(getGiveMaterial(itemStack), itemAmountToTake / 9);

					senderInventory.removeItem(itemsToTake);
					senderInventory.addItem(itemsToGive);
				}
			}
		}
	}

	public static boolean isInvalidCmd(@NotNull CommandSender sender, @NotNull String perm) {

		if (Cmd.isInvalidSender(sender)) {
			return true;
		}
		return Cmd.isNotPermitted(sender, perm);
	}

	private static List<ItemStack> getValidInventoryItemStacks(ItemStack[] inventoryItemStacks) {

		List<ItemStack> validItems = new ArrayList<>();

		for (List<Material> materials : main.getValidItemStorage().loadValidItems().values()) {
			for (Material material : materials) {
				for (ItemStack inventoryItemStack : inventoryItemStacks) {
					if (isInvalidItemStack(inventoryItemStack, material)) {
						continue;
					}
					validItems.add(inventoryItemStack);
				}
			}
		}
		return validItems;
	}

	private static HashMap<Integer, List<Material>> getValidInventoryMaterials(ItemStack[] inventoryItemStacks) {

		List<Material> validInventoryCraftMaterials = new ArrayList<>();
		List<Material> validWorkbenchCraftMaterials = new ArrayList<>();
		HashMap<Integer, List<Material>> validMaterialsMap = new HashMap<>();

		for (Material material : main.getValidItemStorage().loadValidItems().get(4)) {
			for (ItemStack inventoryItemStack : inventoryItemStacks) {
				if (isInvalidItemStack(inventoryItemStack, material)) {
					continue;
				}
				validInventoryCraftMaterials.add(inventoryItemStack.getType());
			}
		}
		for (Material material : main.getValidItemStorage().loadValidItems().get(9)) {
			for (ItemStack inventoryItemStack : inventoryItemStacks) {
				if (isInvalidItemStack(inventoryItemStack, material)) {
					continue;
				}
				validWorkbenchCraftMaterials.add(inventoryItemStack.getType());
			}
		}
		validMaterialsMap.put(4, validInventoryCraftMaterials);
		validMaterialsMap.put(9, validWorkbenchCraftMaterials);
		return validMaterialsMap;
	}

	private static Material getGiveMaterial(ItemStack itemStack) {

		Material itemStackMaterial = itemStack.getType();
		return main.getValidRecipeStorage().loadValidRecipes().get(itemStackMaterial);
	}

	private static boolean isInvalidItemStack(ItemStack inventoryItemStack, Material material) {

		if (inventoryItemStack == null) {
			return true;
		}
		return !(inventoryItemStack.getType() == material);
	}

}
