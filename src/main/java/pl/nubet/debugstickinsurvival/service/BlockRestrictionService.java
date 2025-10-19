package pl.nubet.debugstickinsurvival.service;

import org.bukkit.Material;
import org.bukkit.block.Block;
import pl.nubet.debugstickinsurvival.config.ConfigurationManager;

/**
 * Service responsible for determining if Debug Stick usage is restricted on specific blocks.
 */
public class BlockRestrictionService
{

    private final ConfigurationManager configurationManager;

    public BlockRestrictionService(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public boolean isUsageAllowed(Block block) {
        if (block == null) {
            return false;
        }

        if (!configurationManager.areRestrictionsEnabled()) {
            return true;
        }

        return !isBlockRestricted(block.getType());
    }

    public boolean isBlockRestricted(Material material) {
        return configurationManager.getExcludedBlocks().contains(material);
    }

    public String getRestrictionMessage() {
        return configurationManager.getRestrictionMessage();
    }
}

