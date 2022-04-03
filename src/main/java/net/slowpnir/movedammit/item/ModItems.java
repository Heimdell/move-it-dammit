package net.slowpnir.movedammit.item;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.Item;
import net.minecraft.item.ToolMaterials;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.slowpnir.movedammit.MoveDammitMod;
import net.slowpnir.movedammit.item.custom.CamertoneItem;
import net.slowpnir.movedammit.item.custom.WrenchItem;

import static net.slowpnir.movedammit.item.ModItemGroup.MOVE_DAMMIT;

public class ModItems {
    public static final Item TIN_INGOT =
        registerItem("tin_ingot", new Item (new FabricItemSettings()
            .group(MOVE_DAMMIT)
        ));

    public static final Item TIN_NUGGET =
        registerItem("tin_nugget", new Item (new FabricItemSettings()
            .group(MOVE_DAMMIT)
        ));

    public static final Item TIN_RAW =
        registerItem("tin_raw", new Item (new FabricItemSettings()
            .group(MOVE_DAMMIT)
        ));

    public static final Item CAMERTONE =
        registerItem("camertone", new CamertoneItem(new FabricItemSettings()
            .group(MOVE_DAMMIT)
            .maxDamage(128)
        ));

    public static final Item SMOL_COAL =
        registerItem("smol_coal", new Item (new FabricItemSettings()
            .group(MOVE_DAMMIT)
        ));

    public static final Item SMOL_CHARCOAL =
        registerItem("smol_charcoal", new Item (new FabricItemSettings()
            .group(MOVE_DAMMIT)
        ));

    public static final Item WRENCH =
        registerItem("wrench", new WrenchItem(ToolMaterials.IRON, new FabricItemSettings()
            .group(MOVE_DAMMIT)
        ));

    public static Item registerItem(String name, Item item) {
        return Registry.register(Registry.ITEM, new Identifier(MoveDammitMod.MOD_ID, name), item);
    }

    public static void registerItems() {
        MoveDammitMod.LOGGER.info("Registering items for {}", MoveDammitMod.MOD_ID);
    }
}
