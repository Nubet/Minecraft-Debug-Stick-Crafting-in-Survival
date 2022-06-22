package pl.tosupeprzystojniak.nubet.debugstickcraftinginsurvival;


import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.Plugin;

public final class Crafting extends ShapedRecipe {
    public Crafting(Plugin plugin) {
        super(new NamespacedKey(plugin, "leathercrafting.craft"), new ItemStack(Material.DEBUG_STICK, 1));
        this.shape(
                "  s",
                "np ",
                "pn ");
        setIngredient('n', Material.NETHERITE_INGOT);
        setIngredient('s', Material.NETHER_STAR);
        setIngredient('p', Material.STICK);
    }

}
