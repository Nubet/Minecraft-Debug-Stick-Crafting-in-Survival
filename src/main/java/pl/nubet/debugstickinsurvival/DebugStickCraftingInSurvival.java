package pl.nubet.debugstickinsurvival;

import org.bukkit.plugin.java.JavaPlugin;
import pl.nubet.debugstickinsurvival.command.ReloadCommand;
import pl.nubet.debugstickinsurvival.config.ConfigurationManager;
import pl.nubet.debugstickinsurvival.listener.DebugStickInteractionListener;
import pl.nubet.debugstickinsurvival.recipe.DebugStickRecipeManager;
import pl.nubet.debugstickinsurvival.service.BlockRestrictionService;

import java.util.logging.Level;

/**
 * Main plugin class
 */
public final class DebugStickCraftingInSurvival extends JavaPlugin {

    private ConfigurationManager configurationManager;
    private BlockRestrictionService blockRestrictionService;
    private DebugStickRecipeManager recipeManager;

    @Override
    public void onEnable() {
        try {
            initializePlugin();
            getLogger().info("DebugStickCraftingInSurvival v" + getDescription().getVersion() + " has been enabled!");
            getLogger().info("Running on Minecraft " + getServer().getVersion());
        } catch (Exception e) {
            getLogger().log(Level.SEVERE, "Failed to enable DebugStickCraftingInSurvival", e);
            getServer().getPluginManager().disablePlugin(this);
        }
    }

    @Override
    public void onDisable() {
        unregisterRecipes();
        getLogger().info("DebugStickCraftingInSurvival has been disabled.");
    }

    private void initializePlugin() {
        loadConfiguration();
        initializeServices();
        registerRecipes();
        registerEventListeners();
        registerCommands();
    }

    private void loadConfiguration() {
        saveDefaultConfig();
        this.configurationManager = new ConfigurationManager(this);
        configurationManager.loadConfiguration();
    }

    private void initializeServices() {
        this.blockRestrictionService = new BlockRestrictionService(configurationManager);
    }

    private void registerRecipes() {
        this.recipeManager = new DebugStickRecipeManager(this, configurationManager);
        recipeManager.registerRecipes();
    }

    private void registerEventListeners() {
        DebugStickInteractionListener interactionListener =
                new DebugStickInteractionListener(blockRestrictionService, configurationManager);
        getServer().getPluginManager().registerEvents(interactionListener, this);
    }

    private void registerCommands() {
        ReloadCommand reloadCommand = new ReloadCommand(configurationManager);
        getCommand("debugstickreload").setExecutor(reloadCommand);
    }

    private void unregisterRecipes() {
        if (recipeManager != null) {
            recipeManager.unregisterRecipes();
        }
    }
}
