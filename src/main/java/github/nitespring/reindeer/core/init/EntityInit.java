package github.nitespring.reindeer.core.init;

import github.nitespring.reindeer.Reindeer;
import github.nitespring.reindeer.common.entity.misc.DamageHitboxEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class EntityInit {
	public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE,
			 Reindeer.MODID);
	
	/*public static final DeferredHolder<EntityType<?>,EntityType<EvilSnowman>> SNOWMAN = ENTITIES.register("evil_snowman",
			() -> EntityType.Builder.<EvilSnowman>of(EvilSnowman::new, MobCategory.MONSTER)
			.sized(0.8f, 1.9f)
			.build("evil_snowman"));
	*/
	public static final DeferredHolder<EntityType<?>,EntityType<DamageHitboxEntity>> HITBOX = ENTITIES.register("hitbox",
			() -> EntityType.Builder.<DamageHitboxEntity>of(DamageHitboxEntity::new, MobCategory.MISC)
				.sized(1.5f, 1.5f)
					.build(ResourceKey.create(Registries.ENTITY_TYPE,
							Identifier.fromNamespaceAndPath(Reindeer.MODID, "hitbox"))));

	public static final DeferredHolder<EntityType<?>,EntityType<DamageHitboxEntity>> HITBOX_LARGE = ENTITIES.register("hitbox_large",
			() -> EntityType.Builder.<DamageHitboxEntity>of(DamageHitboxEntity::new, MobCategory.MISC)
					.sized(3.5f, 2.5f)
					.build(ResourceKey.create(Registries.ENTITY_TYPE,
							Identifier.fromNamespaceAndPath(Reindeer.MODID, "hitbox_large"))));




}
