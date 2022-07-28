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
 * along with this program. If not, see https://github.com/LeoMeinel/VitalCompact/blob/main/LICENSE
 */

package dev.meinel.leo.vitalcondense;

import dev.meinel.leo.vitalcondense.commands.VitalCondenseCmd;
import dev.meinel.leo.vitalcondense.files.Messages;
import dev.meinel.leo.vitalcondense.storage.ValidItemStorage;
import dev.meinel.leo.vitalcondense.storage.ValidItemStorageYaml;
import dev.meinel.leo.vitalcondense.storage.ValidRecipeStorage;
import dev.meinel.leo.vitalcondense.storage.ValidRecipeStorageYaml;
import dev.meinel.leo.vitalcondense.utils.storage.StorageSpec;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class VitalCondense
		extends JavaPlugin {

	private ValidItemStorage validItemStorage;
	private ValidRecipeStorage validRecipeStorage;
	private Messages messages;

	@Override
	public void onEnable() {
		Objects.requireNonNull(getCommand("condense"))
		       .setExecutor(new VitalCondenseCmd());
		messages = new Messages();
		setUpStorage();
		Bukkit.getLogger()
		      .info("VitalCondense v" + this.getDescription()
		                                    .getVersion() + " enabled");
		Bukkit.getLogger()
		      .info("Copyright (C) 2022 Leopold Meinel");
		Bukkit.getLogger()
		      .info("This program comes with ABSOLUTELY NO WARRANTY!");
		Bukkit.getLogger()
		      .info("This is free software, and you are welcome to redistribute it under certain conditions.");
		Bukkit.getLogger()
		      .info("See https://github.com/LeoMeinel/VitalCondense/blob/main/LICENSE for more details.");
	}

	private void setUpStorage() {
		this.validItemStorage = new ValidItemStorageYaml();
		this.validRecipeStorage = new ValidRecipeStorageYaml();
		validItemStorage.saveValidItems(StorageSpec.getValidItems());
		validRecipeStorage.saveValidRecipes(StorageSpec.getValidRecipes());
	}

	@Override
	public void onDisable() {
		validItemStorage.clear();
		validRecipeStorage.clear();
		Bukkit.getLogger()
		      .info("VitalCondense v" + this.getDescription()
		                                    .getVersion() + " disabled");
	}

	public Messages getMessages() {
		return messages;
	}

	public ValidItemStorage getValidItemStorage() {
		return validItemStorage;
	}

	public ValidRecipeStorage getValidRecipeStorage() {
		return validRecipeStorage;
	}
}


