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
import org.bukkit.util.io.BukkitObjectInputStream;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings ("unchecked")
public class ValidItemStorageYaml extends ValidItemStorage {

	private static final String IOEXCEPTION = "VitalCondense encountered an IOException while executing task";
	private static final String CLASSNOTFOUNDEXCEPTION = "VitalCondense encountered a ClassNotFoundException while executing task";
	private final File validItemFile;

	public ValidItemStorageYaml() {

		validItemFile = new File(main.getDataFolder(), "validitemstorage.yml");
	}

	@Override
	public void saveValidItems(@NotNull Map<Integer, List<Material>> hashMap) {

		try (FileOutputStream fileOut = new FileOutputStream(validItemFile); ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(hashMap);
		} catch (IOException ignored) {
			Bukkit.getLogger().warning(IOEXCEPTION);
		}
	}

	@Override
	public Map<Integer, List<Material>> loadValidItems() {

		HashMap<Integer, List<Material>> validItems = new HashMap<>();

		try (FileInputStream fileIn = new FileInputStream(validItemFile); ObjectInputStream in = new ObjectInputStream(fileIn)) {
			validItems = (HashMap<Integer, List<Material>>) in.readObject();

		} catch (IOException | ClassNotFoundException ignored) {
			Bukkit.getLogger().warning(IOEXCEPTION);
			Bukkit.getLogger().warning(CLASSNOTFOUNDEXCEPTION);
		}
		return validItems;

	}

	@Override
	public void clear() {

		if (validItemFile.delete()) {
			Bukkit.getLogger().info("VitalCondense deleted valid items!");
			Bukkit.getLogger().info("VitalCondense will restore them on startup!");

		}

	}

}
