package github.nitespring.reindeer.core.event;


import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.core.init.EntityInit;
import github.nitespring.reindeer.core.tags.CustomBiomeTags;
import github.nitespring.reindeer.core.tags.CustomBlockTags;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.Heightmap.Types;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@Mod(value = ReindeerMod.MODID)
@EventBusSubscriber(modid = ReindeerMod.MODID)
public class EntitySpawnRegistration {

	public static boolean checkSnowMobSpawnRules(EntityType<? extends LivingEntity> e, LevelAccessor level, EntitySpawnReason reason, BlockPos pos, RandomSource r) {
		BlockPos blockpos = pos.below();
		return (level.getBlockState(blockpos).is(CustomBlockTags.COLD_BLOCK) || level.getBiome(pos).is(CustomBiomeTags.SPAWN_SNOW_MOBS_DEFAULT))
				&& (reason == EntitySpawnReason.SPAWNER || (level.getBlockState(pos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON)&&level.getBlockState(blockpos).isValidSpawn(level, blockpos, e)));

	}



	@SubscribeEvent
	public static void registerEntitySpawn(RegisterSpawnPlacementsEvent event) {

            
            event.register(EntityInit.REINDEER.get(),
            		SpawnPlacementTypes.ON_GROUND,
            		Types.MOTION_BLOCKING_NO_LEAVES, 
            		EntitySpawnRegistration::checkSnowMobSpawnRules,
					RegisterSpawnPlacementsEvent.Operation.REPLACE);


    }
	
	
	

}
