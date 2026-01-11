package github.nitespring.reindeer.client.render.entity.mob;

import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.client.render.entity.CustomDataTickets;
import github.nitespring.reindeer.common.entity.mob.AbstractReindeer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.base.GeoRenderState;
import software.bernie.geckolib.renderer.base.GeoRenderer;
import software.bernie.geckolib.renderer.base.RenderPassInfo;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ReindeerSaddleLayer<T extends AbstractReindeer & GeoAnimatable, O, R extends GeoRenderState> extends GeoRenderLayer<T, O, R> {

    private static final Identifier RESOURCE = Identifier.fromNamespaceAndPath(ReindeerMod.MODID, "textures/entity/reindeer/reindeer_saddle.png");


    public ReindeerSaddleLayer(GeoRenderer<T, O, R> renderer) {
        super(renderer);

    }

    @Override
    protected Identifier getTextureResource(R renderState) {
        return RESOURCE;
    }

    protected @Nullable RenderType getRenderType(R renderState) {

        return RenderTypes.entityCutoutNoCull(getTextureResource(renderState));
    }

    @Override
    public void submitRenderTask(RenderPassInfo<R> renderPassInfo, SubmitNodeCollector renderTasks) {
        if (!renderPassInfo.willRender())
            return;
        if(getTextureResource(renderPassInfo.renderState())!=null) {
            RenderType renderType = getRenderType(renderPassInfo.renderState());
            if (!renderPassInfo.renderState().hasGeckolibData(CustomDataTickets.IS_BABY)
                    || !renderPassInfo.renderState().getGeckolibData(CustomDataTickets.IS_BABY)) {
                if (renderPassInfo.renderState().hasGeckolibData(CustomDataTickets.HAS_SADDLE)
                        && renderPassInfo.renderState().getGeckolibData(CustomDataTickets.HAS_SADDLE)) {
                    if (renderType != null)
                        this.renderer.submitRenderTasks(renderPassInfo, renderTasks.order(1), renderType);
                }
            }
        }
    }
}