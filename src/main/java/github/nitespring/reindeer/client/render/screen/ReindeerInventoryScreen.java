package github.nitespring.reindeer.client.render.screen;

import github.nitespring.reindeer.common.entity.mob.AbstractReindeer;
import github.nitespring.reindeer.common.inventory.ReindeerInventoryMenu;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractMountInventoryScreen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
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


public class ReindeerInventoryScreen extends AbstractMountInventoryScreen<ReindeerInventoryMenu> {
    private static final Identifier SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot");
    private static final Identifier CHEST_SLOTS_SPRITE = Identifier.withDefaultNamespace("container/horse/chest_slots");
    private static final Identifier HORSE_INVENTORY_LOCATION = Identifier.withDefaultNamespace("textures/gui/container/horse.png");

    public ReindeerInventoryScreen(ReindeerInventoryMenu menu, Inventory playerInventory, AbstractReindeer mount, int inventoryColumns) {
        super(menu, playerInventory, mount.getDisplayName(), inventoryColumns, mount);
    }
    public ReindeerInventoryScreen(ReindeerInventoryMenu menu, Inventory playerInventory, Component title, int inventoryColumns, LivingEntity mount) {
        super(menu, playerInventory, title,inventoryColumns,mount);
    }
    public ReindeerInventoryScreen(ReindeerInventoryMenu menu, Inventory playerInventory, Component title ) {
        super(menu, playerInventory, title,0,(LivingEntity)null);
    }

    /*public ReindeerInventoryScreen(ReindeerInventoryMenu menu, Inventory playerInventory, Component name) {
        super(menu, playerInventory, name, 0, (AbstractReindeer)null);
    }*/
    @Override
    protected Identifier getBackgroundTextureLocation() {
        return HORSE_INVENTORY_LOCATION;
    }
    @Override
    protected Identifier getSlotSpriteLocation() {
        return SLOT_SPRITE;
    }
    @Override
    protected @Nullable Identifier getChestSlotsSpriteLocation() {
        return CHEST_SLOTS_SPRITE;
    }
    @Override
    protected boolean shouldRenderSaddleSlot() {
        return this.mount.canUseSlot(EquipmentSlot.SADDLE) && this.mount.getType().is(EntityTypeTags.CAN_EQUIP_SADDLE);
    }
    @Override
    protected boolean shouldRenderArmorSlot() {
        return this.mount.canUseSlot(EquipmentSlot.BODY) && (this.mount.getType().is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR));
    }
}