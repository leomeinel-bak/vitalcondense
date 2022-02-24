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
import java.util.HashMap;
import java.util.List;

public class ValidItemStorageYaml extends ValidItemStorage {

	private final File validItemFile;

	public ValidItemStorageYaml() {

		validItemFile = new File(main.getDataFolder(), "validitemstorage.yml");
	}

	@Override
	public void saveValidItems(@NotNull HashMap<Integer, List<Material>> hashMap) {

		try {
			FileOutputStream fileOut = new FileOutputStream(validItemFile);
			ObjectOutputStream out = new ObjectOutputStream(fileOut);
			out.writeObject(hashMap);
			out.close();
			fileOut.close();
		} catch (IOException i) {
			i.printStackTrace();
		}
	}

	@Override
	public HashMap<Integer, List<Material>> loadValidItems() {

		HashMap<Integer, List<Material>> validItems = new HashMap<>();
		try {

			FileInputStream fileIn = new FileInputStream(validItemFile);
			ObjectInputStream in = new ObjectInputStream(fileIn);
			validItems = (HashMap<Integer, List<Material>>) in.readObject();
			in.close();
			fileIn.close();

		} catch (IOException | ClassNotFoundException exception) {
			exception.printStackTrace();
		}
		return validItems;

	}

	@Override
	public void clear() {

		if (validItemFile.delete()) {
			Bukkit.getLogger().info("VitalCondense deleted persistent HashMap containing valid items!");
			Bukkit.getLogger().info("VitalCondense will restore it on the next restart!");

		}

	}

}
