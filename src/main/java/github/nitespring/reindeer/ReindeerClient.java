package github.nitespring.reindeer;

import github.nitespring.reindeer.client.render.entity.mob.ReindeerRenderer;
import github.nitespring.reindeer.client.render.screen.ReindeerInventoryScreen;
import github.nitespring.reindeer.common.entity.mob.AbstractReindeer;
import github.nitespring.reindeer.common.inventory.ReindeerInventoryMenu;
import github.nitespring.reindeer.core.datagen.CustomItemModelProvider;
import github.nitespring.reindeer.core.init.EntityInit;
import github.nitespring.reindeer.core.init.MenuInit;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;
import net.neoforged.neoforge.data.event.GatherDataEvent;

// This class will not load on dedicated servers. Accessing client side code from here is safe.
@Mod(value = ReindeerMod.MODID, dist = Dist.CLIENT)
// You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
@EventBusSubscriber(modid = ReindeerMod.MODID, value = Dist.CLIENT)
public class ReindeerClient {
    public ReindeerClient(ModContainer container) {
        // Allows NeoForge to create a config screen for this mod's configs.
        // The config screen is accessed by going to the Mods screen > clicking on your mod > clicking on config.
        // Do not forget to add translations for your config options to the en_us.json file.
        container.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }

    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(EntityInit.REINDEER.get(), ReindeerRenderer::new);
    }

    @SubscribeEvent
    static void onClientSetup(FMLClientSetupEvent event) {
        // Some client setup code
        /*ReindeerMod.LOGGER.info("HELLO FROM CLIENT SETUP");
        ReindeerMod.LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());*/
    }

    @SubscribeEvent
    public static void registerScreens(RegisterMenuScreensEvent event) {
        //event.register(MenuInit.REINDEER_INVENTORY_MENU.get(),ReindeerInventoryScreen::new);
        event.register(MenuInit.REINDEER_INVENTORY_MENU.get(),
                new MenuScreens.ScreenConstructor<ReindeerInventoryMenu, ReindeerInventoryScreen>() {
            @Override
            public ReindeerInventoryScreen create(ReindeerInventoryMenu reindeerInventoryMenu, Inventory inventory, Component component) {
                return new ReindeerInventoryScreen(reindeerInventoryMenu,inventory,component);
            }
        });
    }


}
