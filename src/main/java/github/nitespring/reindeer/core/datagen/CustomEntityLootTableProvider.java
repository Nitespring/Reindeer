package github.nitespring.reindeer.core.datagen;

import github.nitespring.reindeer.core.init.EntityInit;
import github.nitespring.reindeer.core.init.ItemInit;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.EntityLootSubProvider;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.IntRange;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.*;
import net.minecraft.world.level.storage.loot.predicates.EnchantmentActiveCheck;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.time.temporal.ValueRange;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import static net.minecraft.client.data.models.model.ItemModelUtils.when;

public class CustomEntityLootTableProvider extends EntityLootSubProvider {

    public CustomEntityLootTableProvider(HolderLookup.Provider registries) {
        super(FeatureFlags.DEFAULT_FLAGS, registries);
    }

    @Override
    protected Stream<EntityType<?>> getKnownEntityTypes() {

        return Stream.of(EntityInit.REINDEER.get());    }

    @Override
    public void generate() {

        HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        add(EntityInit.REINDEER.get(),
                LootTable.lootTable()
                //.apply(SetItemCountFunction.setCount(ConstantValue.exactly(5)))
                .withPool(LootPool.lootPool()
                        //.apply(LootItemFunctions.)
                        //.when(WeatherCheck.weather().setRaining(true))
                        .setRolls(UniformGenerator.between(1, 3))
                        .setBonusRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(ItemInit.RAW_REINDEER_MEAT)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 2.0F)))
                                //.apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.LOOTING)))
                        )
                        .add(LootItem.lootTableItem(ItemInit.REINDEER_ANTLER)
                                .apply(SetItemCountFunction.setCount(UniformGenerator.between(0.0F, 1.0F)))
                                //.apply(ApplyBonusCount.addUniformBonusCount(registrylookup.getOrThrow(Enchantments.LOOTING)))
                                //.apply(LimitCount.limitCount(IntRange.exact(2)))
                        )
                )
        );

    }


}
