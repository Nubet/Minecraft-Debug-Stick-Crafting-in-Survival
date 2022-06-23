package pl.tosupeprzystojniak.nubet.debugstickcraftinginsurvival;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.block.Block;
import java.util.Arrays;
public class PlayerInteract implements Listener{


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event){
        //player.inventory.getCurrentItem().getItem().equals(Blocks.COBBLESTONE.asItem()
        //event.setCancelled(true) anuluje akcje
        Player player = event.getPlayer();
        Action action = event.getAction();
        //Bukkit.broadcastMessage("You have clicked that block: " + clickedblock.getType());
        String excluded_blocks[] = {"COMPOSTER", "CACTUS", "KELP", "SUGAR_CANE", "GRASS_BLOCK", "ACACIA_LEAVES", "AZALEA_LEAVES","BIRCH_LEAVES","DARK_OAK_LEAVES","FLOWERING_AZALEA_LEAVES","JUNGLE_LEAVES","MANGROVE_LEAVES","OAK_LEAVES","SPRUCE_LEAVES", "BIG_DRIPLEAF", "BIG_DRIPLEAF_STEM","CHORUS_FLOWER", "CHORUS_FRUIT", "CHORUS_PLANT", "SEA_PICKLE","RESPAWN_ANCHOR", "ACACIA_TRAPDOOR", "AZALEA_TRAPDOOR","BIRCH_TRAPDOOR","DARK_OAK_TRAPDOOR","FLOWERING_AZALEA_TRAPDOOR","JUNGLE_TRAPDOOR","MANGROVE_TRAPDOOR","OAK_TRAPDOOR","SPRUCE_TRAPDOOR", "WARPED_TRAPDOOR", "CRIMSON_TRAPDOOR","TRIPWIRE_HOOK", "TURTLE_EGG", "BEE_NEST", "BEEHIVE", "CAKE","SWEET_BERRY_BUSH","BEETROOT_SEEDS", "MELON_SEEDS", "PUMPKIN_SEEDS", "WHEAT_SEEDS", "CHEST", "END_PORTAL_FRAME", "END_PORTAL", "PISTON", "PISTON_HEAD", "SNOW" };
        if(player.getInventory().getItemInMainHand().getType().equals(Material.DEBUG_STICK)){
            Block clickedblock  = event.getClickedBlock();
            //Bukkit.broadcastMessage("player used debug stick");
            if (clickedblock != null && action == Action.RIGHT_CLICK_BLOCK){
                //Bukkit.broadcastMessage("player rightclicked with debug stick");
                //Bukkit.broadcastMessage(clickedblock.toString());
                if(Arrays.asList(excluded_blocks).contains(clickedblock.getType().toString())){
                    event.setCancelled(true);
                }
            }
            else{
                //Bukkit.broadcastMessage("jakis kurwa blad");
            }
        }


    }
}
