package github.nitespring.reindeer.core.init;

import github.nitespring.reindeer.ReindeerMod;
import github.nitespring.reindeer.common.entity.mob.Reindeer;
import github.nitespring.reindeer.common.inventory.ReindeerInventoryMenu;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.extensions.IMenuTypeExtension;
import net.neoforged.neoforge.network.IContainerFactory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class MenuInit{


    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU,
            ReindeerMod.MODID);

//    public static final DeferredHolder<MenuType<?>,MenuType<ReindeerInventoryMenu>> REINDEER_INVENTORY_MENU = MENUS.register("reindeer_inventory_menu",
//            () -> new MenuType(ReindeerInventoryMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static final DeferredHolder<MenuType<?>,MenuType<ReindeerInventoryMenu>> REINDEER_INVENTORY_MENU = MENUS.register("reindeer_inventory_menu",
            () -> IMenuTypeExtension.create(ReindeerInventoryMenu::new));

    public static void register(IEventBus eventBus) {
        MENUS.register(eventBus);
    }

}