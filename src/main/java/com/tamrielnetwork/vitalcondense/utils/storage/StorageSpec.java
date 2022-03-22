/*
 * VitalCondense is a Spigot Plugin that gives players the ability to condense items in their inventory.
 * Copyright © 2022 Leopold Meinel & contributors
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

package com.tamrielnetwork.vitalcondense.utils.storage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageSpec {

	static final List<Material> inventoryCraft = new ArrayList<>();
	static final List<Material> workbenchCraft = new ArrayList<>();

	private StorageSpec() {
		throw new IllegalStateException("Utility class");
	}

	public static Map<Integer, List<Material>> getValidItems() {
		HashMap<Integer, List<Material>> validItems = new HashMap<>();
		Arrays.stream(Material.values())
		      .filter(Material::isBlock)
		      .flatMap(material -> Bukkit.getRecipesFor(new ItemStack(material))
		                                 .stream())
		      .filter(recipe -> !isInvalidMaterial(recipe))
		      .map(recipe -> new ArrayList<>(((ShapedRecipe) recipe).getIngredientMap()
		                                                            .values()))
		      .forEach(ingredientsList -> {
			      updateLists(ingredientsList);
			      inventoryCraft.removeIf(workbenchCraft::contains);
		      });
		validItems.put(4, inventoryCraft);
		validItems.put(9, workbenchCraft);
		return validItems;
	}

	public static Map<Material, Material> getValidRecipes() {
		EnumMap<Material, Material> validRecipes = new EnumMap<>(Material.class);
		Arrays.stream(Material.values())
		      .filter(Material::isBlock)
		      .flatMap(material -> Bukkit.getRecipesFor(new ItemStack(material))
		                                 .stream())
		      .forEach(recipe -> getRecipe(validRecipes, recipe));
		return validRecipes;
	}

	private static void getRecipe(EnumMap<Material, Material> validRecipes, Recipe recipe) {
		if (isInvalidMaterial(recipe)) {
			return;
		}
		List<ItemStack> ingredientsList = new ArrayList<>(((ShapedRecipe) recipe).getIngredientMap()
		                                                                         .values());
		updateLists(ingredientsList);
		if (inventoryCraft.stream()
		                  .anyMatch(workbenchCraft::contains) && ingredientsList.size() == 4) {
			return;
		}
		validRecipes.put(ingredientsList.get(0)
		                                .getType(), recipe.getResult()
		                                                  .getType());
	}

	private static void updateLists(List<ItemStack> ingredientsList) {
		if (ingredientsList.size() == 4) {
			inventoryCraft.add(ingredientsList.get(0)
			                                  .getType());
		}
		if (ingredientsList.size() == 9) {
			workbenchCraft.add(ingredientsList.get(0)
			                                  .getType());
		}
	}

	private static boolean isInvalidMaterial(Recipe recipe) {
		if (!(recipe instanceof ShapedRecipe)) {
			return true;
		}
		List<ItemStack> ingredientsList = new ArrayList<>(((ShapedRecipe) recipe).getIngredientMap()
		                                                                         .values());
		if (ingredientsList.contains(null)) {
			return true;
		}
		for (ItemStack itemStack : ingredientsList) {
			if (itemStack.getType()
			             .isBlock()) {
				return true;
			}
		}
		if (!(ingredientsList.size() == 9 || ingredientsList.size() == 4)) {
			return true;
		}
		return !(ingredientsList.stream()
		                        .allMatch(ingredientsList.get(0)::equals));
	}
}
