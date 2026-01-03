package github.nitespring.reindeer.core.event;


import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.core.datagen.CustomItemModelProvider;
import github.nitespring.reindeer.core.init.EntityInit;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Monster;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.data.event.GatherDataEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;


@EventBusSubscriber(modid = ReindeerMod.MODID)
public class ModRegistrationEventHandler {
	
	@SubscribeEvent
	public static void registerAttributes(EntityAttributeCreationEvent event) {
		AttributeSupplier.Builder reindeerAttributes = Monster.createMobAttributes()
				.add(Attributes.FOLLOW_RANGE, 16)
				.add(Attributes.MAX_HEALTH, 16)
				.add(Attributes.MOVEMENT_SPEED, 0.25f)
				.add(Attributes.ATTACK_DAMAGE, 1.5f)
				.add(Attributes.ATTACK_KNOCKBACK, 0.25)
				.add(Attributes.TEMPT_RANGE, 5);

		event.put(EntityInit.REINDEER.get(), reindeerAttributes.build());

		
	}



}
