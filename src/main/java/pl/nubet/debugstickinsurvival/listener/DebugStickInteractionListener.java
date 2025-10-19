package pl.nubet.debugstickinsurvival.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Slab;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import pl.nubet.debugstickinsurvival.config.ConfigurationManager;
import pl.nubet.debugstickinsurvival.service.BlockRestrictionService;
import pl.nubet.debugstickinsurvival.service.DebugStickCapabilityService;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Handles player interactions with the Debug Stick and enforces usage restrictions.
 * Instead of blocking the entire action, it allows property changes but reverts
 * restricted capabilities (waterlogging, slab doubling).
 */
public class DebugStickInteractionListener implements Listener {

    private final BlockRestrictionService blockRestrictionService;
    private final DebugStickCapabilityService capabilityService;
    private final ConfigurationManager configurationManager;

    // Store block states before Debug Stick interaction
    private final Map<UUID, BlockSnapshot> blockSnapshots = new HashMap<>();

    public DebugStickInteractionListener(BlockRestrictionService blockRestrictionService,
                                         DebugStickCapabilityService capabilityService,
                                         ConfigurationManager configurationManager) {
        this.blockRestrictionService = blockRestrictionService;
        this.capabilityService = capabilityService;
        this.configurationManager = configurationManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isDebugStickRightClick(event)) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        Player player = event.getPlayer();

        // Check block type restrictions (complete block)
        if (configurationManager.areRestrictionsEnabled()) {
            if (!blockRestrictionService.isUsageAllowed(clickedBlock)) {
                handleBlockRestriction(event, player);
                return;
            }
        }

        // For capability restrictions, capture state before change
        if (configurationManager.areCapabilityRestrictionsEnabled()) {
            captureBlockState(player, clickedBlock);
        }
    }

    /**
     * Monitor block changes after Debug Stick interaction
     * Priority MONITOR means this runs AFTER the Debug Stick has changed the block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerInteractMonitor(PlayerInteractEvent event) {
        if (!isDebugStickRightClick(event)) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Skip if no snapshot (means restrictions are disabled or block was completely blocked)
        if (!blockSnapshots.containsKey(playerId)) {
            return;
        }

        // Schedule check on next tick (after Debug Stick has modified the block)
        new BukkitRunnable() {
            @Override
            public void run() {
                BlockSnapshot snapshot = blockSnapshots.remove(playerId);
                if (snapshot == null) {
                    return;
                }

                Block block = snapshot.block;
                BlockData oldData = snapshot.blockData;
                BlockData newData = block.getBlockData();

                // Check if restricted properties were changed
                boolean revertNeeded = false;
                boolean waterlogChanged = false;
                boolean slabChanged = false;

                // Check waterlogging change
                if (configurationManager.isWaterloggingRestrictionEnabled()) {
                    if (oldData instanceof Waterlogged oldWaterlogged && newData instanceof Waterlogged newWaterlogged) {
                        if (oldWaterlogged.isWaterlogged() != newWaterlogged.isWaterlogged()) {
                            if (!player.hasPermission("debugstickcs.waterlog")) {
                                // Revert waterlogged state
                                newWaterlogged.setWaterlogged(oldWaterlogged.isWaterlogged());
                                revertNeeded = true;
                                waterlogChanged = true;
                            }
                        }
                    }
                }

                // Check slab doubling change
                if (configurationManager.isSlabDoublingRestrictionEnabled()) {
                    if (oldData instanceof Slab oldSlab && newData instanceof Slab newSlab) {
                        // Check if trying to change to DOUBLE
                        if (oldSlab.getType() != Slab.Type.DOUBLE && newSlab.getType() == Slab.Type.DOUBLE) {
                            if (!player.hasPermission("debugstickcs.doubleslab")) {
                                // Revert slab type
                                newSlab.setType(oldSlab.getType());
                                revertNeeded = true;
                                slabChanged = true;
                            }
                        }
                    }
                }

                // Apply reverted data and notify player
                if (revertNeeded) {
                    block.setBlockData(newData, false);

                    if (waterlogChanged) {
                        player.sendMessage(configurationManager.getWaterlogRestrictionMessage());
                    }
                    if (slabChanged) {
                        player.sendMessage(configurationManager.getSlabDoublingRestrictionMessage());
                    }
                }
            }
        }.runTaskLater(event.getPlayer().getServer().getPluginManager().getPlugin("DebugStickCraftingInSurvival"), 1L);
    }

    private boolean isDebugStickRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return false;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        return item != null && item.getType() == Material.DEBUG_STICK;
    }

    private void captureBlockState(Player player, Block block) {
        BlockData blockData = block.getBlockData().clone();
        blockSnapshots.put(player.getUniqueId(), new BlockSnapshot(block, blockData));
    }

    private void handleBlockRestriction(PlayerInteractEvent event, Player player) {
        event.setCancelled(true);
        String message = blockRestrictionService.getRestrictionMessage();
        player.sendMessage(message);
    }

    /**
     * Stores a snapshot of block state before Debug Stick interaction
     */
    private static class BlockSnapshot {
        final Block block;
        final BlockData blockData;

        BlockSnapshot(Block block, BlockData blockData) {
            this.block = block;
            this.blockData = blockData;
        }
    }
}
