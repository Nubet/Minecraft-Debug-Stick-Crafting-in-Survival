package pl.tosupeprzystojniak.nubet.debugstickcraftinginsurvival;


import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;

public final class DebugStickCraftingInSurvival extends JavaPlugin {
    @Override
    public void onEnable() {
        // Plugin startup logic
        System.out.println("DebugStickCraftingInSurvival has started");

        //ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(plugin: this, key:"potato"), )
        Bukkit.addRecipe(new Crafting(this));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        System.out.println("DebugStickCraftingInSurvival has STOPPED");
    }


}
