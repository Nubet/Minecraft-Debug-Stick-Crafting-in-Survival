package pl.nubet.debugstickinsurvival.listener;

import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import pl.nubet.debugstickinsurvival.config.ConfigurationManager;
import pl.nubet.debugstickinsurvival.service.BlockRestrictionService;

/**
 * Handles player interactions with the Debug Stick and enforces usage restrictions.
 */
public class DebugStickInteractionListener implements Listener {

    private final BlockRestrictionService blockRestrictionService;
    private final ConfigurationManager configurationManager;

    public DebugStickInteractionListener(BlockRestrictionService blockRestrictionService,
                                         ConfigurationManager configurationManager) {
        this.blockRestrictionService = blockRestrictionService;
        this.configurationManager = configurationManager;
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (!configurationManager.areRestrictionsEnabled()) {
            return;
        }

        if (!isDebugStickRightClick(event)) {
            return;
        }

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null) {
            return;
        }

        if (!blockRestrictionService.isUsageAllowed(clickedBlock)) {
            handleRestrictedUsage(event);
        }
    }

    private boolean isDebugStickRightClick(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) {
            return false;
        }

        Player player = event.getPlayer();
        ItemStack item = player.getInventory().getItemInMainHand();

        return item != null && item.getType() == Material.DEBUG_STICK;
    }

    private void handleRestrictedUsage(PlayerInteractEvent event) {
        event.setCancelled(true);

        Player player = event.getPlayer();
        String message = blockRestrictionService.getRestrictionMessage();
        player.sendMessage(message);
    }
}

