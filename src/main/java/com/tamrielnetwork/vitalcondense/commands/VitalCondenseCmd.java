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

package com.tamrielnetwork.vitalcondense.commands;

import com.tamrielnetwork.vitalcondense.utils.commands.Cmd;
import com.tamrielnetwork.vitalcondense.utils.commands.CmdSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class VitalCondenseCmd implements CommandExecutor {

	@Override
	public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

		if (Cmd.isArgsLengthNotEqualTo(sender, args, 0)) {
			return true;
		}
		doCondense(sender);
		return true;

	}

	private void doCondense(@NotNull CommandSender sender) {

		if (Cmd.isInvalidSender(sender) || Cmd.isNotPermitted(sender, "vitalcondense.craft")) {
			return;
		}
		Player senderPlayer = (Player) sender;

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

}
