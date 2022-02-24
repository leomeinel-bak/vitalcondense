/*
 * VitalCompact is a Spigot Plugin that gives players the ability to compact items in their inventory.
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

package com.tamrielnetwork.vitalcondense.utils;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main {

	public static HashMap<Integer, List<Material>> getValidItems() {

		HashMap<Integer, List<Material>> validItems = new HashMap<>();
		List<Material> inventoryCraft = new ArrayList<>();
		List<Material> workbenchCraft = new ArrayList<>();

		for (Material material : Material.values()) {
			if (!material.isBlock()) {
				continue;
			}
			ItemStack itemStack = new ItemStack(material);
			List<Recipe> recipeList = Bukkit.getRecipesFor(itemStack);
			for (Recipe recipe : recipeList) {
				if (!(recipe instanceof ShapedRecipe)) {
					continue;
				}
				List<ItemStack> ingredientsList = new ArrayList<>(((ShapedRecipe) recipe).getIngredientMap().values());

				if (ingredientsList.contains(null)) {
					continue;
				}
				if (isInvalidMaterial(ingredientsList)) {
					continue;
				}
				if (!(ingredientsList.size() == 9 || ingredientsList.size() == 4)) {
					continue;
				}
				if (!(ingredientsList.stream().allMatch(ingredientsList.get(0)::equals))) {
					continue;
				}
				if (ingredientsList.size() == 4) {
					inventoryCraft.add(ingredientsList.get(0).getType());
					validItems.put(4, inventoryCraft);
				}
				if (ingredientsList.size() == 9) {
					workbenchCraft.add(ingredientsList.get(0).getType());
					validItems.put(9, workbenchCraft);
				}
				inventoryCraft.removeIf(workbenchCraft::contains);
			}

		}
		return validItems;

	}

	private static boolean isInvalidMaterial(List<ItemStack> ingredientsList) {

		for (ItemStack itemStack : ingredientsList) {
			if (itemStack.getType().isBlock()) {
				return true;
			}
		}
		return false;
	}

}
