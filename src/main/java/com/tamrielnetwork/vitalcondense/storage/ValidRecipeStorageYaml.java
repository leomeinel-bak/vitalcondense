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

package com.tamrielnetwork.vitalcondense.storage;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.EnumMap;
import java.util.Map;

@SuppressWarnings ("unchecked")
public class ValidRecipeStorageYaml extends ValidRecipeStorage {

	private static final String IOEXCEPTION = "VitalCondense encountered an IOException while executing task";
	private static final String CLASSNOTFOUNDEXCEPTION = "VitalCondense encountered a ClassNotFoundException while executing task";
	private final File validRecipeFile;

	public ValidRecipeStorageYaml() {

		validRecipeFile = new File(main.getDataFolder(), "validrecipestorage.yml");
	}

	@Override
	public void saveValidRecipes(@NotNull Map<Material, Material> validRecipes) {

		try (FileOutputStream fileOut = new FileOutputStream(validRecipeFile); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(validRecipes);
		} catch (IOException ignored) {
			Bukkit.getLogger().warning(IOEXCEPTION);
		}
	}

	@Override
	public Map<Material, Material> loadValidRecipes() {

		EnumMap<Material, Material> validRecipes = new EnumMap<>(Material.class);

		try (FileInputStream fileIn = new FileInputStream(validRecipeFile); ObjectInputStream in = new ObjectInputStream(fileIn)) {
			validRecipes = (EnumMap<Material, Material>) in.readObject();

		} catch (IOException | ClassNotFoundException ignored) {
			Bukkit.getLogger().warning(IOEXCEPTION);
			Bukkit.getLogger().warning(CLASSNOTFOUNDEXCEPTION);
		}
		return validRecipes;

	}

	@Override
	public void clear() {

		try {
			Files.delete(validRecipeFile.toPath());
			Bukkit.getLogger().info("VitalCondense deleted valid recipes!");
			Bukkit.getLogger().info("VitalCondense will restore them on startup!");
		} catch (IOException ignored) {
			Bukkit.getLogger().warning(IOEXCEPTION);
		}

	}

}
