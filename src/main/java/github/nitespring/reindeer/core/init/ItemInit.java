package github.nitespring.reindeer.core.init;

import github.nitespring.reindeer.Reindeer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ItemInit {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(BuiltInRegistries.ITEM,
			 Reindeer.MODID);
	
	
	//Items
	/*public static final DeferredHolder<Item,Item> SNOWMAN = ITEMS.register("evil_snowman_spawn_egg",
			() -> new SpawnEggItem(EntityInit.SNOWMAN.get(), 14283506, 16737400, new Item.Properties()));*/


	/*public static final DeferredHolder<Item,Item> CANDY_FRAGMENT = ITEMS.register("candy_fragment",
			() -> new Item(new Item.Properties().rarity(Rarity.UNCOMMON).food(YuleFoods.CANDY)));*/


}
