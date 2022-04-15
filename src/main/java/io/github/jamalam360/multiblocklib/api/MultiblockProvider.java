package io.github.jamalam360.multiblocklib.api;

import io.github.jamalam360.Multiblock;
import io.github.jamalam360.pattern.MatchResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * @author Jamalam360
 */
public interface MultiblockProvider {
    Multiblock getMultiblock(BlockPos pos, World world, MatchResult match);
}
