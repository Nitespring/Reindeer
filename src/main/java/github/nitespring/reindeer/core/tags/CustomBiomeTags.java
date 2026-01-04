package github.nitespring.reindeer.core.tags;

import github.nitespring.reindeer.ReindeerMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public final class CustomBiomeTags {

    public static final TagKey<Biome> SPAWN_SNOW_MOBS_DEFAULT = create("spawn_snow_mobs_default");

    private CustomBiomeTags() {
    }

    private static TagKey<Biome> create(String p_203847_) {
        return TagKey.create(Registries.BIOME, Identifier.fromNamespaceAndPath(ReindeerMod.MODID , p_203847_));
    }


}
