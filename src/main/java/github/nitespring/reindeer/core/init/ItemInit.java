package github.nitespring.reindeer.core.init;

import github.nitespring.reindeer.ReindeerMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Function;

public class ItemInit {
	public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(
			 ReindeerMod.MODID);
	
	
	//Items


	//public static final DeferredItem<Item> REINDEER_SPAWN_EGG = ITEMS.registerItem("reindeer_spawn_egg",
	//		(properties)-> new SpawnEggItem(properties.spawnEgg(EntityInit.REINDEER.get())));



	public static final DeferredItem<Item> REINDEER_SPAWN_EGG = registerSpawnEgg(EntityInit.REINDEER);

	public static final DeferredItem<Item> RAW_REINDEER_MEAT = registerFood(
			"raw_reindeer_meat", 64, FoodRegistry.RAW_REINDEER);

	public static final DeferredItem<Item> COOKED_REINDEER_MEAT = registerFood(
			"cooked_reindeer_meat", 64, FoodRegistry.COOKED_REINDEER);

	public static final DeferredItem<Item> REINDEER_ANTLER = registerSimpleItem(
			"reindeer_antler", 64);

	private static <T extends Entity> DeferredItem<Item> registerSpawnEgg(DeferredHolder<EntityType<?>,EntityType<T>> entityType) {
		return registerItem(
				 entityType.getId().getPath() + "_spawn_egg",
				(properties)-> new SpawnEggItem(properties.spawnEgg(entityType.get())));
	}

	private static DeferredItem<Item> registerFood(String key, int stackSize, FoodProperties food) {
		return registerItem(key,
				(properties) -> new Item(properties.stacksTo(stackSize).food(food)));
	}

	private static DeferredItem<Item> registerSimpleItem(String key, int stackSize) {
		return registerItem(key,
				(properties) -> new Item(properties.stacksTo(stackSize)));
	}

	private static <I extends Item> DeferredItem<I> registerItem(String key, Function<Item.Properties, ? extends I> func) {
		return ITEMS.registerItem(key, func);
	}


	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB,
			ReindeerMod.MODID);

	public static final DeferredHolder<CreativeModeTab,CreativeModeTab> REINDEER_MOD_TAB = TABS.register("reindeer",
			() ->CreativeModeTab.builder()
					.title(Component.translatable("itemGroup.reindeer.reindeer"))
					.icon(REINDEER_ANTLER.get()::getDefaultInstance)
					.withSearchBar().displayItems(
							(displayParams, output) -> {
								output.accept(REINDEER_SPAWN_EGG.get());
								output.accept(REINDEER_ANTLER.get());
								output.accept(RAW_REINDEER_MEAT.get());
								output.accept(COOKED_REINDEER_MEAT.get());
							}).build());


	public static void registerItems(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	public static void registerTabs(IEventBus eventBus) {
		TABS.register(eventBus);
	}
}
