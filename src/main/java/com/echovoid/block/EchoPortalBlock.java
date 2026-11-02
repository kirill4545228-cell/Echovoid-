package com.echovoid.block;

import com.echovoid.registry.ModBlocks;
import com.echovoid.registry.ModDimensions;
import com.echovoid.world.EchoTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraft.resources.ResourceKey;

public class EchoPortalBlock extends Block {
    public static final EnumProperty<Direction.Axis> AXIS = BlockStateProperties.HORIZONTAL_AXIS;
    protected static final VoxelShape X_SHAPE = Block.box(0.0D, 0.0D, 6.0D, 16.0D, 16.0D, 10.0D);
    protected static final VoxelShape Z_SHAPE = Block.box(6.0D, 0.0D, 0.0D, 10.0D, 16.0D, 16.0D);

    public EchoPortalBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AXIS, Direction.Axis.X));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.getValue(AXIS) == Direction.Axis.Z ? Z_SHAPE : X_SHAPE;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AXIS);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction dir, BlockState neighborState,
                                  LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        Direction.Axis dirAxis = dir.getAxis();
        Direction.Axis portalAxis = state.getValue(AXIS);
        // Neighbours inside the portal plane must be portal or frame, otherwise the portal collapses.
        if (dirAxis == Direction.Axis.Y || dirAxis == portalAxis) {
            if (!neighborState.is(this) && !neighborState.is(ModBlocks.ECHO_CRYSTAL_BLOCK.get())) {
                return Blocks.AIR.defaultBlockState();
            }
        }
        return super.updateShape(state, dir, neighborState, level, pos, neighborPos);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter level, BlockPos pos, BlockState state) {
        return ItemStack.EMPTY;
    }

    @Override
    public void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (!(level instanceof ServerLevel serverLevel)) return;
        if (entity.isPassenger() || entity.isVehicle() || !entity.canChangeDimensions()) return;

        if (entity.isOnPortalCooldown()) {
            entity.setPortalCooldown();
            return;
        }
        ResourceKey<Level> targetKey = level.dimension() == ModDimensions.ABYSS ? Level.OVERWORLD : ModDimensions.ABYSS;
        ServerLevel target = serverLevel.getServer().getLevel(targetKey);
        if (target == null) return;

        entity.setPortalCooldown();
        entity.changeDimension(target, new EchoTeleporter());
    }
}
