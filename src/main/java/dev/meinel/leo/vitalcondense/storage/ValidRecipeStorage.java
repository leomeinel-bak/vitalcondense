/*
 * File: ValidRecipeStorage.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalcondense.storage;

import dev.meinel.leo.vitalcondense.VitalCondense;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public abstract class ValidRecipeStorage {

    protected final VitalCondense main = JavaPlugin.getPlugin(VitalCondense.class);

    public abstract Map<Material, Material> loadValidRecipes();

    public abstract void saveValidRecipes(@NotNull Map<Material, Material> validRecipes);

    public abstract void clear();
}
