package com.echovoid.world;

import com.echovoid.block.EchoPortalBlock;
import com.echovoid.registry.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class EchoPortalHelper {
    /** Fixed arrival point (standing position) inside the Echoing Abyss. */
    public static final BlockPos ARRIVAL = new BlockPos(0, 100, 0);
    private static final int MAX = 21;

    private static boolean isFrame(Level level, BlockPos pos) {
        return level.getBlockState(pos).is(ModBlocks.ECHO_CRYSTAL_BLOCK.get());
    }

    private static boolean isEmpty(Level level, BlockPos pos) {
        return level.getBlockState(pos).isAir();
    }

    /** Looks for an echo crystal frame around start (an air block) and lights it. */
    public static boolean tryCreate(Level level, BlockPos start) {
        return tryCreate(level, start, Direction.Axis.X) || tryCreate(level, start, Direction.Axis.Z);
    }

    private static boolean tryCreate(Level level, BlockPos start, Direction.Axis axis) {
        if (!isEmpty(level, start)) return false;
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        Direction left = right.getOpposite();

        BlockPos p = start;
        int guard = 0;
        while (isEmpty(level, p.below()) && guard++ < MAX) p = p.below();
        if (!isFrame(level, p.below())) return false;

        guard = 0;
        while (isEmpty(level, p.relative(left)) && guard++ < MAX) p = p.relative(left);
        if (!isFrame(level, p.relative(left))) return false;

        int width = 0;
        while (width <= MAX && isEmpty(level, p.relative(right, width))) width++;
        if (width < 2 || width > MAX || !isFrame(level, p.relative(right, width))) return false;

        int height = 0;
        while (height <= MAX && isEmpty(level, p.above(height))) height++;
        if (height < 3 || height > MAX) return false;

        for (int i = 0; i < width; i++) {
            BlockPos column = p.relative(right, i);
            if (!isFrame(level, column.below()) || !isFrame(level, column.above(height))) return false;
        }
        for (int j = 0; j < height; j++) {
            if (!isFrame(level, p.relative(left).above(j)) || !isFrame(level, p.relative(right, width).above(j))) return false;
            for (int i = 0; i < width; i++) {
                if (!isEmpty(level, p.relative(right, i).above(j))) return false;
            }
        }

        fillPortal(level, p, axis, width, height);
        return true;
    }

    private static void fillPortal(Level level, BlockPos bottomLeft, Direction.Axis axis, int width, int height) {
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        BlockState portal = ModBlocks.ECHO_PORTAL.get().defaultBlockState().setValue(EchoPortalBlock.AXIS, axis);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                level.setBlock(bottomLeft.relative(right, i).above(j), portal,
                        Block.UPDATE_CLIENTS | Block.UPDATE_KNOWN_SHAPE);
            }
        }
    }

    /** Builds a frame (echo crystal blocks) plus the portal inside it. bottomLeft = lowest interior block. */
    public static void buildFrame(Level level, BlockPos bottomLeft, Direction.Axis axis, int width, int height) {
        Direction right = axis == Direction.Axis.X ? Direction.EAST : Direction.SOUTH;
        BlockState frame = ModBlocks.ECHO_CRYSTAL_BLOCK.get().defaultBlockState();
        for (int i = -1; i <= width; i++) {
            for (int j = -1; j <= height; j++) {
                boolean edgeX = i == -1 || i == width;
                boolean edgeY = j == -1 || j == height;
                if (edgeX && edgeY) continue; // corners are not needed
                if (edgeX || edgeY) {
                    level.setBlock(bottomLeft.relative(right, i).above(j), frame, Block.UPDATE_ALL);
                }
            }
        }
        fillPortal(level, bottomLeft, axis, width, height);
    }

    /** Makes sure the arrival platform with a return portal exists in the Abyss and returns the standing position. */
    public static BlockPos ensureArrival(ServerLevel level) {
        BlockPos portalPos = ARRIVAL.offset(-1, 0, 3);
        level.getChunkAt(ARRIVAL); // load / generate
        if (!level.getBlockState(portalPos).is(ModBlocks.ECHO_PORTAL.get())) {
            BlockState stone = ModBlocks.ECHO_STONE.get().defaultBlockState();
            for (int dx = -4; dx <= 4; dx++) {
                for (int dz = -4; dz <= 4; dz++) {
                    level.setBlock(ARRIVAL.offset(dx, -1, dz), stone, Block.UPDATE_ALL);
                    for (int dy = 0; dy <= 5; dy++) {
                        level.setBlock(ARRIVAL.offset(dx, dy, dz), Blocks.AIR.defaultBlockState(), Block.UPDATE_ALL);
                    }
                }
            }
            buildFrame(level, portalPos, Direction.Axis.X, 2, 3);
        }
        return ARRIVAL;
    }
}
