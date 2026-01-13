package github.nitespring.reindeer.common.inventory;

import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.common.entity.mob.AbstractReindeer;
import github.nitespring.reindeer.common.entity.mob.Reindeer;
import github.nitespring.reindeer.core.init.EntityInit;
import github.nitespring.reindeer.core.init.MenuInit;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.datafix.fixes.SaddleEquipmentSlotFix;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jspecify.annotations.Nullable;

public class ReindeerInventoryMenu extends AbstractContainerMenu {
    private final Container mountContainer;
    private final AbstractReindeer mount;
    private static final Identifier SADDLE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/saddle");
    private static final Identifier CHEST_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/chest");
    private static final Identifier ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/horse_armor");

    /*public ReindeerInventoryMenu(int containerId, Inventory playerInv) {
        this(containerId, playerInv, new SimpleContainer(1), null);
    }*/
    public ReindeerInventoryMenu(int containerId, Inventory playerInv, RegistryFriendlyByteBuf buf) {

        this(containerId, playerInv, new SimpleContainer(17),(AbstractReindeer) Minecraft.getInstance().level.getEntity(buf.readInt()));
    }

    public ReindeerInventoryMenu(int containerId, Inventory playerInventory, Container mountContainer, AbstractReindeer mount) {
        super(MenuInit.REINDEER_INVENTORY_MENU.get(), containerId);
        this.mountContainer = mountContainer;
        this.mount = mount;
        Container container1 = mount.createEquipmentSlotContainer(EquipmentSlot.SADDLE);
        this.addSlot(new ArmorSlot(container1, mount, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE) {
            public boolean isActive() {
                return mount.canUseSlot(EquipmentSlot.SADDLE);
            }
        });
        this.addSlot(new Slot(mountContainer, 0, 8, 36) {
            @Override
            public void setChanged() {
                this.container.setChanged();
            }

            /*@Override
            public @Nullable Identifier getNoItemIcon() {
                return CHEST_SLOT_SPRITE;
            }*/

            @Override
            public boolean mayPlace(ItemStack stack) {
                return super.mayPlace(stack) && stack.is(Items.CHEST);
            }
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });
        int s = 1;
        for(int i = 0; i < 3; ++i) {
            for(int j = 0; j < 5; ++j) {
                this.addSlot(new Slot(mountContainer, s + j + i * 5, 80 + j * 18, 18 + i * 18) {
                    public boolean isActive() {
                        return mount.hasChest();
                    }
                });
            }
        }
//        this.addSlot(new Slot(mountContainer, 0, 8, 18) {
//            @Override
//            public void setChanged() {
//                this.container.setChanged();
//            }
//
//            @Override
//            public boolean mayPlace(ItemStack stack) {
//                return super.mayPlace(stack) && stack.is(Items.SADDLE);
//            }
//        });
        mountContainer.startOpen(playerInventory.player);
        this.addStandardInventorySlots(playerInventory, 8, 84);
    }


    protected boolean hasInventoryChanged(Container inventory) {
        return this.mount.hasInventoryChanged(inventory);
    }

    public AbstractReindeer getMount() {
        return mount;
    }

    @Override
    public boolean stillValid(Player player) {
        return !this.hasInventoryChanged(this.mountContainer) && this.mountContainer.stillValid(player) && this.mount.isAlive() && player.isWithinEntityInteractionRange(this.mount, (double)4.0F);
    }
    @Override
    public void removed(Player player) {
        super.removed(player);
        this.mountContainer.stopOpen(player);
    }
    @Override
    public ItemStack quickMoveStack(Player player, int p_470684_) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot)this.slots.get(p_470684_);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            int i = 2 + this.mountContainer.getContainerSize();
            if (p_470684_ < i) {
                if (!this.moveItemStackTo(itemstack1, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemstack1) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemstack1) && !this.getSlot(0).hasItem()) {
                if (!this.moveItemStackTo(itemstack1, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.mountContainer.getContainerSize() == 0 || !this.moveItemStackTo(itemstack1, 2, i, false)) {
                int j = i + 27;
                int k = j + 9;
                if (p_470684_ >= j && p_470684_ < k) {
                    if (!this.moveItemStackTo(itemstack1, i, j, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (p_470684_ >= i && p_470684_ < j) {
                    if (!this.moveItemStackTo(itemstack1, j, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemstack1, j, j, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemstack1.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemstack;
    }

}