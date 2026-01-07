package github.nitespring.reindeer.common.inventory;

import github.nitespring.reindeer.common.entity.mob.AbstractReindeer;
import github.nitespring.reindeer.common.entity.mob.Reindeer;
import github.nitespring.reindeer.core.init.MenuInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.Container;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractMountInventoryMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.inventory.ArmorSlot;

public class ReindeerInventoryMenu extends AbstractMountInventoryMenu {
    private static final Identifier SADDLE_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/saddle");
    private static final Identifier LLAMA_ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/llama_armor");
    private static final Identifier ARMOR_SLOT_SPRITE = Identifier.withDefaultNamespace("container/slot/horse_armor");

    public ReindeerInventoryMenu(int containerId, Inventory playerInv) {
        super(containerId, playerInv, (Container) null,(AbstractReindeer)null);
    }
    public ReindeerInventoryMenu(int containerId, Inventory playerInventory, Container mountContainer, final AbstractReindeer horse, int inventoryColumns) {
        super(containerId, playerInventory, mountContainer, horse);
        Container container = horse.createEquipmentSlotContainer(EquipmentSlot.SADDLE);
        this.addSlot(new ArmorSlot(container, horse, EquipmentSlot.SADDLE, 0, 8, 18, SADDLE_SLOT_SPRITE) {
            public boolean isActive() {
                return horse.canUseSlot(EquipmentSlot.SADDLE) && horse.getType().is(EntityTypeTags.CAN_EQUIP_SADDLE);
            }
        });
        /*final boolean flag = horse instanceof Llama;
        Identifier identifier = flag ? LLAMA_ARMOR_SLOT_SPRITE : ARMOR_SLOT_SPRITE;
        Container container1 = horse.createEquipmentSlotContainer(EquipmentSlot.BODY);
        this.addSlot(new ArmorSlot(container1, horse, EquipmentSlot.BODY, 0, 8, 36, identifier) {
            public boolean isActive() {
                return horse.canUseSlot(EquipmentSlot.BODY) && (horse.getType().is(EntityTypeTags.CAN_WEAR_HORSE_ARMOR) || flag);
            }
        });*/
        if (inventoryColumns > 0) {
            for(int i = 0; i < 3; ++i) {
                for(int j = 0; j < inventoryColumns; ++j) {
                    this.addSlot(new Slot(mountContainer, j + i * inventoryColumns, 80 + j * 18, 18 + i * 18));
                }
            }
        }

        this.addStandardInventorySlots(playerInventory, 8, 84);
    }

    protected boolean hasInventoryChanged(Container inventory) {
        return ((Reindeer)this.mount).hasInventoryChanged(inventory);
    }
}