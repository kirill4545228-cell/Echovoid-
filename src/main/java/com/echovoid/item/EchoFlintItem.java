package com.echovoid.item;

import com.echovoid.registry.ModBlocks;
import com.echovoid.world.EchoPortalHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

public class EchoFlintItem extends Item {
    public EchoFlintItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext ctx) {
        Level level = ctx.getLevel();
        BlockPos clicked = ctx.getClickedPos();
        if (level.isClientSide || !level.getBlockState(clicked).is(ModBlocks.ECHO_CRYSTAL_BLOCK.get())) {
            return InteractionResult.PASS;
        }
        BlockPos start = clicked.relative(ctx.getClickedFace());
        if (!EchoPortalHelper.tryCreate(level, start)) {
            return InteractionResult.PASS;
        }
        level.playSound(null, start, SoundEvents.FLINTANDSTEEL_USE, SoundSource.BLOCKS, 1.0F, 0.7F);
        Player player = ctx.getPlayer();
        if (player != null) {
            ctx.getItemInHand().hurtAndBreak(1, player, p -> p.broadcastBreakEvent(ctx.getHand()));
        }
        return InteractionResult.sidedSuccess(false);
    }
}
