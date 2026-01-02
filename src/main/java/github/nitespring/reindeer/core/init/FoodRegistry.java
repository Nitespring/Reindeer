package github.nitespring.reindeer.core.init;

import net.minecraft.world.food.FoodProperties;

public class FoodRegistry {

    public static final FoodProperties RAW_REINDEER = (new FoodProperties.Builder())
            .nutrition(3)
            .saturationModifier(0.3F)
            .build();
    public static final FoodProperties COOKED_REINDEER = (new FoodProperties.Builder())
            .nutrition(7)
            .saturationModifier(0.8F)
            .build();

}
