package pl.nubet.debugstickinsurvival.service;

import org.bukkit.block.Block;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;
import pl.nubet.debugstickinsurvival.config.ConfigurationManager;

/**
 * Service responsible for providing information about Debug Stick capabilities.
 */
public class DebugStickCapabilityService {

    private final ConfigurationManager configurationManager;

    public DebugStickCapabilityService(ConfigurationManager configurationManager) {
        this.configurationManager = configurationManager;
    }

    public boolean isWaterloggable(Block block) {
        return block.getBlockData() instanceof Waterlogged;
    }

    public boolean isSlab(Block block) {
        return block.getBlockData() instanceof Slab;
    }
}
