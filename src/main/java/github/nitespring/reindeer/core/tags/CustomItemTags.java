package github.nitespring.reindeer.core.tags;

import github.nitespring.reindeer.Reindeer;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public final class CustomItemTags {

    public static final TagKey<Item> REINDEER_FOOD = create("reindeer_food");

    private CustomItemTags() {
    }


    private static TagKey<Item> create(String string) {
        return TagKey.create(BuiltInRegistries.ITEM.key(), Identifier.fromNamespaceAndPath(Reindeer.MODID , string));
    }



}
