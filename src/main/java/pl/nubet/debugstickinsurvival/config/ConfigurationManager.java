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
public class ConfigurationManager
{

    private final JavaPlugin plugin;
    private FileConfiguration config;
    private Set<Material> excludedBlocks;
    private String restrictionMessage;
    private String waterlogRestrictionMessage;
    private String netherWaterlogRestrictionMessage;
    private String slabDoublingRestrictionMessage;
    private boolean enableCrafting;
    private boolean enableRestrictions;
    private boolean enableCapabilityRestrictions;
    private boolean enableWaterloggingRestriction;
    private boolean enableNetherWaterloggingRestriction;
    private boolean enableSlabDoublingRestriction;

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
        this.waterlogRestrictionMessage = config.getString("messages.waterlog_restriction",
            "§cYou don't have permission to waterlog blocks with the Debug Stick!");
        this.netherWaterlogRestrictionMessage = config.getString("messages.nether_waterlog_restriction",
            "§cYou don't have permission to waterlog blocks in the Nether with the Debug Stick!");
        this.slabDoublingRestrictionMessage = config.getString("messages.slab_doubling_restriction",
            "§cYou don't have permission to create double slabs with the Debug Stick!");
    }

    private void loadFeatureToggles() {
        this.enableCrafting = config.getBoolean("features.enable_crafting", true);
        this.enableRestrictions = config.getBoolean("features.enable_restrictions", true);
        this.enableCapabilityRestrictions = config.getBoolean("features.enable_capability_restrictions", true);
        this.enableWaterloggingRestriction = config.getBoolean("features.restrict_waterlogging", true);
        this.enableNetherWaterloggingRestriction = config.getBoolean("features.restrict_nether_waterlogging", true);
        this.enableSlabDoublingRestriction = config.getBoolean("features.restrict_slab_doubling", true);
    }

    public Set<Material> getExcludedBlocks() {
        return new HashSet<>(excludedBlocks);
    }

    public String getRestrictionMessage() {
        return restrictionMessage;
    }

    public String getWaterlogRestrictionMessage() {
        return waterlogRestrictionMessage;
    }

    public String getNetherWaterlogRestrictionMessage() {
        return netherWaterlogRestrictionMessage;
    }

    public String getSlabDoublingRestrictionMessage() {
        return slabDoublingRestrictionMessage;
    }

    public boolean isCraftingEnabled() {
        return enableCrafting;
    }

    public boolean areRestrictionsEnabled() {
        return enableRestrictions;
    }

    public boolean areCapabilityRestrictionsEnabled() {
        return enableCapabilityRestrictions;
    }

    public boolean isWaterloggingRestrictionEnabled() {
        return enableWaterloggingRestriction;
    }

    public boolean isNetherWaterloggingRestrictionEnabled() {
        return enableNetherWaterloggingRestriction;
    }

    public boolean isSlabDoublingRestrictionEnabled() {
        return enableSlabDoublingRestriction;
    }

    public void reload() {
        loadConfiguration();
    }
}
