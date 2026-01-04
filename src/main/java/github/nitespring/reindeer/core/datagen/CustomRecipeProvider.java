package github.nitespring.reindeer.core.datagen;

import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.core.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.registries.DeferredItem;

import java.util.List;
import java.util.concurrent.CompletableFuture;


public class CustomRecipeProvider extends RecipeProvider {

    public static final String SMELTING_RECIPE_ID = "from_smelting";
    public static final String SMOKING_RECIPE_ID = "from_smoking";
    public static final String CAMPFIRE_COOKING_RECIPE_ID = "from_campfire_cooking";
    public static final String BLASTING_RECIPE_ID = "from_blasting";

    public CustomRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
        super(provider, recipeOutput);
    }

    public static class Runner extends RecipeProvider.Runner {
        public Runner(PackOutput packOutput, CompletableFuture<HolderLookup.Provider> provider) {
            super(packOutput, provider);
        }

        @Override
        protected RecipeProvider createRecipeProvider(HolderLookup.Provider provider, RecipeOutput recipeOutput) {
            return new CustomRecipeProvider(provider, recipeOutput);
        }

        @Override
        public String getName() {
            return "Reindeer Recipes";
        }
    }


    @Override
    protected void buildRecipes() {
        createFoodCookingRecipes(ItemInit.RAW_REINDEER_MEAT,ItemInit.COOKED_REINDEER_MEAT,0.25f, 200);
    }


    protected void createFoodCookingRecipes(ItemLike itemIn, ItemLike itemOut, float exp, int cookingTime){
        createCustomSmeltingRecipe(itemIn,itemOut, exp, cookingTime, RecipeCategory.FOOD, RecipeSerializer.SMELTING_RECIPE, SmeltingRecipe::new, SMELTING_RECIPE_ID);
        createCustomSmeltingRecipe(itemIn,itemOut, exp, cookingTime / 2, RecipeCategory.FOOD, RecipeSerializer.SMOKING_RECIPE, SmokingRecipe::new, SMOKING_RECIPE_ID);
        createCustomSmeltingRecipe(itemIn,itemOut, exp, cookingTime * 3, RecipeCategory.FOOD, RecipeSerializer.CAMPFIRE_COOKING_RECIPE, CampfireCookingRecipe::new, CAMPFIRE_COOKING_RECIPE_ID);
    }

    protected <T extends AbstractCookingRecipe> void createCustomSmeltingRecipe(
            ItemLike itemIn, ItemLike itemOut, float exp, int cookingTime, RecipeCategory category,
            RecipeSerializer recipeSerializer, AbstractCookingRecipe.Factory<T> factory, String recipeId
    ){
        SimpleCookingRecipeBuilder.generic(
                        Ingredient.of(itemIn), category, itemOut, exp, cookingTime, recipeSerializer, factory)
                .group(getItemName(itemOut)).unlockedBy(getHasName(itemIn), has(itemIn))
                .save(output, ReindeerMod.MODID + ":" + getItemName(itemOut) + "_" + recipeId + "_" + getItemName(itemIn));
    }



}
