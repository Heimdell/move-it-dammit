package net.slowpnir.movedammit.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.slowpnir.movedammit.MoveDammitMod;
import net.slowpnir.movedammit.block.custom.MotorBlock;

import static net.slowpnir.movedammit.MoveDammitMod.MOD_ID;
import static net.slowpnir.movedammit.item.ModItemGroup.MOVE_DAMMIT;

public class ModBlocks {
    public static final Block TIN_BLOCK = registerBlock("tin_block",
            new Block(
                    FabricBlockSettings.of(Material.METAL)
                            .strength(2f)
                            .requiresTool()
            ),
            MOVE_DAMMIT
    );

    public static final Block TIN_ORE = registerBlock("tin_ore",
            new Block(
                    FabricBlockSettings.of(Material.STONE)
                            .strength(2f)
                            .requiresTool()
            ),
            MOVE_DAMMIT
    );

    public static final Block TIN_ORE_DEEPSLATE = registerBlock("tin_ore_deepslate",
            new Block(
                    FabricBlockSettings.of(Material.STONE)
                            .strength(2f)
                            .requiresTool()
            ),
            MOVE_DAMMIT
    );

    public static final Block TIN_ORE_NETHERRACK = registerBlock("tin_ore_netherrack",
        new Block(
            FabricBlockSettings.of(Material.STONE)
                .strength(2f)
                .requiresTool()
        ),
        MOVE_DAMMIT
    );

    public static final Block MOTOR = registerBlock("motor",
        new MotorBlock(
            FabricBlockSettings.of(Material.WOOD)
                .strength(2f)
                .requiresTool()
        ),
        MOVE_DAMMIT
    );

    private static Block registerBlock(String name, Block block, ItemGroup group) {
        registerBlockItem(name, block, group);
        return Registry.register(Registry.BLOCK, new Identifier(MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block, ItemGroup group) {
        return Registry.register(Registry.ITEM, new Identifier(MOD_ID, name),
            new BlockItem(block, new FabricItemSettings().group(group))
        );
    }

    public static void registerBlocks() {
        MoveDammitMod.LOGGER.info("Registering items for {}", MOD_ID);
    }
}
