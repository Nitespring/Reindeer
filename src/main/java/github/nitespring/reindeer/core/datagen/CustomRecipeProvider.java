package github.nitespring.reindeer.core.datagen;

import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.core.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
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

        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TRANSPORTATION, ItemInit.REINDEER_SADDLE)
                .pattern("ADA")
                .pattern("WSW")
                .pattern("LIL")
                .define('A', Items.AMETHYST_SHARD)
                .define('D', Items.DIAMOND)
                .define('W', Items.RED_WOOL)
                .define('S', Items.SADDLE)
                .define('I', Items.IRON_INGOT)
                .define('L', Items.LEATHER)
                .unlockedBy("has_saddle", has(Items.SADDLE))
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .unlockedBy("has_diamond", has(Items.DIAMOND))
                .unlockedBy("has_amethyst_shard", has(Items.AMETHYST_SHARD))
                .group(getItemName(ItemInit.REINDEER_SADDLE))
                .save(output);
        ShapedRecipeBuilder.shaped(this.registries.lookupOrThrow(Registries.ITEM), RecipeCategory.TRANSPORTATION, ItemInit.BUTTER_COOKIE)
                .pattern(" S ")
                .pattern("WMW")
                .pattern(" S ")
                .define('W', Items.WHEAT)
                .define('M', Items.MILK_BUCKET)
                .define('S', Items.SUGAR)
                .unlockedBy("has_wheat", has(Items.WHEAT))
                .unlockedBy("has_milk_bucket", has(Items.MILK_BUCKET))
                .unlockedBy("has_sugar", has(Items.SUGAR))
                .group(getItemName(ItemInit.BUTTER_COOKIE))
                .save(output);
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
