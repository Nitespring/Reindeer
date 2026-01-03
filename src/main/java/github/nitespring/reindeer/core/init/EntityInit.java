package github.nitespring.reindeer.core.init;

import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.common.entity.misc.DamageHitboxEntity;
import github.nitespring.reindeer.common.entity.mob.Reindeer;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class EntityInit {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,
			 ReindeerMod.MODID);
	
	/*public static final DeferredHolder<EntityType<?>,EntityType<Reindeer>> REINDEER = ENTITIES.register("reindeer",
			() -> EntityType.Builder.<Reindeer>of(Reindeer::new, MobCategory.MONSTER).sized(0.8f, 1.9f)
					.build(createResourcekey("reindeer")));*/


	public static final DeferredHolder<EntityType<?> ,EntityType<Reindeer>> REINDEER = registerEntity("reindeer",
			EntityType.Builder.<Reindeer>of(Reindeer::new, MobCategory.CREATURE)
					.sized(1.3964844F, 1.6F)
					.eyeHeight(1.52F)
					.passengerAttachments(1.31875F)
					.clientTrackingRange(10)
			);

	public static final Supplier<EntityType<DamageHitboxEntity>> HITBOX = registerEntity("hitbox",
			EntityType.Builder.<DamageHitboxEntity>of(DamageHitboxEntity::new, MobCategory.MISC)
				.sized(1.5f, 1.5f)
					);

	public static final Supplier<EntityType<DamageHitboxEntity>> HITBOX_LARGE = registerEntity("hitbox_large",
			EntityType.Builder.<DamageHitboxEntity>of(DamageHitboxEntity::new, MobCategory.MISC)
					.sized(3.5f, 2.5f)
					);

	public static <T extends Entity> DeferredHolder<EntityType<?> ,EntityType<T>> registerEntity(String key, EntityType.Builder<T> builder)
	{
		return ENTITIES.register(key, ()-> builder.build(createResourcekey(key)));
	}

	private static ResourceKey<EntityType<?>> createResourcekey(String key) {
		return ResourceKey.create(Registries.ENTITY_TYPE,
				Identifier.fromNamespaceAndPath(ReindeerMod.MODID, key));
	}

	public static void register(IEventBus eventBus) {
		ENTITIES.register(eventBus);
	}


}
