package net.gridith.reciperatings.decorations;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeDecorator;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;

public class RecipeRating implements EmiRecipeDecorator {
    @Override
    public void decorateRecipe(EmiRecipe emiRecipe, WidgetHolder widgetHolder) {
        if (emiRecipe.getId() != null) {
            // TODO figure out how configs can enable/disable these ratings
            EnableRatsEnrichmentRecipeRatings(emiRecipe, widgetHolder);
        }
    }

    private static void EnableRatsEnrichmentRecipeRatings(EmiRecipe emiRecipe, WidgetHolder widgetHolder) {
        if (!isValidCategoryCheck(emiRecipe)) return;

        final int ICON_SIZE = 12;
        final int TEXTURE_SIZE = ICON_SIZE*3;
        int ratingId = 4;
        final Map<String, Integer> RATING_IDS = Map.of(
                "sequencebreak", 0,
                "buildersshortcut", 1,
                "ratsdefault", 5,
                "artisansprocess", 7,
                "glorpquest", 8
        );

        for (var entry : RATING_IDS.entrySet()) {if (emiRecipe.getId().getPath().contains(entry.getKey())) {ratingId = entry.getValue();}}
        addRecipeRatingIcon(widgetHolder, emiRecipe, ratingId, TEXTURE_SIZE, ICON_SIZE );
    }

    public static boolean isValidCategoryCheck(EmiRecipe emiRecipe) {
        return !emiRecipe.getCategory().getId().toString().matches("create:block_cutting|emi:fuel|emi:world_interaction|emi:composting|emi:tag|emi:anvil_repairing|emi_loot:chest_loot|emi_loot:mob_drops|emi_loot:archaeology_drops|emi:info|oritech:bio_generator|oritech:fuel_generator|oritech:lava_generator|oritech:steam_engine|oritech:reactor|emi_enchanting:enchantments");
    }

    /**
     *
     * @param iconID Represents which icon on the spritesheet to render (aligned to the top left starting at 0), and what tooltips to use.
     * @param textureSize The resolution in pixels of the icons' sprite sheet.
     * @param iconSize The resolution in pixels of an individual icon.
     */
    public static void addRecipeRatingIcon(WidgetHolder widgetHolder, EmiRecipe emiRecipe, int iconID, int textureSize, int iconSize) {
        int iconsPerRow = textureSize/iconSize;
        int u = (iconID+iconsPerRow)%iconsPerRow * iconSize;
        int v = iconID / iconsPerRow * iconSize;
        widgetHolder.addTexture(ResourceLocation.fromNamespaceAndPath(
                "reciperatings", "textures/gui/ratings.png"),
                getPositionInCategory(widgetHolder, emiRecipe, iconSize, 'x'),
                getPositionInCategory(widgetHolder, emiRecipe, iconSize, 'y'),
                iconSize, iconSize,
                u, v,
                iconSize, iconSize,
                textureSize,
                textureSize);

        widgetHolder.addTooltipText(
                List.of(Component.translatable("reciperatings.icons."+iconID+".title"),
                        Component.translatable("reciperatings.icons."+iconID+".description")),
                getPositionInCategory(widgetHolder, emiRecipe, iconSize, 'x'),
                getPositionInCategory(widgetHolder, emiRecipe, iconSize, 'y'),
                iconSize, iconSize
        );
    }

    /**
     *
     * @param iconSize The resolution in pixels of an individual icon, used for offset.
     * @param axisChar The axis the method should return. Only lazily checks for x and assumes y for anything else
     * @return Depending on axisChar, returns the position an icon should render at, based on its category, in pixels.
     */
    private static int getPositionInCategory(WidgetHolder widgetHolder, EmiRecipe emiRecipe, int iconSize, char axisChar) {
        switch(emiRecipe.getCategory().getId().toString()){
            case "minecraft:smithing":
                if(axisChar=='x'){return -17 ;}
                return widgetHolder.getHeight() - iconSize+4;
            case "minecraft:stonecutting": // Align with recipe buttons
                if(axisChar=='x'){return widgetHolder.getWidth() +18 ;}
                else return -4;
            case "farmersdelight:cooking":
                if(axisChar=='x'){return widgetHolder.getWidth() + 5;}
                else return widgetHolder.getHeight() - 48;

            case "brewinandchewin:pouring": // Alight top left
                return 0;
            case "brewinandchewin:aging", "create:milling", "create:mixing", "create:packing", "createaddition:liquid_burning", "create:automatic_brewing": // Alight top right
                if(axisChar=='x'){return widgetHolder.getWidth() - iconSize;}
                else return 0;
            case "": // Align bottom left
                if(axisChar=='x'){return 0;}
                else return widgetHolder.getHeight() - iconSize;
            default: // Align bottom right
                if(axisChar=='x'){return widgetHolder.getWidth() - iconSize;}
                else return widgetHolder.getHeight() - iconSize;
        }
    }
}