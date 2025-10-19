package pl.nubet.debugstickinsurvival.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;
import pl.nubet.debugstickinsurvival.config.ConfigurationManager;

/**
 * Handles the /debugstickreload command for reloading plugin configuration.
 */
public class ReloadCommand implements CommandExecutor
{

    private final ConfigurationManager configurationManager;

    public ReloadCommand(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command,
        @NotNull String label, @NotNull String[] args) {
        if (!sender.hasPermission("debugstickcs.reload")) {
            sender.sendMessage("§cYou don't have permission to reload this plugin.");
            return true;
        }

        try {
            configurationManager.reload();
            sender.sendMessage("§aDebug Stick Crafting configuration reloaded successfully!");
            return true;
        } catch (Exception e) {
            sender.sendMessage("§cFailed to reload configuration. Check console for details.");
            e.printStackTrace();
            return true;
        }
    }
}

