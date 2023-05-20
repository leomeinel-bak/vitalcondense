/*
 * File: ValidItemStorage.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalcondense.storage;

import dev.meinel.leo.vitalcondense.VitalCondense;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;

public abstract class ValidItemStorage {

    protected final VitalCondense main = JavaPlugin.getPlugin(VitalCondense.class);

    public abstract Map<Integer, List<Material>> loadValidItems();

    public abstract void saveValidItems(@NotNull Map<Integer, List<Material>> hashMap);

    public abstract void clear();
}
