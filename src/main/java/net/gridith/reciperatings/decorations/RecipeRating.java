package net.gridith.reciperatings.decorations;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeDecorator;
import dev.emi.emi.api.widget.WidgetHolder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class RecipeRating implements EmiRecipeDecorator {
    @Override
    public void decorateRecipe(EmiRecipe emiRecipe, WidgetHolder widgetHolder) {
        final int RecipeRatingIconSize = 12;
        final int RecipeRatingTextureSize = RecipeRatingIconSize*3;
        if (emiRecipe.getId() != null) {
            if (emiRecipe.getId().getPath().contains("/") && !emiRecipe.getCategory().getId().toString().matches("create:block_cutting|emi:fuel|emi:world_interaction|emi:composting|emi:tag|emi:anvil_repairing|emi_loot:chest_loot|emi_loot:mob_drops|emi_loot:archaeology_drops|emi:info|oritech:bio_generator|oritech:fuel_generator|oritech:lava_generator|oritech:steam_engine|oritech:reactor|emi_enchanting:enchantments")){
                switch(emiRecipe.getId().getPath().split("/")[0]){
                    case "sequencebreak":
                        addRecipeRatingIcon(widgetHolder, emiRecipe,
                                0,
                                RecipeRatingTextureSize,
                                RecipeRatingIconSize
                        ); break;
                    case "buildersshortcut":
                        addRecipeRatingIcon(widgetHolder, emiRecipe,
                                1,
                                RecipeRatingTextureSize,
                                RecipeRatingIconSize
                        ); break;
                    case "ratsdefault":
                        addRecipeRatingIcon(widgetHolder, emiRecipe,
                                5,
                                RecipeRatingTextureSize,
                                RecipeRatingIconSize
                        ); break;
                    case "artisansprocess":
                        addRecipeRatingIcon(widgetHolder, emiRecipe,
                                7,
                                RecipeRatingTextureSize,
                                RecipeRatingIconSize
                        ); break;
                    case "glorpquest":
                        addRecipeRatingIcon(widgetHolder, emiRecipe,
                                8,
                                RecipeRatingTextureSize,
                                RecipeRatingIconSize
                        ); break;
                }
            }else{ // Vanilla Default
                addRecipeRatingIcon(widgetHolder, emiRecipe,
                        4,
                        RecipeRatingTextureSize,
                        RecipeRatingIconSize
                );
            }
        }
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
                return -4;
            case "minecraft:stonecutting": // Align with recipe buttons
                if(axisChar=='x'){return widgetHolder.getWidth() +19 ;}
                else return -4;
            case "create:automatic_brewing", "farmersdelight:cooking":
                if(axisChar=='x'){return widgetHolder.getWidth() + 5;}
                else return widgetHolder.getHeight() - 16;

            case "brewinandchewin:pouring": // Alight top left
                return 0;
            case "brewinandchewin:aging", "create:milling", "create:mixing", "create:packing", "createaddition:liquid_burning": // Alight top right
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