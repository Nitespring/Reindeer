package github.nitespring.reindeer;

import github.nitespring.reindeer.core.init.EntityInit;
import github.nitespring.reindeer.core.init.ItemInit;
import net.neoforged.bus.api.SubscribeEvent;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;

import net.neoforged.bus.api.IEventBus;

import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod(ReindeerMod.MODID)
public class ReindeerMod {

    public static final String MODID = "reindeer";

    public static final Logger LOGGER = LogUtils.getLogger();


    public ReindeerMod(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        //NeoForge.EVENT_BUS.register(this);
        ItemInit.registerTabs(modEventBus);

        ItemInit.registerItems(modEventBus);

        EntityInit.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {

    }





}
