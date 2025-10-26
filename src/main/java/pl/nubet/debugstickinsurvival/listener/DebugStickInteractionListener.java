package pl.nubet.debugstickinsurvival.listener;

import org.bukkit.Material;
import org.bukkit.World;
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
public class DebugStickInteractionListener implements Listener
{

    private final BlockRestrictionService blockRestrictionService;
    private final DebugStickCapabilityService capabilityService;
    private final ConfigurationManager configurationManager;

    // Store block states before Debug Stick interaction
    private final Map<UUID, BlockSnapshot> blockSnapshots = new HashMap<>();

    // Cooldown system to prevent spam clicking (150ms = 3 ticks)
    private final Map<UUID, Long> playerCooldowns = new HashMap<>();
    private static final long COOLDOWN_MS = 150;

    public DebugStickInteractionListener(BlockRestrictionService blockRestrictionService,
        DebugStickCapabilityService capabilityService,
        ConfigurationManager configurationManager) {
        this.blockRestrictionService = blockRestrictionService;
        this.capabilityService = capabilityService;
        this.configurationManager = configurationManager;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!isDebugStickClick(event)) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        Player player = event.getPlayer();
        UUID playerId = player.getUniqueId();

        // Check cooldown
        if (isOnCooldown(playerId)) {
            event.setCancelled(true);
            return;
        }

        // Set cooldown
        setCooldown(playerId);

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
        if (!isDebugStickClick(event)) {
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
        new BukkitRunnable()
        {
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
                String restrictionMessage = null;

                // Check waterlogging change
                if (shouldCheckWaterlogging(oldData, newData)) {
                    WaterlogPermissionResult waterlogResult = checkWaterlogPermission(player, block.getWorld());
                    if (!waterlogResult.hasPermission()) {
                        revertWaterlogging((Waterlogged) oldData, (Waterlogged) newData);
                        revertNeeded = true;
                        restrictionMessage = waterlogResult.getMessage();
                    }
                }

                // Check slab doubling change
                if (shouldCheckSlabDoubling(oldData, newData)) {
                    if (!player.hasPermission("debugstickcs.doubleslab")) {
                        revertSlabDoubling((Slab) oldData, (Slab) newData);
                        revertNeeded = true;
                        restrictionMessage = configurationManager.getSlabDoublingRestrictionMessage();
                    }
                }

                // Apply reverted data and notify player
                if (revertNeeded) {
                    block.setBlockData(newData, false);
                    if (restrictionMessage != null) {
                        player.sendMessage(restrictionMessage);
                    }
                }
            }
        }.runTaskLater(event.getPlayer().getServer().getPluginManager().getPlugin("DebugStickCraftingInSurvival"), 1L);
    }

    private boolean isDebugStickClick(PlayerInteractEvent event) {
        Action action = event.getAction();
        if (action != Action.RIGHT_CLICK_BLOCK && action != Action.LEFT_CLICK_BLOCK) {
            return false;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        return item != null && item.getType() == Material.DEBUG_STICK;
    }

    private boolean isOnCooldown(UUID playerId) {
        if (!playerCooldowns.containsKey(playerId)) {
            return false;
        }

        long lastUse = playerCooldowns.get(playerId);
        long currentTime = System.currentTimeMillis();
        return (currentTime - lastUse) < COOLDOWN_MS;
    }

    private void setCooldown(UUID playerId) {
        playerCooldowns.put(playerId, System.currentTimeMillis());
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

    private boolean isNetherWorld(World world) {
        return world.getEnvironment() == org.bukkit.World.Environment.NETHER;
    }

    private boolean shouldCheckWaterlogging(BlockData oldData, BlockData newData) {
        return configurationManager.isWaterloggingRestrictionEnabled() &&
            oldData instanceof Waterlogged oldWaterlogged && newData instanceof Waterlogged newWaterlogged &&
            oldWaterlogged.isWaterlogged() != newWaterlogged.isWaterlogged();
    }

    private boolean shouldCheckSlabDoubling(BlockData oldData, BlockData newData) {
        return configurationManager.isSlabDoublingRestrictionEnabled() &&
            oldData instanceof Slab oldSlab && newData instanceof Slab newSlab &&
            oldSlab.getType() != Slab.Type.DOUBLE && newSlab.getType() == Slab.Type.DOUBLE;
    }

    private WaterlogPermissionResult checkWaterlogPermission(Player player, World world) {
        boolean isNether = isNetherWorld(world);
        boolean hasPermission;

        if (isNether && configurationManager.isNetherWaterloggingRestrictionEnabled()) {
            hasPermission = player.hasPermission("debugstickcs.waterlog.nether");
            String message = configurationManager.getNetherWaterlogRestrictionMessage();
            return hasPermission ? WaterlogPermissionResult.allowed() : WaterlogPermissionResult.denied(message);
        }
        else {
            hasPermission = player.hasPermission("debugstickcs.waterlog");
            String message = configurationManager.getWaterlogRestrictionMessage();
            return hasPermission ? WaterlogPermissionResult.allowed() : WaterlogPermissionResult.denied(message);
        }
    }

    private void revertWaterlogging(Waterlogged oldWaterlogged, Waterlogged newWaterlogged) {
        newWaterlogged.setWaterlogged(oldWaterlogged.isWaterlogged());
    }

    private void revertSlabDoubling(Slab oldSlab, Slab newSlab) {
        newSlab.setType(oldSlab.getType());
    }

    /**
     * Stores a snapshot of block state before Debug Stick interaction
     */
    private static class BlockSnapshot
    {
        final Block block;
        final BlockData blockData;

        BlockSnapshot(Block block, BlockData blockData) {
            this.block = block;
            this.blockData = blockData;
        }
    }

    private static class WaterlogPermissionResult
    {
        private final boolean allowed;
        private final String message;

        private WaterlogPermissionResult(boolean allowed, String message) {
            this.allowed = allowed;
            this.message = message;
        }

        public static WaterlogPermissionResult allowed() {
            return new WaterlogPermissionResult(true, null);
        }

        public static WaterlogPermissionResult denied(String message) {
            return new WaterlogPermissionResult(false, message);
        }

        public boolean hasPermission() {
            return allowed;
        }

        public String getMessage() {
            return message;
        }
    }
}
