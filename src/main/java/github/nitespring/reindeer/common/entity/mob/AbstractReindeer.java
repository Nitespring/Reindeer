package github.nitespring.reindeer.common.entity.mob;

import com.google.common.graph.Network;
import github.nitespring.reindeer.common.entity.misc.DamageHitboxEntity;
import github.nitespring.reindeer.common.inventory.ReindeerInventoryMenu;
import github.nitespring.reindeer.core.init.MenuInit;
import net.minecraft.core.Holder;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundMountScreenOpenPacket;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.*;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.FlyingAnimal;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Donkey;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.entity.vehicle.boat.ChestBoat;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.pathfinder.PathType;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.extensions.IPlayerExtension;
import net.neoforged.neoforge.event.EventHooks;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemStacksResourceHandler;
import org.jspecify.annotations.Nullable;

public abstract class AbstractReindeer extends TamableAnimal implements ContainerListener, HasCustomInventoryScreen, MenuProvider, FlyingAnimal, OwnableEntity, PlayerRideableJumping {

    protected int hitStunTicks = 0;

    private static final EntityDataAccessor<Integer> ANIMATION_TICK = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ANIMATION_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> COMBAT_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> ENTITY_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> MOVEMENT_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> MOVEMENT_SPEED = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> LIGHTS = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LIGHT_STATE = SynchedEntityData.defineId(AbstractReindeer.class, EntityDataSerializers.INT);
    protected SimpleContainer inventory;

    protected int accelerationValue = 0;

    public AbstractReindeer(EntityType<? extends AbstractReindeer> entityType, Level level) {
        super(entityType, level);
        this.setTame(false, false);
        this.setPathfindingMalus(PathType.DANGER_OTHER, -1.0F);
        this.setPathfindingMalus(PathType.DAMAGE_OTHER, -1.0F);
        this.createInventory();
    }


    @Override
    public boolean canUseSlot(EquipmentSlot slot) {
        return slot != EquipmentSlot.SADDLE ? super.canUseSlot(slot) : this.isAlive() && !this.isBaby() && this.isTame();
    }
    @Override
    protected boolean canDispenserEquipIntoSlot(EquipmentSlot p_478125_) {
        return p_478125_ == EquipmentSlot.SADDLE && this.isTame() || super.canDispenserEquipIntoSlot(p_478125_);
    }
    @Override
    protected Holder<SoundEvent> getEquipSound(EquipmentSlot p_477912_, ItemStack p_481049_, Equippable p_481569_) {
        return (Holder<SoundEvent>)(p_477912_ == EquipmentSlot.SADDLE ? SoundEvents.HORSE_SADDLE : super.getEquipSound(p_477912_, p_481049_, p_481569_));
    }
    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    public int getAnimationTick() {
        return this.entityData.get(ANIMATION_TICK);
    }

    public void setAnimationTick(int anim) {
        this.entityData.set(ANIMATION_TICK, anim);
    }

    public void increaseAnimationTick(int i) {
        setAnimationTick(getAnimationTick() + 1);
    }

    public void increaseAnimationTick() {
        increaseAnimationTick(1);
    }

    public void resetAnimationTick() {
        setAnimationTick(0);
    }

    public int getAnimationState() {
        return this.entityData.get(ANIMATION_STATE);
    }

    public void setAnimationState(int anim) {
        this.entityData.set(ANIMATION_STATE, anim);
    }

    public int getCombatState() {
        return this.entityData.get(COMBAT_STATE);
    }

    public void setCombatState(int anim) {
        this.entityData.set(COMBAT_STATE, anim);
    }

    public int getEntityState() {
        return this.entityData.get(ENTITY_STATE);
    }

    public void setEntityState(int anim) {
        this.entityData.set(ENTITY_STATE, anim);
    }

    public int getMovementState() {
        return this.entityData.get(MOVEMENT_STATE);
    }

    public void setMovementState(int anim) {
        this.entityData.set(MOVEMENT_STATE, anim);
    }

    public float getMovementSpeed() {
        return this.entityData.get(MOVEMENT_SPEED);
    }

    public void setMovementSpeed(float anim) {
        this.entityData.set(MOVEMENT_SPEED, anim);
    }

    public boolean hasLights() {
        return this.entityData.get(LIGHTS);
    }

    public void setLights(boolean anim) {
        this.entityData.set(LIGHTS, anim);
    }

    public int getLightState() {
        return this.entityData.get(LIGHT_STATE);
    }

    public void setLightState(int anim) {
        this.entityData.set(LIGHT_STATE, anim);
    }

    public void changeLightState() {
        int i = getLightState();
        if (i < 3) {
            this.entityData.set(LIGHT_STATE, i + 1);
        } else {
            this.entityData.set(LIGHT_STATE, 0);
        }
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(ANIMATION_TICK, 0);
        builder.define(ANIMATION_STATE, 0);
        builder.define(COMBAT_STATE, 0);
        builder.define(ENTITY_STATE, 0);
        builder.define(MOVEMENT_STATE, 0);
        builder.define(LIGHTS, false);
        builder.define(LIGHT_STATE, 0);
        builder.define(MOVEMENT_SPEED, 0.0f);

    }

    @Override
    protected void addAdditionalSaveData(ValueOutput out) {
        super.addAdditionalSaveData(out);
        out.putInt("AnimationTick", this.getAnimationTick());
        out.putInt("AnimationState", this.getAnimationState());
        out.putInt("CombatState", this.getCombatState());
        out.putInt("EntityState", this.getEntityState());
        out.putInt("MovementState", this.getMovementState());
        out.putBoolean("HasLights", this.hasLights());
        out.putInt("LightState", this.getLightState());

        ValueOutput.TypedOutputList<ItemStackWithSlot> typedoutputlist = out.list("Items", ItemStackWithSlot.CODEC);
        for(int i = 0; i < this.inventory.getContainerSize(); ++i) {
            ItemStack itemstack = this.inventory.getItem(i);
            if (!itemstack.isEmpty()) {
                typedoutputlist.add(new ItemStackWithSlot(i, itemstack));
            }
        }
    }

    @Override
    protected void readAdditionalSaveData(ValueInput in) {
        super.readAdditionalSaveData(in);
        this.setAnimationTick(in.getIntOr("AnimationTick",0));
        this.setAnimationState(in.getIntOr("AnimationState",0));
        this.setAnimationState(in.getIntOr("CombatState",0));
        this.setAnimationState(in.getIntOr("EntityState",0));
        this.setAnimationState(in.getIntOr("MovementState",0));
        this.setLights(in.getBooleanOr("HasLights",false));
        this.setAnimationState(in.getIntOr("LightState",0));

        this.createInventory();
        for(ItemStackWithSlot itemstackwithslot : in.listOrEmpty("Items", ItemStackWithSlot.CODEC)) {
            if (itemstackwithslot.isValidInContainer(this.inventory.getContainerSize())) {
                this.inventory.setItem(itemstackwithslot.slot(), itemstackwithslot.stack());
            }
        }
    }

    @Override
    public boolean hurtServer(ServerLevel level, DamageSource damageSource, float f) {
        doHurt(damageSource, f);
        return super.hurtServer(level, damageSource, f);
    }

    @Override
    public boolean hurtClient(DamageSource damageSource) {
        return super.hurtClient(damageSource);
    }

    public void doHurt(DamageSource source, float f) {
        Entity e = source.getEntity();
        if(f>0 && (e != null && !(e instanceof DamageHitboxEntity && ((DamageHitboxEntity)e).getOwner() == this))) {
            if(hitStunTicks<=0) {
                hitStunTicks=5;
            }
        }



    }

    @Override
    public boolean isOwnedBy(LivingEntity entity) {
        return entity == this.getOwner();
    }

    @Override
    public void tick() {
        super.tick();
        if(hasLights()){
            if(tickCount%7==0) {
                changeLightState();
            }
        }
        if(hitStunTicks>=-1) {
            hitStunTicks--;
        }
        if (walkAnimation.isMoving()) {
            float speed = walkAnimation.speed();
            double speedAttribute = this.getAttributeValue(Attributes.MOVEMENT_SPEED);
            double speedModifier = getMoveControl().getSpeedModifier();

            float maxSpeed = (float) Math.max(0.8f, speedAttribute * speedModifier);
            float movementSpeed = (float) (speed / speedAttribute);
            float animationSpeed = speed / maxSpeed;
            if (movementSpeed <= 0.005f) {
                this.setMovementState(0);
            } else if (movementSpeed <= 2.5f) {
                this.setMovementState(1);
            } else {
                this.setMovementState(2);
            }
            //System.out.print("Speed:"+ animationSpeed);

            setMovementSpeed(animationSpeed);
        } else {
            this.setMovementState(0);
        }
    }

    protected void doPlayerRide(Player player) {
        if (!this.level().isClientSide()) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }

    }



    @Override
    protected void tickDeath() {
        ++this.deathTime;
        if (this.deathTime >= 40 && !this.level().isClientSide() && !this.isRemoved()) {
            this.level().broadcastEntityEvent(this, (byte)60);
            this.remove(RemovalReason.KILLED);
        }
    }
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isVehicle() && !this.isBaby()) {
            if (this.isTame() && player.isSecondaryUseActive()) {
                this.openCustomInventoryScreen(player);
                return InteractionResult.SUCCESS;
            } else {
                ItemStack itemstack = player.getItemInHand(hand);
                if (!itemstack.isEmpty()) {
                    InteractionResult interactionresult = itemstack.interactLivingEntity(player, this, hand);
                    if (interactionresult.consumesAction()) {
                        return interactionresult;
                    }

                }

                if (this.isTame() && player == this.getOwner()) {
                    this.doPlayerRide(player);
                }
                return InteractionResult.SUCCESS;
            }
        } else {
            return super.mobInteract(player, hand);
        }
    }

    @Override
    public boolean supportQuadLeash() {
        return true;
    }
    @Override
    public Vec3[] getQuadLeashOffsets() {
        return Leashable.createQuadLeashOffsets(this, 0.04, 0.52, 0.23, 0.87);
    }


    protected @Nullable SoundEvent getEatingSound() {
        return SoundEvents.HORSE_EAT;
    }

    protected @Nullable SoundEvent getAngrySound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.COW_HURT;
    }

    @Override
    protected @Nullable SoundEvent getDeathSound() {
        return SoundEvents.COW_DEATH;
    }

    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.COW_AMBIENT;
    }

    @Override
    public float getVoicePitch() {

        return isBaby() ? 1.6f : 0.4f;
    }

    private boolean isWoodSoundType(SoundType soundType) {
        return soundType == SoundType.WOOD || soundType == SoundType.NETHER_WOOD || soundType == SoundType.STEM || soundType == SoundType.CHERRY_WOOD || soundType == SoundType.BAMBOO_WOOD;
    }




    @Override
    public void onPlayerJump(int i) {
        this.setDeltaMovement(this.getDeltaMovement().add(0, this.getJumpPower(), 0));
    }

    @Override
    public boolean canJump() {
        //return true;
        return !this.getBlockStateOn().isAir();
        // return this.isSaddled();
    }

    @Override
    public void handleStartJump(int i) {

    }

    @Override
    public void handleStopJump() {

    }

    @Override
    protected void updateWalkAnimation(float partialTick) {
        float f = Math.min(partialTick * 4.0F, 1.0F);
        this.walkAnimation.update(f, 0.4F, this.isBaby() ? 3.0F : 1.0F);

    }


    @Override
    public boolean isFlying() {
        return this.isVehicle() && !this.onGround();
    }

    @Override
    public void travel(Vec3 pos) {
        if (this.isAlive()) {
            if (this.isVehicle()) {
                int accelerationValueMax=20;
                if(getDeltaMovement().horizontalDistance()>=0.001f){
                    if(accelerationValue<accelerationValueMax){accelerationValue++;}
                }else{
                    accelerationValue=0;
                }
                float actualAccelerationValue = 0.5f + 0.5f*Math.min(1.0f,accelerationValue/accelerationValueMax);
                if (this.onGround()) {

                    LivingEntity passenger = (LivingEntity) getControllingPassenger();
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

                    this.setSpeed((float) (actualAccelerationValue*this.getAttributeValue(Attributes.MOVEMENT_SPEED)));
                    super.travel(new Vec3(x, pos.y, z));
                } else {
                    LivingEntity passenger = (LivingEntity) getControllingPassenger();
                    Vec3 aim = passenger.getLookAngle().normalize();
                    this.yRotO = getYRot();
                    this.xRotO = getXRot();

                    setYRot(passenger.getYRot());
                    setXRot(passenger.getXRot() * 0.5f);
                    setRot(getYRot(), getXRot());

                    this.yBodyRot = this.getYRot();
                    this.yHeadRot = this.yBodyRot;
                    float x = passenger.xxa * 0.5F;
                    //float y = passenger.yya;
                    float z = passenger.zza;

                    //float x = (float) (7.5f*aim.x);
                    float y = (float) (aim.y);
                    //float z = (float) (7.5f*aim.z);

                    if (z <= 0)
                        z *= 0.25f;

                    this.setSpeed((float) (this.getAttributeValue(Attributes.FLYING_SPEED)*actualAccelerationValue));
                    super.travelFlying(new Vec3(x, y, z), (float) (actualAccelerationValue*this.getAttributeValue(Attributes.FLYING_SPEED)));
                }
            } else {
                accelerationValue=0;
                super.travel(pos);
            }
        }
    }

    @Override
    protected double getEffectiveGravity() {
        /*if(isVehicle()&&this.getDeltaMovement().horizontalDistance()>=0.1f) {
            return 0;
        }else{*/
            return super.getEffectiveGravity();
        //}
    }

    // Get the controlling passenger
    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return getFirstPassenger() instanceof LivingEntity entity ? entity : null;
    }

    @Override
    public void calculateEntityAnimation(boolean includeHeight) {
        super.calculateEntityAnimation(includeHeight);
    }

    @Override
    protected void propagateFallToPassengers(double fallDistance, float damageMultiplier, DamageSource damageSource) {

    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return null;
    }
    //Container Stuff

    @Override
    public void openCustomInventoryScreen(Player player) {
        System.out.print("Check 1 ");
        if (!this.level().isClientSide() /*&& (!this.isVehicle() || this.hasPassenger(player))*/ && this.isTame()) {
            System.out.print("Check 2 ");
            this.openReindeerInventory(player);

        }
    }
    @Override
    public @Nullable AbstractContainerMenu createMenu(int i, Inventory playerInventory, Player player) {

        return new ReindeerInventoryMenu(i, playerInventory, this.inventory, this, this.getInventoryColumns());
    }

    public void openReindeerInventory(Player player) {
        if (player.containerMenu != player.inventoryMenu) {
            player.closeContainer();
        }
        System.out.print("Check 3 ");
        int i = this.getInventoryColumns();
        if(player instanceof ServerPlayer serverPlayer) {

            serverPlayer.openMenu(this);
            /*serverPlayer.openMenu(new SimpleMenuProvider(
                    (containerId, playerInventory, player1) ->
                            new ReindeerInventoryMenu(containerId, playerInventory, inventory, this, i),
                    this.getDisplayName()
            ));*/
        }
    }



    public int getInventoryColumns() {
        return 3;
    }
    public boolean hasInventoryChanged(Container pInventory) {
        return this.inventory != pInventory;
    }



    protected void createInventory() {

        SimpleContainer simplecontainer = this.inventory;
        this.inventory = new SimpleContainer(this.getInventorySize());
        if (simplecontainer != null) {
            int i = Math.min(simplecontainer.getContainerSize(), this.inventory.getContainerSize());

            for(int j = 0; j < i; ++j) {
                ItemStack itemstack = simplecontainer.getItem(j);
                if (!itemstack.isEmpty()) {
                    this.inventory.setItem(j, itemstack.copy());
                }
            }
        }


    }

    public int getInventorySize() {
        return ReindeerInventoryMenu.getInventorySize(this.getInventoryColumns());
    }

    @Override
    public void containerChanged(Container container) {
    }

}
