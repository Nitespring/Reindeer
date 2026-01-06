package github.nitespring.reindeer.client.render.entity.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.client.render.entity.CustomDataTickets;
import github.nitespring.reindeer.common.entity.mob.Reindeer;

import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;


import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animation.state.BoneSnapshot;
import software.bernie.geckolib.cache.model.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;
import software.bernie.geckolib.model.GeoModel;

import software.bernie.geckolib.renderer.GeoEntityRenderer;

import software.bernie.geckolib.renderer.base.BoneSnapshots;
import software.bernie.geckolib.renderer.base.GeoRenderState;

import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;


public class ReindeerRenderer<T extends Entity & GeoAnimatable, R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Reindeer, R>{

	public ReindeerRenderer(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new ReindeerModel<>());
        
        this.shadowRadius = 0.5F;
		withRenderLayer(ReindeerEmissiveLightsLayer::new);
		withRenderLayer(ReindeerEmissiveNoseLayer::new);
          
    }

	@Override
	public int getPackedOverlay(Reindeer animatable, @Nullable Void relatedObject, float u, float partialTick) {
		return OverlayTexture.NO_OVERLAY;
	}
	@Override
	protected float getDeathMaxRotation(GeoRenderState renderState) {
		return 0f;
	}

	@Override
	protected float getShadowRadius(R renderState) {
		if(renderState.hasGeckolibData(CustomDataTickets.IS_BABY)
				&&renderState.getGeckolibData(CustomDataTickets.IS_BABY)) {
			return 0.5f;
		}else{
			return 0.75f;
		}
	}

	@Override
	public void scaleModelForRender(RenderPassInfo<R> renderPassInfo, float widthScale, float heightScale) {
		float width = widthScale;
		float height = heightScale;
		if(renderPassInfo.renderState().hasGeckolibData(CustomDataTickets.IS_BABY)
				&&renderPassInfo.getGeckolibData(CustomDataTickets.IS_BABY)){
			width = widthScale*0.7f;
			height = heightScale*0.7f;
		}

		super.scaleModelForRender(renderPassInfo, width, height);
	}

	@Override
	public void adjustModelBonesForRender(RenderPassInfo<R> renderPassInfo, BoneSnapshots snapshots) {
		BoneSnapshot head = snapshots.get("head_rotation").get();
		BoneSnapshot neck = snapshots.get("neck_rotation").get();
		float headPitch =  renderPassInfo.getGeckolibData(DataTickets.ENTITY_PITCH);
		float headYaw =  renderPassInfo.getGeckolibData(DataTickets.ENTITY_YAW);
		head.setRotX(-0.15f*headPitch * ((float) Math.PI / 180F));
		head.setRotY(-0.15f*headYaw * ((float) Math.PI / 180F));
		neck.setRotX(-0.45f*headPitch * ((float) Math.PI / 180F));
		neck.setRotY(-0.45f*headYaw * ((float) Math.PI / 180F));
		if(renderPassInfo.getGeckolibData(CustomDataTickets.IS_BABY)) {
			float headScale = 1.0f;
			head.setScale(headScale, headScale, headScale);
		}
	}

	public static class ReindeerModel<T extends Reindeer> extends GeoModel<T> {
		@Override
		public Identifier getAnimationResource(T animatable) {
			if(animatable.isBaby()) {
				return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "baby_reindeer");
			}else {
				return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "reindeer");
			}
		}
		@Override
		public Identifier getModelResource(GeoRenderState renderState) {
			if(renderState.hasGeckolibData(CustomDataTickets.IS_BABY)&&renderState.getGeckolibData(CustomDataTickets.IS_BABY)) {
				return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "baby_reindeer");
			}else{
				return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "reindeer");
			}
		}
		@Override
		public Identifier getTextureResource(GeoRenderState renderState) {
			if(renderState.hasGeckolibData(CustomDataTickets.IS_BABY)&&renderState.getGeckolibData(CustomDataTickets.IS_BABY)) {
				if(renderState.hasGeckolibData(CustomDataTickets.COLOUR)) {
					switch(renderState.getGeckolibData(CustomDataTickets.COLOUR)){
						case 1:
							return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/baby_reindeer_white.png");
						case 2:
							return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/baby_reindeer_brown.png");
						case 3:
							return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/baby_reindeer_black.png");
						default:
							return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/baby_reindeer.png");
					}
				}else{
					return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/baby_reindeer.png");
				}
			}else{
				if(renderState.hasGeckolibData(CustomDataTickets.COLOUR)) {
					switch(renderState.getGeckolibData(CustomDataTickets.COLOUR)){
						case 1:
							return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/reindeer_white.png");
						case 2:
							return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/reindeer_brown.png");
						case 3:
							return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/reindeer_black.png");
						default:
							return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/reindeer.png");
					}
				}else{
					return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/reindeer.png");
				}
			}
		}

		@Override
		public void addAdditionalStateData(T animatable, @Nullable Object relatedObject, GeoRenderState renderState) {
			renderState.addGeckolibData(CustomDataTickets.COLOUR, animatable.getColour());
			renderState.addGeckolibData(CustomDataTickets.HAS_LIGHTS, animatable.hasLights());
			renderState.addGeckolibData(CustomDataTickets.LIGHT_STATE, animatable.getLightState());
			renderState.addGeckolibData(CustomDataTickets.IS_RUDOLPH, animatable.isRudolph());
			renderState.addGeckolibData(CustomDataTickets.IS_BABY, animatable.isBaby());
		}
	}

}
