package net.slowpnir.movedammit.item;

import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import static net.slowpnir.movedammit.MoveDammitMod.MOD_ID;

public class ModItemGroup {
    public static final ItemGroup MOVE_DAMMIT = FabricItemGroupBuilder.build(
        new Identifier(MOD_ID, "move_dammit"), () ->
            new ItemStack(ModItems.TIN_INGOT)
    );
}
