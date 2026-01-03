package github.nitespring.reindeer.core.datagen;

import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.core.init.ItemInit;
import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.data.PackOutput;

public class CustomItemModelProvider extends ModelProvider {
    public CustomItemModelProvider(PackOutput output) {
        super(output, ReindeerMod.MODID);
    }
    @Override
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {

        itemModels.generateFlatItem(ItemInit.REINDEER_SPAWN_EGG.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemInit.RAW_REINDEER_MEAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemInit.COOKED_REINDEER_MEAT.get(), ModelTemplates.FLAT_ITEM);
        itemModels.generateFlatItem(ItemInit.REINDEER_ANTLER.get(), ModelTemplates.FLAT_ITEM);

    }


}
