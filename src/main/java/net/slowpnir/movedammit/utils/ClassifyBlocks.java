package net.slowpnir.movedammit.utils;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.slowpnir.movedammit.block.ModBlocks;

public class ClassifyBlocks {
    public static boolean isAir(Block b) {
        return b == Blocks.AIR || b == Blocks.GLASS;
    }

    public static boolean isBedrock(Block b) {
        return b == Blocks.BEDROCK || b == Blocks.OBSIDIAN;
    }

    public static boolean isDecoration(Block b) {
        return b == Blocks.REDSTONE_LAMP;
    }

    public static boolean isFrame(Block b) {
        return b == Blocks.OAK_WOOD || b == Blocks.JUNGLE_WOOD;
    }

    public static boolean isMotor(Block b) {
        return b == ModBlocks.MOTOR;
    }
}
