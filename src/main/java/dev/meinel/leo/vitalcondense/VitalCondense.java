/*
 * File: VitalCondense.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
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
                .info("VitalCondense v" + this.getPluginMeta().getVersion() + " enabled");
        Bukkit.getLogger()
                .info("Copyright (C) 2022 Leopold Meinel");
        Bukkit.getLogger()
                .info("This program comes with ABSOLUTELY NO WARRANTY!");
        Bukkit.getLogger()
                .info("This is free software, and you are welcome to redistribute it under certain conditions.");
        Bukkit.getLogger()
                .info("See https://www.gnu.org/licenses/gpl-3.0-standalone.html for more details.");
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
                .info("VitalCondense v" + this.getPluginMeta().getVersion() + " disabled");
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
