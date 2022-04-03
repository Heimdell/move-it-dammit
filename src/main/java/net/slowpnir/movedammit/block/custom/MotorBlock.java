package net.slowpnir.movedammit.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.slowpnir.movedammit.utils.ABlock;
import net.slowpnir.movedammit.utils.Log;
import net.slowpnir.movedammit.utils.Movement;
import net.slowpnir.movedammit.utils.OrdMonoid;

import java.util.List;
import java.util.function.BiFunction;

import static net.minecraft.util.math.Direction.*;
import static net.slowpnir.movedammit.utils.ClassifyBlocks.*;

public class MotorBlock extends Block {
    public static final DirectionProperty FACING  = DirectionProperty.of("facing");
    public static final DirectionProperty MOVING  = DirectionProperty.of("moving");
    public static final BooleanProperty   POWERED = BooleanProperty  .of("powered");

    public MotorBlock(Settings settings) {
        super(settings);
    }

    @Override
    public void onSteppedOn(World world, BlockPos pos, BlockState state, Entity entity) {

        if (!world.isClient()) {
            if (entity instanceof LivingEntity living) {
                living.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 300));
            }
        }

        super.onSteppedOn(world, pos, state, entity);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {

        state = normalise(player, state);
        world.setBlockState(pos, state);

        if (!world.isClient() /*&& hand == Hand.MAIN_HAND*/) {

            Log.info(player, "Hand = %s".formatted(hand));

            tryMove(state, world, pos, player);
            /*
            var old = state.get(MOVING);
            var cur = old.rotateYClockwise();
            var state1 = state.with(MOVING, cur);
            world.setBlockState(pos, state1, NOTIFY_ALL);
            */
        } else {
            Log.info(player, "State was facing = %s, moving = %s, powered = %s"
                .formatted(state.get(FACING), state.get(MOVING), state.get(POWERED))
            );
        }

        return ActionResult.SUCCESS;
    }

    private void tryMove(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        Movement<BlockPos, Direction> movement = new Movement<>() {
            @Override
            public BlockPos move(BlockPos p, Direction d) {
                return p.offset(d);
            }

            @Override
            public int compareByDir(Direction dir, BlockPos o1, BlockPos o2) {
                BiFunction<BlockPos, BlockPos, Integer> byZ = (p1, p2) -> p1.getZ() - p2.getZ();
                BiFunction<BlockPos, BlockPos, Integer> byX = (p1, p2) -> p1.getX() - p2.getX();
                BiFunction<BlockPos, BlockPos, Integer> byY = (p1, p2) -> p1.getY() - p2.getY();

                return switch (dir) {
                    case DOWN  -> OrdMonoid.mconcat(byY.apply(o1, o2), byX.apply(o1, o2), byZ.apply(o1, o2));
                    case UP    -> OrdMonoid.mconcat(byY.apply(o2, o1), byX.apply(o1, o2), byZ.apply(o1, o2));
                    case NORTH -> OrdMonoid.mconcat(byZ.apply(o1, o2), byY.apply(o1, o2), byX.apply(o1, o2));
                    case SOUTH -> OrdMonoid.mconcat(byZ.apply(o2, o1), byY.apply(o1, o2), byX.apply(o1, o2));
                    case WEST  -> OrdMonoid.mconcat(byX.apply(o1, o2), byY.apply(o1, o2), byZ.apply(o1, o2));
                    case EAST  -> OrdMonoid.mconcat(byX.apply(o2, o1), byY.apply(o1, o2), byZ.apply(o1, o2));
                };
            }

            @Override
            public void moveAtTo(BlockPos p, Direction d) {
                var state = world.getBlockState(p);
                world.setBlockState(p, Blocks.AIR.getDefaultState());
                world.setBlockState(p.offset(d), state);
            }

            @Override
            public ABlock<Direction> get(BlockPos p) {
                BlockState state = world.getBlockState(p);
                var block = state.getBlock();

                return isAir(block) ?        new ABlock.Other<>(true, true)
                     : isFrame(block) ?      new ABlock.Frame<>(List.of(NORTH, SOUTH, WEST, EAST, UP, DOWN))
                     : isMotor(block) ?      new ABlock.Motor<>(state.get(FACING))
                     : isBedrock(block) ?    new ABlock.Other<>(true, false)
                     : isDecoration(block) ? new ABlock.Other<>(false, true)
                     :                       new ABlock.Other<>(false, false);
            }
        };

        var ship = movement.collect(player, pos, state.get(FACING), state.get(MOVING), 1024);

        if (ship instanceof Movement.Ship.Good<BlockPos> ship1) {
            Log.info(player, "Good %s".formatted(ship1.Volume.toString()));
            movement.performMovement(ship1.Volume, state.get(MOVING));
        }

        if (ship instanceof Movement.Ship.Blocked<BlockPos> blocks) {
            Log.info(player, "Blocked %s".formatted(blocks.Roadblocks.toString()));
            for (var pt : blocks.Roadblocks) {
                world.addParticle(ParticleTypes.EXPLOSION, pt.getX(), pt.getY(), pt.getZ(), 0, 1, 0);
            }
        }

        if (ship instanceof Movement.Ship.TooBig<BlockPos> tooBig) {
            Log.info(player, "Too big %s".formatted(tooBig.Exceeding.toString()));
            for (var pt : tooBig.Exceeding) {
                world.addParticle(ParticleTypes.EXPLOSION, pt.getX(), pt.getY(), pt.getZ(), 0, 1, 0);
            }
        }
    }

    private BlockState normalise(PlayerEntity player, BlockState state) {
        if (state.get(FACING) == state.get(MOVING)) {
            Log.info(player, "Invalid state (facing = %s, moving = %s, powered = %s) -> top * north"
                .formatted(state.get(FACING), state.get(MOVING), state.get(POWERED))
            );
            return state.with(FACING, Direction.UP).with(MOVING, NORTH);
        }
        return state;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, MOVING, POWERED);
        super.appendProperties(builder);
    }

    @Override
    public boolean onSyncedBlockEvent(BlockState state, World world, BlockPos pos, int type, int data) {
        return super.onSyncedBlockEvent(state, world, pos, type, data);
    }
}
