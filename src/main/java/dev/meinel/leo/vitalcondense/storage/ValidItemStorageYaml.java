/*
 * File: ValidItemStorageYaml.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2022 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalcondense.storage;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
public class ValidItemStorageYaml
		extends ValidItemStorage {

	private static final String IOEXCEPTION = "VitalCondense encountered an IOException while executing task";
	private static final String CLASSNOTFOUNDEXCEPTION = "VitalCondense encountered a ClassNotFoundException while executing task";
	private final File validItemFile;

	public ValidItemStorageYaml() {
		validItemFile = new File(main.getDataFolder(), "validitemstorage.yml");
	}

	@Override
	public void saveValidItems(@NotNull Map<Integer, List<Material>> hashMap) {
		try (FileOutputStream fileOut = new FileOutputStream(validItemFile);
				ObjectOutputStream out = new ObjectOutputStream(fileOut)) {
			out.writeObject(hashMap);
		} catch (IOException ignored) {
			Bukkit.getLogger()
					.warning(IOEXCEPTION);
		}
	}

	@Override
	public Map<Integer, List<Material>> loadValidItems() {
		HashMap<Integer, List<Material>> validItems = new HashMap<>();
		try (FileInputStream fileIn = new FileInputStream(validItemFile);
				ObjectInputStream in = new ObjectInputStream(fileIn)) {
			validItems = (HashMap<Integer, List<Material>>) in.readObject();
		} catch (IOException | ClassNotFoundException ignored) {
			Bukkit.getLogger()
					.warning(IOEXCEPTION);
			Bukkit.getLogger()
					.warning(CLASSNOTFOUNDEXCEPTION);
		}
		return validItems;
	}

	@Override
	public void clear() {
		try {
			Files.delete(validItemFile.toPath());
			Bukkit.getLogger()
					.info("VitalCondense deleted valid items!");
			Bukkit.getLogger()
					.info("VitalCondense will restore them on startup!");
		} catch (IOException ignored) {
			Bukkit.getLogger()
					.warning(IOEXCEPTION);
		}
	}
}
