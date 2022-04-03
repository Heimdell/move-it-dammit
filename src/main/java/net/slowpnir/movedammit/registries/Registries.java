package net.slowpnir.movedammit.registries;

import net.fabricmc.fabric.api.registry.FuelRegistry;
import net.minecraft.item.Items;
import net.slowpnir.movedammit.item.ModItems;

import static net.slowpnir.movedammit.MoveDammitMod.LOGGER;

public class Registries {
    public static void registerVarious() {
        registerFuels();
    }

    private static void registerFuels() {
        LOGGER.info("Registering fuels");
        FuelRegistry registry = FuelRegistry.INSTANCE;

        registry.add(ModItems.SMOL_CHARCOAL, 200);
        registry.add(ModItems.SMOL_COAL, 200);
    }
}
