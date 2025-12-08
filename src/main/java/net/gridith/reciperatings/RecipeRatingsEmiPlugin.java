package net.gridith.reciperatings;

import dev.emi.emi.api.EmiEntrypoint;
import dev.emi.emi.api.EmiPlugin;
import dev.emi.emi.api.EmiRegistry;
import net.gridith.reciperatings.decorations.RecipeRating;

@EmiEntrypoint
public final class RecipeRatingsEmiPlugin implements EmiPlugin {
    @Override
    public void register(EmiRegistry emiRegistry){
        emiRegistry.addRecipeDecorator(new RecipeRating());
    }
}
