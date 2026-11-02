package com.echovoid.world;

import com.echovoid.registry.ModDimensions;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.portal.PortalInfo;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.ITeleporter;

public class EchoTeleporter implements ITeleporter {
    @Override
    public PortalInfo getPortalInfo(Entity entity, ServerLevel dest, Function<ServerLevel, PortalInfo> defaultPortalInfo) {
        if (dest.dimension() == ModDimensions.ABYSS) {
            BlockPos arrival = EchoPortalHelper.ensureArrival(dest);
            return new PortalInfo(new Vec3(arrival.getX() + 0.5D, arrival.getY(), arrival.getZ() + 0.5D),
                    Vec3.ZERO, 0.0F, 0.0F);
        }
        // Back to the overworld: world spawn, on the surface.
        BlockPos spawn = dest.getSharedSpawnPos();
        BlockPos top = dest.getHeightmapPos(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, spawn);
        return new PortalInfo(new Vec3(top.getX() + 0.5D, top.getY(), top.getZ() + 0.5D),
                Vec3.ZERO, entity.getYRot(), entity.getXRot());
    }
}
