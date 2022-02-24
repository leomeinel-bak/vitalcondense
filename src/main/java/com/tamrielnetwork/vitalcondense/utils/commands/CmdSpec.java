/*
 * VitalCondense is a Spigot Plugin that gives players the ability to compact items in their inventory.
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
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class CmdSpec {

	private static final VitalCondense main = JavaPlugin.getPlugin(VitalCondense.class);

	public static HashMap<Integer, List<ItemStack>> getValidInventoryItemStacks(ItemStack[] inventoryItemStacks) {

		List<ItemStack> validInventoryCraftItems = new ArrayList<>();
		List<ItemStack> validWorkbenchCraftItems = new ArrayList<>();
		HashMap<Integer, List<ItemStack>> validItemsMap = new HashMap<>();

		for (Material material : main.getValidItemStorage().loadValidItems().get(4)) {
			for (ItemStack inventoryItemStack : inventoryItemStacks) {
				if (inventoryItemStack == null) {
					continue;
				}
				if (!(inventoryItemStack.getType() == material)) {
					continue;
				}
				validInventoryCraftItems.add(inventoryItemStack);
				validItemsMap.put(4, validInventoryCraftItems);
			}
		}
		for (Material material : main.getValidItemStorage().loadValidItems().get(9)) {
			for (ItemStack inventoryItemStack : inventoryItemStacks) {
				if (inventoryItemStack == null) {
					continue;
				}
				if (!(inventoryItemStack.getType() == material)) {
					continue;
				}
				validWorkbenchCraftItems.add(inventoryItemStack);
				validItemsMap.put(9, validWorkbenchCraftItems);
			}
		}
		return validItemsMap;
	}

}
