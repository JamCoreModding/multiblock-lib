package io.github.jamalam360.multiblocklib.api;

import io.github.jamalam360.Multiblock;
import io.github.jamalam360.multiblocklib.impl.MultiblockLibImpl;
import io.github.jamalam360.pattern.MultiblockPattern;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Jamalam360
 */
public interface MultiblockLib {
    //TODO: Remove debug logging
    //TODO: Proper logging of necessary information
    //TODO: Flesh out the multiblock API with more methods (click, neighbor state change, etc.)
    //TODO: Assemble from any block on the multiblock
    //TODO: MultiblockContext record
    //TODO: Count the number of each block in the multiblock (util method)
    //TODO: Move implementation and API into separate packages
    //TODO: Icon
    //TODO: Use gametest API
    //TODO: Throw if multiblock registered with JSON is not registered in code

    MultiblockLib INSTANCE = new MultiblockLibImpl();

    /**
     * @param identifier The {@link Identifier} of the {@link io.github.jamalam360.pattern.MultiblockPattern} to register.
     * @param provider   The {@link MultiblockProvider} to register.
     * @param keys       The {@link Map} of keys to use for the {@link io.github.jamalam360.pattern.MultiblockPattern}.
     */
    void registerMultiblock(Identifier identifier, MultiblockProvider provider, Map<Character, Predicate<CachedBlockPosition>> keys);

    /**
     * Checks all registered multiblock patterns for a match. It is more efficient to use one of
     * the other methods below if you have a pattern ID.
     *
     * @param world The {@link World} to use.
     * @param pos   The {@link BlockPos} of the bottom left corner of the multiblock.
     * @return {@code true} if the multiblock was successfully assembled, {@code false} otherwise.
     */
    boolean tryAssembleMultiblock(World world, BlockPos pos);


    /**
     * @param patternId The {@link Identifier} of the {@link MultiblockPattern} to test for.
     * @param world     The {@link World} to use.
     * @param pos       The {@link BlockPos} of the bottom left corner of the multiblock.
     * @return {@code true} if the multiblock was successfully assembled, {@code false} otherwise.
     */
    boolean tryAssembleMultiblock(Identifier patternId, World world, BlockPos pos);


    /**
     * @param pattern The {@link MultiblockPattern} to test for.
     * @param world   The {@link World} to use.
     * @param pos     The {@link BlockPos} of the bottom left corner of the multiblock.
     * @return {@code true} if the multiblock was successfully assembled, {@code false} otherwise.
     */
    boolean tryAssembleMultiblock(MultiblockPattern pattern, World world, BlockPos pos);

    /**
     * @param multiblock The {@link Multiblock} to try to disassemble.
     * @param forced     {@code true} if the multiblock should be disassembled forcefully. If a
     *                   {@link Multiblock} is not disassembled forcefully, it will only
     *                   be disassembled if the return value of {@link Multiblock#onDisassemble}
     *                   is {@code true}.
     * @return {@code true} if the multiblock was successfully disassembled, {@code false} otherwise.
     */
    boolean tryDisassembleMultiblock(Multiblock multiblock, boolean forced);


    /**
     * Checks whether the given {@link BlockPos} is within the bounds of a multiblock.
     *
     * @param world The {@link World} to use.
     * @param pos   The {@link BlockPos} of the block to check.
     * @return {@code true} if the block is within the bounds of a multiblock, {@code false} otherwise.
     */
    Optional<Multiblock> getMultiblock(World world, BlockPos pos);
}
