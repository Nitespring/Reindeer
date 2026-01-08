package github.nitespring.reindeer.common.entity.mob;

import github.nitespring.reindeer.core.init.EntityInit;
import github.nitespring.reindeer.core.tags.CustomItemTags;
import net.minecraft.core.NonNullList;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.equine.Horse;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.animal.wolf.WolfSoundVariants;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.level.storage.loot.LootTable;
import net.neoforged.neoforge.event.EventHooks;
import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;

import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Random;

public class Reindeer extends AbstractReindeer implements GeoEntity {

    private static final EntityDataAccessor<Integer> COLOUR = SynchedEntityData.defineId(Reindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> RUDOLPH = SynchedEntityData.defineId(Reindeer.class, EntityDataSerializers.BOOLEAN);
    //private static final EntityDataAccessor<Boolean> IS_BABY = SynchedEntityData.defineId(Reindeer.class, EntityDataSerializers.BOOLEAN);

    protected AnimatableInstanceCache factory = GeckoLibUtil.createInstanceCache(this);

    public Reindeer(EntityType<? extends AbstractReindeer> entityType, Level level) {
        super(entityType, level);
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(CustomItemTags.REINDEER_FOOD);
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return factory;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<Reindeer>( "hit_controller", 4, this::hitStunPredicate));
        controllers.add(new AnimationController<Reindeer>( "main_controller", 8, this::predicate));
    }

    private String createAnimationName(String anim){
        String animIdentifier = new String("reindeer");
        if(this.isBaby()){
            animIdentifier = new String("baby_reindeer");
        }

        return new String("animation." + animIdentifier + "." + anim);
    }


    private <E extends GeoAnimatable> PlayState hitStunPredicate(AnimationTest<E> event) {
        if(hitStunTicks>0) {
            event.controller().setAnimation(RawAnimation.begin().thenPlay(createAnimationName("hit")));
        }else {
            event.controller().setAnimation(RawAnimation.begin().thenLoop(createAnimationName("null")));
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationTest<E> event) {
        int animState = this.getAnimationState();
        if(this.isBaby()) {
            if (this.isDeadOrDying()) {
                event.controller().setAnimationSpeed(1.0f).setAnimation(RawAnimation.begin().thenPlay("animation.baby_reindeer.death"));
            } else {
                float speed = getMovementSpeed();
                switch (getMovementState()) {
                    case 1:
                        event.controller().setAnimationSpeed(3.5f * speed).setAnimation(RawAnimation.begin().thenLoop("animation.baby_reindeer.walk"));
                        break;
                    case 2:
                        event.controller().setAnimationSpeed(5.0f * speed).setAnimation(RawAnimation.begin().thenLoop("animation.baby_reindeer.run"));
                        break;
                    default:
                        event.controller().setAnimationSpeed(1.0f).setAnimation(RawAnimation.begin().thenLoop("animation.baby_reindeer.idle"));
                        break;
                }
            }
        }else {
            if (this.isDeadOrDying()) {
                event.controller().setAnimationSpeed(1.0f).setAnimation(RawAnimation.begin().thenPlay("animation.reindeer.death"));
            } else {
                float speed = getMovementSpeed();
                switch (getMovementState()) {
                    case 1:
                        event.controller().setAnimationSpeed(2.5f * speed).setAnimation(RawAnimation.begin().thenLoop("animation.reindeer.walk"));
                        break;
                    case 2:
                        event.controller().setAnimationSpeed(4.0f * speed).setAnimation(RawAnimation.begin().thenLoop("animation.reindeer.run"));
                        break;
                    default:
                        event.controller().setAnimationSpeed(1.0f).setAnimation(RawAnimation.begin().thenLoop("animation.reindeer.idle"));
                        break;
                }
            }
        }
        return PlayState.CONTINUE;
    }

    public int getColour() {return this.entityData.get(COLOUR);}
    public void setColour(int anim) {this.entityData.set(COLOUR, anim);}


    public boolean isRudolph() {return this.entityData.get(RUDOLPH);}
    public void setRudolph(boolean anim) {this.entityData.set(RUDOLPH, anim);}



    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOUR, 0);
        builder.define(RUDOLPH, false);
    }

    @Override
    protected void addAdditionalSaveData(ValueOutput out) {
        super.addAdditionalSaveData(out);
        out.putInt("CoatColour", this.getColour());
        out.putBoolean("isRudolph", this.isRudolph());

    }

    @Override
    protected void readAdditionalSaveData(ValueInput in) {
        super.readAdditionalSaveData(in);
        this.setColour(in.getIntOr("CoatColour",0));
        this.setRudolph(in.getBooleanOr("isRudolph",false));

    }


    @Override
    protected void registerGoals() {
        /*
        this.goalSelector.addGoal(4, new FollowParentGoal(this, (double)1.0F));*/
        this.goalSelector.addGoal(2, new BreedGoal(this, (double)1.0F, Reindeer.class));
        this.goalSelector.addGoal(2, new PanicGoal(this, (double)1.2F));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));


        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.25F, (item) -> item.is(CustomItemTags.REINDEER_FOOD), false));
    }

    @Override
    public @Nullable AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        Reindeer reindeer = (Reindeer) EntityInit.REINDEER.get().create(serverLevel, EntitySpawnReason.BREEDING);
        if (reindeer != null && ageableMob instanceof Reindeer reindeer1) {
            if (this.random.nextBoolean()) {
                reindeer.setColour(this.getColour());
            } else {
                reindeer.setColour(reindeer1.getColour());
            }
            if (this.random.nextBoolean()) {
                reindeer.setRudolph(this.isRudolph());
            } else {
                reindeer.setRudolph(reindeer1.isRudolph());
            }
            if (this.random.nextBoolean()) {
                reindeer.setLights(this.hasLights());
            } else {
                reindeer.setLights(reindeer1.hasLights());
            }
        }
        return reindeer;
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, EntitySpawnReason spawnReason, @Nullable SpawnGroupData data) {
        if(spawnReason != EntitySpawnReason.BREEDING) {
            int r = new Random().nextInt(255);
            if (r <= 35) {
                this.setColour(1);
            } else if (r <= 110) {
                this.setColour(2);
            } else if (r <= 150) {
                this.setColour(3);
            } else {
                this.setColour(0);
            }
            int r1 = new Random().nextInt(255);
            if (r1 <= 15) {
                this.setLights(true);
            }else{
                this.setLights(false);
            }

            int r2 = new Random().nextInt(255);
            if (r2 <= 7) {
                this.setRudolph(true);
            }else{
                this.setRudolph(false);
            }
        }
        return super.finalizeSpawn(level, difficulty, spawnReason, data);
    }


    @Override
    public boolean canMate(Animal animal) {
        if (animal == this) {
            return false;
        }else if (animal instanceof Reindeer) {
            Reindeer wolf = (Reindeer)animal;
                return this.isInLove() && wolf.isInLove();
        } else {
            return false;
        }
    }
    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        if (!this.isVehicle() && this.isTame() && player.isSecondaryUseActive()) {
            this.openCustomInventoryScreen(player);
            return InteractionResult.SUCCESS;
        }
        boolean flag = !this.isBaby() && this.isTame() && player.isSecondaryUseActive();
        if (!this.isVehicle() && !flag) {
            ItemStack itemstack = player.getItemInHand(hand);
            if (!itemstack.isEmpty()) {
                if (this.isFood(itemstack)) {
                    return this.fedFood(player, itemstack);
                }
            }
            return super.mobInteract(player, hand);
        } else {
            return super.mobInteract(player, hand);
        }
    }
    public InteractionResult fedFood(Player player, ItemStack stack) {
        boolean flag = this.handleEating(player, stack);
        if (flag) {
            stack.consume(1, player);
        }

        return (InteractionResult)(!flag && !this.level().isClientSide() ? InteractionResult.PASS : InteractionResult.SUCCESS_SERVER);
    }
    protected boolean handleEating(Player player, ItemStack stack) {
        boolean flag = false;
        float f = 0.0F;
        int i = 0;
        if (!this.level().isClientSide()&&stack.is(CustomItemTags.REINDEER_FOOD_TAMING)) {
            f = 2.0F;
            i = 20;
            this.tryToTame(player);
        }

        if (stack.is(CustomItemTags.REINDEER_FOOD_MATING)) {
            f = 4.0F;
            i = 60;
            if (!this.level().isClientSide() && this.getAge() == 0 && !this.isInLove()) {
                flag = true;
                this.setInLove(player);
            }
        }

        if (this.getHealth() < this.getMaxHealth() && f > 0.0F) {
            this.heal(f);
            flag = true;
        }

        if (this.isBaby() && i > 0) {
            this.level().addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX((double)1.0F), this.getRandomY() + (double)0.5F, this.getRandomZ((double)1.0F), (double)0.0F, (double)0.0F, (double)0.0F);
            if (!this.level().isClientSide()) {
                this.ageUp(i);
                flag = true;
            }
        }



        if (flag) {
            this.gameEvent(GameEvent.EAT);
        }

        return flag;
    }

    private void tryToTame(Player player) {
        if (this.random.nextInt(5) == 0 && !EventHooks.onAnimalTame(this, player)) {
            this.tame(player);
            this.navigation.stop();
            this.setTarget((LivingEntity)null);
            //this.setOrderedToSit(true);
            this.level().broadcastEntityEvent(this, (byte)7);
        } else {
            this.level().broadcastEntityEvent(this, (byte)6);
        }

    }



}
