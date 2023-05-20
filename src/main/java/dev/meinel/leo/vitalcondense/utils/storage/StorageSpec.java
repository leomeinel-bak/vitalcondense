/*
 * File: StorageSpec.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalcondense.utils.storage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;
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
        for (Material material : Material.values()) {
            if (!material.isBlock()) {
                continue;
            }
            for (Recipe recipe : Bukkit.getRecipesFor(new ItemStack(material))) {
                if (isInvalidMaterial(recipe)) {
                    continue;
                }
                List<ItemStack> ingredientsList =
                        new ArrayList<>(((ShapedRecipe) recipe).getIngredientMap().values());
                updateLists(ingredientsList);
                inventoryCraft.removeIf(workbenchCraft::contains);
            }
        }
        validItems.put(4, inventoryCraft);
        validItems.put(9, workbenchCraft);
        return validItems;
    }

    public static Map<Material, Material> getValidRecipes() {
        EnumMap<Material, Material> validRecipes = new EnumMap<>(Material.class);
        for (Material material : Material.values()) {
            if (!material.isBlock()) {
                continue;
            }
            for (Recipe recipe : Bukkit.getRecipesFor(new ItemStack(material))) {
                getRecipe(validRecipes, recipe);
            }
        }
        return validRecipes;
    }

    private static void getRecipe(EnumMap<Material, Material> validRecipes, Recipe recipe) {
        if (isInvalidMaterial(recipe)) {
            return;
        }
        List<ItemStack> ingredientsList =
                new ArrayList<>(((ShapedRecipe) recipe).getIngredientMap().values());
        updateLists(ingredientsList);
        if (inventoryCraft.stream().anyMatch(workbenchCraft::contains)
                && ingredientsList.size() == 4) {
            return;
        }
        validRecipes.put(ingredientsList.get(0).getType(), recipe.getResult().getType());
    }

    private static void updateLists(List<ItemStack> ingredientsList) {
        if (ingredientsList.size() == 4) {
            inventoryCraft.add(ingredientsList.get(0).getType());
        }
        if (ingredientsList.size() == 9) {
            workbenchCraft.add(ingredientsList.get(0).getType());
        }
    }

    private static boolean isInvalidMaterial(Recipe recipe) {
        if (!(recipe instanceof ShapedRecipe)) {
            return true;
        }
        List<ItemStack> ingredientsList =
                new ArrayList<>(((ShapedRecipe) recipe).getIngredientMap().values());
        if (ingredientsList.contains(null)) {
            return true;
        }
        for (ItemStack itemStack : ingredientsList) {
            if (itemStack.getType().isBlock()) {
                return true;
            }
        }
        if (!(ingredientsList.size() == 9 || ingredientsList.size() == 4)) {
            return true;
        }
        return !(ingredientsList.stream().allMatch(ingredientsList.get(0)::equals));
    }
}
