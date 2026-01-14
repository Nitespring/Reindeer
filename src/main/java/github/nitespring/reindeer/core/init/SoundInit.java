package github.nitespring.reindeer.core.init;

import github.nitespring.reindeer.ReindeerMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SoundInit {


    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(BuiltInRegistries.SOUND_EVENT,
            ReindeerMod.MODID);


    public static final DeferredHolder<SoundEvent,SoundEvent> REINDEER_CHIME = build("entity.reindeer.chime");


    private static DeferredHolder<SoundEvent,SoundEvent> build(String id)
    {
        return SOUNDS.register(id, () -> SoundEvent.createVariableRangeEvent(Identifier.fromNamespaceAndPath(ReindeerMod.MODID, id)));

    }

    public static void register(IEventBus eventBus) {
        SOUNDS.register(eventBus);
    }
}
