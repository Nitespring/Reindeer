package github.nitespring.reindeer.common.entity.mob;

import github.nitespring.reindeer.core.tags.CustomItemTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityReference;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.Nullable;

public class Reindeer extends AbstractReindeer{


    public Reindeer(EntityType<? extends AbstractReindeer> entityType, Level level) {
        super(entityType, level);
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(CustomItemTags.REINDEER_FOOD);
    }


}
