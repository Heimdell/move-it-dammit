package net.slowpnir.movedammit.item.custom;

import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.slowpnir.movedammit.block.ModBlocks;

import java.util.Arrays;

public class CamertoneItem extends Item {
    public CamertoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();

        if (world.isClient()) {
            out:do {
                BlockPos clicked = context.getBlockPos();

                if (performScan(world, clicked, player, 12)) break out;
                if (performScan(world, clicked, player, 24)) break out;
                if (performScan(world, clicked, player, 48)) break out;

                player.sendMessage(new TranslatableText("item.movedammit.camertone.not_found"), false);
            } while (false);
        }

        context.getStack().damage(1, player, player1 ->
            player1.sendToolBreakStatus(player1.getActiveHand())
        );

        return super.useOnBlock(context);
    }

    private boolean performScan(World world, BlockPos clicked, PlayerEntity player, int radius) {
        var r1 = radius + 1;
        var d  = radius / 2;
        for (var i = 0; i < 50; i++) {
            int x = (int) Math.floor(Math.random() * r1) - d;
            int y = (int) Math.floor(Math.random() * r1) - d;
            int z = (int) Math.floor(Math.random() * r1) - d;

            var scanPos = clicked.add(x, y, z);
            var scanned = world.getBlockState(scanPos).getBlock();

            if (isInteresting(scanned)) {
                tellLocation(scanPos, clicked, player, scanned);
                return true;
            }
        }
        return false;
    }

    private boolean isInteresting(Block block) {
        return Arrays.asList(
            Blocks.COAL_ORE,
            Blocks.COPPER_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.EMERALD_ORE,
            Blocks.GOLD_ORE,
            Blocks.LAPIS_ORE,
            Blocks.REDSTONE_ORE,
            Blocks.DEEPSLATE_COAL_ORE,
            Blocks.DEEPSLATE_COPPER_ORE,
            Blocks.DEEPSLATE_DIAMOND_ORE,
            Blocks.DEEPSLATE_EMERALD_ORE,
            Blocks.DEEPSLATE_GOLD_ORE,
            Blocks.DEEPSLATE_LAPIS_ORE,
            Blocks.DEEPSLATE_REDSTONE_ORE,
            Blocks.NETHER_GOLD_ORE,
            Blocks.NETHER_QUARTZ_ORE,
            Blocks.SPAWNER,
            Blocks.CHEST,
            Blocks.TRAPPED_CHEST,
            ModBlocks.TIN_ORE,
            ModBlocks.TIN_ORE_DEEPSLATE,
            ModBlocks.TIN_ORE_NETHERRACK
        ).contains(block);
    }

    private String where(BlockPos blockPos, BlockPos clicked) {
        var dist = blockPos.getSquaredDistance(clicked);

        var length =
            dist > 20 ? "far " :
            dist > 10 ? "somewhere " :
                        "near ";

        if (blockPos.compareTo(clicked) == 0) {
            return "right here";
        } else if (blockPos.isWithinDistance(clicked, 5)) {
            return "nearby";
        } else if (blockPos.getY() >= clicked.getY() + 5) {
            return length + "above";
        } else if (blockPos.getY() <= clicked.getY() - 5) {
            return length + "below";
        } else if (Math.abs(blockPos.getX() - clicked.getX()) > Math.abs(blockPos.getZ() - clicked.getZ())) {
            if (blockPos.getX() > clicked.getX()) {
                return length + "at East";
            } else {
                return length + "at West";
            }
        } else if (blockPos.getZ() > clicked.getZ()) {
            return length + "at South";
        } else {
            return length + "at North";
        }
    }

    private void tellLocation(BlockPos blockPos, BlockPos clicked, PlayerEntity player, Block found) {
        String text = "Found a " + found.asItem().getName().getString() + " " + where(blockPos, clicked);
        player.sendMessage(new LiteralText(text), false);
    }
}
