package github.nitespring.reindeer.core.init;

import github.nitespring.reindeer.ReindeerMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemInit {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(
			 ReindeerMod.MODID);
	
	
	//Items


	public static final DeferredItem<Item> REINDEER_SPAWN_EGG = ITEMS.registerItem("reindeer_spawn_egg",
			(properties)-> new SpawnEggItem(properties.spawnEgg(EntityInit.REINDEER.get())));



	/*public static final DeferredItem<Item> REINDEER_SPAWN_EGG = registerSpawnEgg(EntityInit.REINDEER.get());

	public static final DeferredItem<Item> RAW_REINDEER_MEAT = registerFood(
			"raw_reindeer_meat", 64, FoodRegistry.RAW_REINDEER);

	public static final DeferredItem<Item> COOKED_REINDEER_MEAT = registerFood(
			"cooked_reindeer_meat", 64, FoodRegistry.COOKED_REINDEER);

	public static final DeferredItem<Item> REINDEER_ANTLER = registerSimpleItem(
			"reindeer_antler", 64);*/


	private static DeferredItem<Item> registerSpawnEgg(EntityType<?> entityType) {
		return ITEMS.registerItem(EntityType.getKey(entityType).withSuffix("_spawn_egg").toString(),
				(properties)-> new SpawnEggItem(properties.spawnEgg(entityType)));
	}

	private static DeferredItem<Item> registerFood(String key, int stackSize, FoodProperties food) {
		return registerItem(key, new Item.Properties().stacksTo(stackSize).food(food));
	}

	private static DeferredItem<Item> registerSimpleItem(String key, int stackSize) {
		return registerItem(key, new Item.Properties().stacksTo(stackSize));
	}

	private static DeferredItem<Item> registerItem(String key, Item.Properties properties) {
		return ITEMS.register(key,
				() -> new Item(properties));
	}

	/*public static final DeferredItem<Item> CANDY_FRAGMENT = ITEMS.register("candy_fragment",
			() -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).food(YuleFoods.CANDY)));*/

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB,
			ReindeerMod.MODID);

	public static final DeferredHolder<CreativeModeTab,CreativeModeTab> REINDEER_MOD_TAB = TABS.register("reindeer",
			() ->CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.reindeer.reindeer"))
					.icon(REINDEER_SPAWN_EGG.get()::getDefaultInstance)
					.withSearchBar().displayItems(
							(displayParams, output) -> {
								output.accept(REINDEER_SPAWN_EGG.get());
								/*output.accept(REINDEER_ANTLER.get());
								output.accept(RAW_REINDEER_MEAT.get());
								output.accept(COOKED_REINDEER_MEAT.get());*/
							}).build());


	public static void registerItems(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	public static void registerTabs(IEventBus eventBus) {
		TABS.register(eventBus);
	}
}
