package pl.tosupeprzystojniak.nubet.debugstickcraftinginsurvival;


import org.bukkit.NamespacedKey;
import org.bukkit.event.block.Action;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
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

public final class DebugStickCraftingInSurvival extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.broadcastMessage("DebugStickCraftingInSurvival has started");
        Bukkit.addRecipe(new Crafting(this));
        getServer().getPluginManager().registerEvents(new PlayerInteract(), this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        Bukkit.broadcastMessage("DebugStickCraftingInSurvival has STOPPED");
    }


}



