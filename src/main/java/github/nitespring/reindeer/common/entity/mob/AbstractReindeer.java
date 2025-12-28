package github.nitespring.reindeer.common.entity.mob;

import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import org.jspecify.annotations.Nullable;

public abstract class AbstractReindeer extends Animal implements OwnableEntity, PlayerRideableJumping{

    protected int hitStunTicks=0;

    private static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMBAT_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ENTITY_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final TargetingConditions.Selector PARENT_HORSE_SELECTOR = (livingEntity, level) -> {
        boolean var10000;
        if (livingEntity instanceof AbstractHorse abstracthorse) {
            if (abstracthorse.isBred()) {
                var10000 = true;
                return var10000;
            }
        }

        var10000 = false;
        return var10000;
    };
    protected boolean canGallop = true;
    protected int gallopSoundCounter;

    public AbstractReindeer(EntityType<? extends AbstractReindeer> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(PathType.DANGER_OTHER, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, -1.0F);
    }
    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    protected boolean canPerformRearing() {
        return true;
    }

    protected @Nullable SoundEvent getEatingSound() {
        return null;
    }

    protected @Nullable SoundEvent getAngrySound() {
        return null;
    }

    protected void playStepSound(BlockPos pos, BlockState block) {
        if (!block.liquid()) {
            BlockState blockstate = this.level().getBlockState(pos.above());
            SoundType soundtype = block.getSoundType(this.level(), pos, this);
            if (blockstate.is(Blocks.SNOW)) {
                soundtype = blockstate.getSoundType(this.level(), pos, this);
            }

            if (this.isVehicle() && this.canGallop) {
                ++this.gallopSoundCounter;
                if (this.gallopSoundCounter > 5 && this.gallopSoundCounter % 3 == 0) {
                    this.playGallopSound(soundtype);
                } else if (this.gallopSoundCounter <= 5) {
                    this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
                }
            } else if (this.isWoodSoundType(soundtype)) {
                this.playSound(SoundEvents.HORSE_STEP_WOOD, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            } else {
                this.playSound(SoundEvents.HORSE_STEP, soundtype.getVolume() * 0.15F, soundtype.getPitch());
            }
        }

    }

    private boolean isWoodSoundType(SoundType soundType) {
        return soundType == SoundType.WOOD || soundType == SoundType.NETHER_WOOD || soundType == SoundType.STEM || soundType == SoundType.CHERRY_WOOD || soundType == SoundType.BAMBOO_WOOD;
    }

    protected void playGallopSound(SoundType soundType) {
        this.playSound(SoundEvents.HORSE_GALLOP, soundType.getVolume() * 0.15F, soundType.getPitch());
    }

}
