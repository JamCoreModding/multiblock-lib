package io.github.jamalam360;

import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Jamalam360
 */
public class MultiblockLib {
    protected static final Map<BlockPattern, MultiblockProvider> MULTIBLOCKS = new HashMap<>();

    public static void registerMultiblock(MultiblockProvider provider, BlockPattern pattern) {
        MULTIBLOCKS.put(pattern, provider);
    }

    public static boolean tryAssembleMultiblock(World world, BlockPos pos) {
        for (Map.Entry<BlockPattern, MultiblockProvider> entry : MULTIBLOCKS.entrySet()) {
            if (entry.getKey().searchAround(world, pos) != null) {
                System.out.println("Found multiblock at " + pos);
                return true;
            }
        }

        return false;
    }

    @FunctionalInterface
    public interface MultiblockProvider {
        Multiblock getMultiblock();
    }
}
