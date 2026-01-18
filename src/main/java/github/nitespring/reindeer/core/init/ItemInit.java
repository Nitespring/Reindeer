package github.nitespring.reindeer.core.init;

import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.common.ReindeerSaddle;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.equipment.Equippable;
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

	public static final DeferredItem<Item> BUTTER_COOKIE = registerFood(
			"butter_cookie", 64, FoodRegistry.BUTTER_COOKIE);

	public static final DeferredItem<Item> REINDEER_ANTLER = registerSimpleItem(
			"reindeer_antler", 64);

	public static final DeferredItem<ReindeerSaddle> REINDEER_SADDLE = registerItem(
			"reindeer_saddle", (properties) -> new ReindeerSaddle(properties
					.stacksTo(1)
					.rarity(Rarity.RARE)
					.attributes(ItemAttributeModifiers.builder()
							.add(Attributes.GRAVITY, new AttributeModifier(
									Identifier.fromNamespaceAndPath(ReindeerMod.MODID,"reindeer_saddle_gravity_modifier"),
									-0.04f,
									AttributeModifier.Operation.ADD_VALUE),
								EquipmentSlotGroup.SADDLE)
							.add(Attributes.FALL_DAMAGE_MULTIPLIER, new AttributeModifier(
									Identifier.fromNamespaceAndPath(ReindeerMod.MODID,"reindeer_saddle_fall_damage_modifier"),
									-1.0f,
									AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL),
								EquipmentSlotGroup.SADDLE)
							.build())
					.component(DataComponents.ENCHANTMENT_GLINT_OVERRIDE, true)
					.component(DataComponents.EQUIPPABLE, Equippable.saddle())));

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
								output.accept(REINDEER_SADDLE.get());
								output.accept(BUTTER_COOKIE.get());
							}).build());


	public static void registerItems(IEventBus eventBus) {
		ITEMS.register(eventBus);
	}

	public static void registerTabs(IEventBus eventBus) {
		TABS.register(eventBus);
	}
}
