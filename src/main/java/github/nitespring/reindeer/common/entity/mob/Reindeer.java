package github.nitespring.reindeer.common.entity.mob;

import github.nitespring.reindeer.core.tags.CustomItemTags;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;

import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.animation.state.AnimationTest;
import software.bernie.geckolib.util.GeckoLibUtil;

public class Reindeer extends AbstractReindeer implements GeoEntity {

    private static final EntityDataAccessor<Integer> COLOUR = SynchedEntityData.defineId(Reindeer.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> RUDOLPH = SynchedEntityData.defineId(Reindeer.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> LIGHTS = SynchedEntityData.defineId(Reindeer.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> LIGHT_STATE = SynchedEntityData.defineId(Reindeer.class, EntityDataSerializers.INT);
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
        controllers.add(new AnimationController<Reindeer>( "main_controller", 4, this::predicate));
    }

    private <E extends GeoAnimatable> PlayState hitStunPredicate(AnimationTest<E> event) {

        if(hitStunTicks>0) {
            event.controller().setAnimation(RawAnimation.begin().thenPlay("animation.reindeer.hit"));
        }else {
            event.controller().setAnimation(RawAnimation.begin().thenLoop("animation.reindeer.null"));
        }
        return PlayState.CONTINUE;
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationTest<E> event) {
        int animState = this.getAnimationState();
        if(this.isDeadOrDying()) {
            event.controller().setAnimationSpeed(1.0f).setAnimation(RawAnimation.begin().thenPlay("animation.reindeer.death"));
        }else {
            if(event.isMoving()){
                if(hasControllingPassenger()) {
                    event.controller().setAnimationSpeed(4.0f).setAnimation(RawAnimation.begin().thenLoop("animation.reindeer.run"));
                } else {
                    event.controller().setAnimationSpeed(1.2f).setAnimation(RawAnimation.begin().thenLoop("animation.reindeer.walk"));
                }

            }else {
                event.controller().setAnimationSpeed(1.0f).setAnimation(RawAnimation.begin().thenLoop("animation.reindeer.idle"));
            }
        }
        return PlayState.CONTINUE;
    }

    public int getColour() {return this.entityData.get(COLOUR);}
    public void setColour(int anim) {this.entityData.set(COLOUR, anim);}
    public int getLightState() {return this.entityData.get(LIGHT_STATE);}
    public void setLightState(int anim) {this.entityData.set(LIGHT_STATE, anim);}
    public void changeLightState() {
        int i = getLightState();
        if(i<5) {
            this.entityData.set(LIGHT_STATE, i + 1);
        }else{
            this.entityData.set(LIGHT_STATE, 0);
        }
    }

    public boolean isRudolph() {return this.entityData.get(RUDOLPH);}
    public void setRudolph(boolean anim) {this.entityData.set(RUDOLPH, anim);}
    public boolean hasLights() {return this.entityData.get(LIGHTS);}
    public void setLights(boolean anim) {this.entityData.set(LIGHTS, anim);}

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOUR, 0);
        builder.define(LIGHT_STATE, 0);
        builder.define(RUDOLPH, false);
        builder.define(LIGHTS, false);
    }



    @Override
    protected void registerGoals() {
        /*this.goalSelector.addGoal(2, new BreedGoal(this, (double)1.0F, Reindeer.class));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, (double)1.0F));*/
        //this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.2));
        this.goalSelector.addGoal(6, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));


        this.addBehaviourGoals();
    }

    protected void addBehaviourGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(3, new TemptGoal(this, (double)1.25F, (item) -> item.is(CustomItemTags.REINDEER_FOOD), false));
    }
}
