package github.nitespring.reindeer.core.datagen;

import github.nitespring.reindeer.ReindeerMod;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.List;
import java.util.Set;

@EventBusSubscriber(modid = ReindeerMod.MODID)
public class DataGenerators {


    @SubscribeEvent
    public static void gatherData(GatherDataEvent.Client event) {
        //DataGenerator generator = event.getGenerator();
        //PackOutput packOutput = generator.getPackOutput();
        //CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        event.createProvider(CustomItemModelProvider::new);

        event.createProvider((output, lookupProvider) -> new LootTableProvider(
                output, Set.of(), List.of(
                        new LootTableProvider.SubProviderEntry(CustomEntityLootTableProvider::new, LootContextParamSets.ENTITY)
        ), lookupProvider
        ));
        event.createProvider(CustomRecipeProvider.Runner::new);

    }
}
