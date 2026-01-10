package github.nitespring.reindeer.client.render.screen;

import github.nitespring.reindeer.common.entity.mob.AbstractReindeer;
import github.nitespring.reindeer.common.inventory.ReindeerInventoryMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.*;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import javax.annotation.Nullable;


public class ReindeerInventoryScreen extends AbstractContainerScreen<ReindeerInventoryMenu> {
    private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
    private static final Identifier CHEST_SLOTS_SPRITE = Identifier.withDefaultNamespace("container/horse/chest_slots");
    private static final Identifier HORSE_INVENTORY_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/horse.png");
    //protected int inventoryColumns;
    protected float xMouse;
    protected float yMouse;
    protected AbstractReindeer mount;


    public ReindeerInventoryScreen(ReindeerInventoryMenu menu, Inventory playerInventory, Component title ) {
        super(menu, playerInventory, title);
        this.mount = menu.getMount();
        //this.inventoryColumns = mount.getInventoryColumns();

    }

    @Override
    protected void renderBg(GuiGraphics gui, float p_470831_, int p_470675_, int p_470799_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        gui.blit(RenderPipelines.GUI_TEXTURED, this.getBackgroundTextureLocation(), i, j, 0.0F, 0.0F, this.imageWidth, this.imageHeight, 256, 256);
        /*if (this.inventoryColumns > 0 && this.getChestSlotsSpriteLocation() != null) {
            gui.blitSprite(RenderPipelines.GUI_TEXTURED, this.getChestSlotsSpriteLocation(), 90, 54, 0, 0, i + 79, j + 17, this.inventoryColumns * 18, 54);
        }*/
        this.drawSlot(gui, i + 7, j + 35 - 18);
        if(this.mount!=null) {
           /* if (this.shouldRenderSaddleSlot()) {
                this.drawSlot(gui, i + 7, j + 35 - 18);
            }

            if (this.shouldRenderArmorSlot()) {
                this.drawSlot(gui, i + 7, j + 35);
            }*/
        InventoryScreen.renderEntityInInventoryFollowsMouse(gui, i + 26, j + 18, i + 78, j + 70, 17, 0.25F, this.xMouse, this.yMouse, this.mount);
        }
        }

    protected void drawSlot(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, this.getSlotSpriteLocation(), x, y, 18, 18);
    }
    @Override
    public void render(GuiGraphics p_470541_, int p_470731_, int p_470784_, float p_470749_) {
        this.xMouse = (float)p_470731_;
        this.yMouse = (float)p_470784_;
        super.render(p_470541_, p_470731_, p_470784_, p_470749_);
        this.renderTooltip(p_470541_, p_470731_, p_470784_);
    }


    /*public ReindeerInventoryScreen(ReindeerInventoryMenu menu, Inventory playerInventory, Component name) {
        super(menu, playerInventory, name, 0, (AbstractReindeer)null);
    }*/

    protected Identifier getBackgroundTextureLocation() {
        return HORSE_INVENTORY_LOCATION;
    }

    protected Identifier getSlotSpriteLocation() {
        return SLOT_SPRITE;
    }

    protected @Nullable Identifier getChestSlotsSpriteLocation() {
        return CHEST_SLOTS_SPRITE;
    }

}