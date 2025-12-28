package github.nitespring.reindeer.common.entity.misc;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.projectile.EvokerFangs;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DamageHitboxEntity extends Entity implements TraceableEntity {


	private @Nullable EntityReference<LivingEntity> owner;
	private int delayTicks = 1;
	private float damage = 4.0f;
	private List<LivingEntity> hitEntities = new ArrayList<LivingEntity>();
	private @Nullable List<MobEffectInstance> effects = new ArrayList<MobEffectInstance>();
	public DamageHitboxEntity(EntityType<?> entityType, Level level) {
		super(entityType, level);
	}

	public DamageHitboxEntity(EntityType<?> entityType, Level level, Vec3 pos, float dmg) {
		super(entityType, level);
		this.setPos(pos);
		this.damage=dmg;
	}
	public DamageHitboxEntity(EntityType<?> entityType, Level level, Vec3 pos, float dmg, int delayTicks) {
		this(entityType, level, pos, dmg);
		this.delayTicks = delayTicks;
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		
	}
	public void addMobEffect(MobEffectInstance mobEffect){
		if(this.effects.isEmpty()){
			this.effects.add(0,mobEffect);
		}else{
			int s = this.effects.size();
			this.effects.add(s,mobEffect);
		}
	}
	public List<MobEffectInstance> getMobEffects(){
		return this.effects;
	}
	@Override
	protected void readAdditionalSaveData(ValueInput valueInput) {
		this.delayTicks = valueInput.getIntOr("Delay", 0);
		this.owner = EntityReference.read(valueInput, "Owner");
	}
	@Override
	protected void addAdditionalSaveData(ValueOutput valueOutput) {
		valueOutput.putInt("Delay", this.delayTicks);
		EntityReference.store(this.owner, valueOutput, "Owner");
	}

	public void setOwner(@Nullable LivingEntity owner) {
		this.owner = EntityReference.of(owner);
	}
	@Override
	public @Nullable LivingEntity getOwner() {
		return EntityReference.getLivingEntity(this.owner, this.level());
	}

	@Override
	public Packet<ClientGamePacketListener> getAddEntityPacket(ServerEntity e) {
		return new ClientboundAddEntityPacket(this,e);
	}

	@Override
	public boolean fireImmune() {return true;}
	@Override
	public boolean isOnFire() {return false;}
	 
	 @Override
	public void tick() {
		delayTicks--;
		if(delayTicks<=0) {
			this.remove(RemovalReason.DISCARDED);
		}
		for(LivingEntity livingentity : this.level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(0.75D, 0.5D, 0.75D))) {
            if(this.getOwner()==null || !livingentity.isAlliedTo(this.getOwner())) {
            	if(!hitEntities.contains(livingentity)) {
         	    this.dealDamageTo(livingentity);
         	    hitEntities.add(livingentity);
            	}
            }
         }
		super.tick();
	}
	 
	 private void dealDamageTo(LivingEntity target) {
	      LivingEntity owner = this.getOwner();
	      if (target.isAlive() && !target.isInvulnerable() && target != owner) {
	         if (owner == null) {
				 target.hurt(this.damageSources().generic(), damage);
				 applyEffects(target);
	         } else {
	            if (owner.isAlliedTo(target)||owner==target) {
	               return;
	            }else {
					target.hurt(this.damageSources().mobAttack(owner), damage);
					applyEffects(target);
	            }
	         }

	      }
	   }
	private void applyEffects(LivingEntity target){
		List<MobEffectInstance> effects = this.getMobEffects();

		if(effects.isEmpty()){
			return;
		}

		int size = effects.size();
		for(int i = 0; i<=size; i++) {
			MobEffectInstance effect = effects.get(i);
			if (this.getOwner() == null) {
				target.addEffect(effect);
			} else {
				target.addEffect(effect, this.getOwner());
			}
		}
	}
	@Override
	public boolean hurtServer(ServerLevel s, DamageSource d, float f) {
		return false;
	}

	 
}
