package github.nitespring.reindeer.common.entity.mob;

import github.nitespring.reindeer.core.tags.CustomItemTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.Nullable;

public abstract class AbstractReindeer extends Animal implements OwnableEntity, PlayerRideableJumping{

    protected int hitStunTicks=0;

    private static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMBAT_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ENTITY_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);

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

    public int getAnimationTick() {return this.entityData.get(ANIMATION_TICK);}
    public void setAnimationTick(int anim) {this.entityData.set(ANIMATION_TICK, anim);}
    public void increaseAnimationTick(int i) {setAnimationTick(getAnimationTick()+1);}
    public void increaseAnimationTick() {increaseAnimationTick(1);}
    public void resetAnimationTick() {setAnimationTick(0);}
    public int getAnimationState() {return this.entityData.get(ANIMATION_STATE);}
    public void setAnimationState(int anim) {this.entityData.set(ANIMATION_STATE, anim);}

    public int getCombatState() {return this.entityData.get(COMBAT_STATE);}
    public void setCombatState(int anim) {this.entityData.set(COMBAT_STATE, anim);}

    public int getEntityState() {return this.entityData.get(ENTITY_STATE);}
    public void setEntityState(int anim) {this.entityData.set(ENTITY_STATE, anim);}


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
    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }

    @Override
    public @Nullable EntityReference<LivingEntity> getOwnerReference() {
        return null;
    }

    @Override
    public void onPlayerJump(int i) {

    }
    @Override
    public boolean canJump() {
        return this.isSaddled();
    }

    @Override
    public void handleStartJump(int i) {

    }

    @Override
    public void handleStopJump() {

    }

    @Override
    public void travel(Vec3 pos) {
        if (this.isAlive()) {
            if (this.isVehicle()) {
                LivingEntity passenger = (LivingEntity)getControllingPassenger();
                this.yRotO = getYRot();
                this.xRotO = getXRot();

                setYRot(passenger.getYRot());
                setXRot(passenger.getXRot() * 0.5f);
                setRot(getYRot(), getXRot());

                this.yBodyRot = this.getYRot();
                this.yHeadRot = this.yBodyRot;
                float x = passenger.xxa * 0.5F;
                float z = passenger.zza;

                if (z <= 0)
                    z *= 0.25f;

                this.setSpeed(0.3f);
                super.travel(new Vec3(x, pos.y, z));
            }
        }
    }

    // Get the controlling passenger
    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity entity ? entity : null;
    }


}
