package pl.nubet.debugstickinsurvival.recipe;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;
import pl.nubet.debugstickinsurvival.config.ConfigurationManager;

import java.util.logging.Level;

/**
 * Manages the registration and unregistration of Debug Stick crafting recipes.
 */
public class DebugStickRecipeManager
{

    private static final String RECIPE_KEY = "debug_stick_crafting";

    private final JavaPlugin plugin;
    private final ConfigurationManager configurationManager;
    private NamespacedKey recipeKey;

    public DebugStickRecipeManager(JavaPlugin plugin, ConfigurationManager configurationManager) {
        this.plugin = plugin;
        this.configurationManager = configurationManager;
    }

    public void registerRecipes() {
        if (!configurationManager.isCraftingEnabled()) {
            plugin.getLogger().info("Debug Stick crafting is disabled in configuration.");
            return;
        }

        try {
            this.recipeKey = new NamespacedKey(plugin, RECIPE_KEY);
            ShapedRecipe recipe = createDebugStickRecipe();
            Bukkit.addRecipe(recipe);
            plugin.getLogger().info("Debug Stick crafting recipe registered successfully.");
        } catch (Exception e) {
            plugin.getLogger().log(Level.SEVERE, "Failed to register Debug Stick recipe", e);
        }
    }

    public void unregisterRecipes() {
        if (recipeKey != null) {
            Bukkit.removeRecipe(recipeKey);
            plugin.getLogger().info("Debug Stick crafting recipe unregistered.");
        }
    }

    private ShapedRecipe createDebugStickRecipe() {
        ItemStack debugStick = new ItemStack(Material.DEBUG_STICK, 1);
        ShapedRecipe recipe = new ShapedRecipe(recipeKey, debugStick);

        recipe.shape(
            "  S",
            "NP ",
            "PN "
        );

        recipe.setIngredient('N', Material.NETHERITE_INGOT);
        recipe.setIngredient('S', Material.NETHER_STAR);
        recipe.setIngredient('P', Material.STICK);

        return recipe;
    }
}

