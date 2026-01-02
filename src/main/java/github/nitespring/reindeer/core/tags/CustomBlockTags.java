package github.nitespring.reindeer.core.tags;

import github.nitespring.reindeer.ReindeerMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public final class CustomBlockTags {

    public static final TagKey<Block> COLD_BLOCK = create("cold_block");

    private CustomBlockTags() {
    }


    private static TagKey<Block> create(String string) {
        return TagKey.create(BuiltInRegistries.BLOCK.key(), Identifier.fromNamespaceAndPath(ReindeerMod.MODID , string));
    }



}
