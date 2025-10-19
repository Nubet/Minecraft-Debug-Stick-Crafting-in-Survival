package pl.nubet.debugstickinsurvival.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

/**
 * Manages plugin configuration and provides access to configuration values.
 */
public class ConfigurationManager {

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private Set<Material> excludedBlocks;
    private String restrictionMessage;
    private boolean enableCrafting;
    private boolean enableRestrictions;

    public ConfigurationManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.excludedBlocks = new HashSet<>();
    }

    public void loadConfiguration() {
        plugin.reloadConfig();
        this.config = plugin.getConfig();

        loadExcludedBlocks();
        loadMessages();
        loadFeatureToggles();

        plugin.getLogger().info("Configuration loaded successfully. Excluded blocks: " + excludedBlocks.size());
    }

    private void loadExcludedBlocks() {
        List<String> blockNames = config.getStringList("excluded_blocks");

        this.excludedBlocks = blockNames.stream()
                .map(this::parseMaterial)
                .filter(material -> material != null)
                .collect(Collectors.toSet());
    }

    private Material parseMaterial(String materialName) {
        try {
            return Material.valueOf(materialName.toUpperCase());
        } catch (IllegalArgumentException e) {
            plugin.getLogger().log(Level.WARNING,
                    "Invalid material in config: " + materialName + ". Skipping...");
            return null;
        }
    }

    private void loadMessages() {
        this.restrictionMessage = config.getString("messages.restriction",
                "§cYou cannot use the Debug Stick on this type of block!");
    }

    private void loadFeatureToggles() {
        this.enableCrafting = config.getBoolean("features.enable_crafting", true);
        this.enableRestrictions = config.getBoolean("features.enable_restrictions", true);
    }

    public Set<Material> getExcludedBlocks() {
        return new HashSet<>(excludedBlocks);
    }

    public String getRestrictionMessage() {
        return restrictionMessage;
    }

    public boolean isCraftingEnabled() {
        return enableCrafting;
    }

    public boolean areRestrictionsEnabled() {
        return enableRestrictions;
    }

    public void reload() {
        loadConfiguration();
    }
}

