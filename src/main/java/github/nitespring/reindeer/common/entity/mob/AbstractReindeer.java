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
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
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

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_TICK, 0);
        builder.define(ANIMATION_STATE, 0);
        builder.define(COMBAT_STATE, 0);
        builder.define(ENTITY_STATE, 0);
    }


    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide()) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }

    }

    public InteractionResult mobInteract(Player p_478070_, InteractionHand p_478064_) {
        if (!this.isVehicle() && !this.isBaby()) {

            ItemStack itemstack = p_478070_.getItemInHand(p_478064_);
            if (!itemstack.isEmpty()) {
                InteractionResult interactionresult = itemstack.interactLivingEntity(p_478070_, this, p_478064_);
                if (interactionresult.consumesAction()) {
                    return interactionresult;
                }

            }

            this.doPlayerRide(p_478070_);
            return InteractionResult.SUCCESS;

        } else {
            return super.mobInteract(p_478070_, p_478064_);
        }
    }

    protected @Nullable SoundEvent getEatingSound() {
        return null;
    }

    protected @Nullable SoundEvent getAngrySound() {
        return null;
    }



    private boolean isWoodSoundType(SoundType soundType) {
        return soundType == SoundType.WOOD || soundType == SoundType.NETHER_WOOD || soundType == SoundType.STEM || soundType == SoundType.CHERRY_WOOD || soundType == SoundType.BAMBOO_WOOD;
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
        this.setDeltaMovement(this.getDeltaMovement().add(0,1.5f,0));
    }
    @Override
    public boolean canJump() {
        return true;
       // return this.isSaddled();
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
