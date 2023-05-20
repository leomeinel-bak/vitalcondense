/*
 * File: VitalCondenseCmd.java
 * Author: Leopold Meinel (leo@meinel.dev)
 * -----
 * Copyright (c) 2023 Leopold Meinel & contributors
 * SPDX ID: GPL-3.0-or-later
 * URL: https://www.gnu.org/licenses/gpl-3.0-standalone.html
 * -----
 */

package dev.meinel.leo.vitalcondense.commands;

import dev.meinel.leo.vitalcondense.utils.commands.Cmd;
import dev.meinel.leo.vitalcondense.utils.commands.CmdSpec;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class VitalCondenseCmd implements CommandExecutor {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
            @NotNull String label, @NotNull String[] args) {
        if (Cmd.isArgsLengthNotEqualTo(sender, args, 0)) {
            return false;
        }
        doCondense(sender);
        return true;
    }

    private void doCondense(@NotNull CommandSender sender) {
        if (CmdSpec.isInvalidCmd(sender, "vitalcondense.condense")) {
            return;
        }
        Player senderPlayer = (Player) sender;
        CmdSpec.handleCondense(senderPlayer);
    }
}
