package github.nitespring.reindeer.client.render.entity.mob;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.common.entity.mob.Reindeer;

import net.minecraft.client.renderer.MultiBufferSource;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

import net.minecraft.resources.Identifier;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;


import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;

import software.bernie.geckolib.renderer.GeoEntityRenderer;

import software.bernie.geckolib.renderer.base.GeoRenderState;

import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

@SuppressWarnings("ALL")
public class ReindeerRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Reindeer>{

	public ReindeerRenderer(EntityRendererProvider.Context renderManager)
    {
        super(renderManager, new ReindeerModel<>());
        
        this.shadowRadius = 0.5F;

          
    }
	@Override
	protected float getDeathMaxRotation(GeoRenderState renderState) {
		return 0f;
	}
	@Override
	public @Nullable RenderType getRenderType(GeoRenderState renderState, Identifier texture) {
		return RenderTypes.entityCutoutNoCull(texture);
	}



	public static class ReindeerModel<T extends Reindeer> extends GeoModel<T> {
		@Override
		public Identifier getAnimationResource(T animatable) {

			return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "reindeer.animation.json");
		}
		@Override
		public Identifier getModelResource(GeoRenderState renderState) {
			return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "reindeer.geo.json");

		}
		@Override
		public Identifier getTextureResource(GeoRenderState renderState) {
			return Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer.png");
		}
	}
}
