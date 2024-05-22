package pl.tosupeprzystojniak.nubet.debugstickcraftinginsurvival;

import java.util.List;
import java.util.Arrays;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.event.EventHandler;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.plugin.Plugin;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import org.bukkit.Material;

public final class DebugStickCraftingInSurvival extends JavaPlugin {

    FileConfiguration config = getConfig();

    @Override
    public void onEnable() {
        // Plugin startup logic
        config.options().copyDefaults(true);
        saveConfig();
        Bukkit.getLogger().info(config.getString("excluded_blocks"));

        Bukkit.broadcastMessage("DebugStickCraftingInSurvival has started");
        Bukkit.addRecipe(new Crafting(this));
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);


    }

    public FileConfiguration getConfigFile() {
        return config;
    }

    public class PlayerInteract implements Listener {
        @EventHandler
        public void onPlayerInteract(PlayerInteractEvent event) {
            Player player = event.getPlayer();
            Action action = event.getAction();
            //Bukkit.broadcastMessage("You have clicked that block: " + clickedblock.getType());
            //String excluded_blocks[] = {"COMPOSTER", "CACTUS", "KELP", "SUGAR_CANE", "GRASS_BLOCK", "ACACIA_LEAVES", "AZALEA_LEAVES", "BIRCH_LEAVES", "DARK_OAK_LEAVES", "FLOWERING_AZALEA_LEAVES", "JUNGLE_LEAVES", "MANGROVE_LEAVES", "OAK_LEAVES", "SPRUCE_LEAVES", "BIG_DRIPLEAF", "BIG_DRIPLEAF_STEM", "CHORUS_FLOWER", "CHORUS_FRUIT", "CHORUS_PLANT", "SEA_PICKLE", "RESPAWN_ANCHOR", "ACACIA_TRAPDOOR", "AZALEA_TRAPDOOR", "BIRCH_TRAPDOOR", "DARK_OAK_TRAPDOOR", "FLOWERING_AZALEA_TRAPDOOR", "JUNGLE_TRAPDOOR", "MANGROVE_TRAPDOOR", "OAK_TRAPDOOR", "SPRUCE_TRAPDOOR", "WARPED_TRAPDOOR", "CRIMSON_TRAPDOOR", "TRIPWIRE_HOOK", "TURTLE_EGG", "BEE_NEST", "BEEHIVE", "CAKE", "SWEET_BERRY_BUSH", "BEETROOT_SEEDS", "MELON_SEEDS", "PUMPKIN_SEEDS", "WHEAT_SEEDS", "CHEST", "END_PORTAL_FRAME", "END_PORTAL", "PISTON", "PISTON_HEAD", "SNOW", "SCULK_SENSOR", "SCULK_SCHRIEKER", "ACACIA_SLAB", "ANDESITE_SLAB", "BIRCH_SLAB", "BLACKSTONE_SLAB", "BRICK_SLAB", "COBBLED_DEEPSLATE_SLAB", "COBBLESTONE_SLAB", "CRIMSON_SLAB", "CUT_COPPER_SLAB", "CUT_RED_SANDSTONE_SLAB", "CUT_SANDSTONE_SLAB", "DARK_OAK_SLAB", "DARK_PRISMARINE_SLAB", "DEEPSLATE_TITLE_SLAB", "DIORITE_SLAB", "END_STONE_BRICK_SLAB", "EXPOSED_CUT_COPPER_SLAB", "GRANITE_SLAB", "JUNGLE_SLAB", "MANGROVE_SLAB", "MOSSY_STONE_BRICK_SLAB", "MUD_BRICK_SLAB", "NETHER_BRICK_SLAB", "OAK_SLAB", "OXIDIZED_CUT_COPPER_SLAB", "PETRIFIED_OAK_SLAB", "POLISHED_ANDESITE_SLAB", "POLISHED_BLACKSTONE_BRICK_SLAB", "POLISHED_BLACKSTONE_SLAB", "POLISHED_DEEPSLATE_SLAB", "POLISHED_DIORITE_SLAB", "PRISMARINE_BRICK_SLAB", "PRISMARINE_SLAB", "PURPUR_SLAB", "QUARTZ_SLAB", "RED_NETHER_BRICK_SLAB", "RED_SANDSTONE_SLAB", "SANDSTONE_SLAB", "SMOOTH_QUARTZ_SLAB", "SMOOTH_RED_SANDSTONE_SLAB", "SMOOTH_STONE_SLAB", "SPRUCE_SLAB", "STONE_BRICK_SLAB", "STONE_SLAB", "WARPED_SLAB", "WAXED_CUT_COPPER_SLAB", "WAXED_EXPOSED_CUT_COPPER_SLAB", "WAXED_OXIDIZED_CUT_COPPER_SLAB", "WAXED_WEATHERED_CUT_COPPER_SLAB", "WEATHERED_CUT_COPPER_SLAB"};
            List<?> excluded_blocks2 = config.getList("excluded_blocks");
            String[] excluded_blocks = excluded_blocks2.stream().toArray(String[]::new);

            if (player.getInventory().getItemInMainHand().getType().equals(Material.DEBUG_STICK)) {
                Block clickedblock = event.getClickedBlock();
                //Bukkit.broadcastMessage("player used debug stick");
                if (clickedblock != null && action == Action.RIGHT_CLICK_BLOCK) {
                    //Bukkit.broadcastMessage("player rightclicked with debug stick");
                    //Bukkit.broadcastMessage(clickedblock.toString());
                    if (Arrays.asList(excluded_blocks).contains(clickedblock.getType().toString())) {
                        event.setCancelled(true);
                        player.sendMessage("You cant use debug stick at this type of block!");
                    }
                } else {
                    //Bukkit.broadcastMessage("jakis kurwa blad");
                }
            }

        }
    }

}



