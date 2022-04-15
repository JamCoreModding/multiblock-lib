/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jamalam360
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360;

import com.google.common.collect.Maps;
import io.github.jamalam360.components.CardinalComponentsInit;
import io.github.jamalam360.pattern.MatchResult;
import io.github.jamalam360.pattern.MultiblockPattern;
import io.github.jamalam360.pattern.MultiblockPatterns;
import io.github.jamalam360.pattern.PatternTester;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * @author Jamalam360
 */
public class MultiblockLib {
    //TODO: Split MultiblockLib into API and implementation.
    //TODO: Remove debug logging
    //TODO: Proper logging of necessary information
    //TODO: Disassemble multiblocks when a block inside them is broken
    //TODO: Flesh out the multiblock API with more methods (click, neighbor state change, etc.)
    //TODO: Implement hit-boxes for multiblocks
    //TODO: Place from any block on the multiblock
    //TODO: MultiblockContext record
    //TODO: Multiblock NBT methods (can probably use CCA)
    //TODO: Count the number of each block in the multiblock (util method)
    //TODO: Move implementation and API into separate packages
    //TODO: Icon

    protected static final Map<Identifier, Map<Character, Predicate<CachedBlockPosition>>> MULTIBLOCK_PATTERNS_TO_KEYS = Maps.newHashMap();
    protected static final Map<Identifier, MultiblockProvider> MULTIBLOCK_PATTERNS_TO_PROVIDERS = Maps.newHashMap();

    public static void registerMultiblock(Identifier identifier, MultiblockProvider provider, Map<Character, Predicate<CachedBlockPosition>> keys) {
        MULTIBLOCK_PATTERNS_TO_PROVIDERS.put(identifier, provider);
        MULTIBLOCK_PATTERNS_TO_KEYS.put(identifier, keys);
    }


    public static boolean tryAssembleMultiblock(World world, BlockPos pos) {
        if (!world.isClient) {
            for (MultiblockPattern pattern : MultiblockPatterns.get()) {
                if (tryAssembleMultiblock(pattern, world, pos)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean tryAssembleMultiblock(List<MultiblockPattern> possiblePatterns, World world, BlockPos pos) {
        if (!world.isClient) {
            for (MultiblockPattern pattern : possiblePatterns) {
                if (tryAssembleMultiblock(pattern, world, pos)) {
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean tryAssembleMultiblock(Identifier patternId, World world, BlockPos pos) {
        if (!world.isClient) {
            Optional<MultiblockPattern> optional = MultiblockPatterns.get(patternId);
            if (optional.isPresent()) {
                return tryAssembleMultiblock(optional.get(), world, pos);
            }
        }

        return false;
    }

    public static boolean tryAssembleMultiblock(MultiblockPattern pattern, World world, BlockPos pos) {
        if (!world.isClient) {
            MatchResult result = PatternTester.tryMatchPattern(pos, world, pattern, MULTIBLOCK_PATTERNS_TO_KEYS.get(pattern.identifier()));
            if (result.matched()) {
                System.out.println("Found multiblock at " + pos + " with height " + result.height() + ", width " + result.width() + ", and depth " + result.depth());

                MultiblockProvider provider = MULTIBLOCK_PATTERNS_TO_PROVIDERS.get(pattern.identifier());

                if (provider != null) {
                    Multiblock multiblock = provider.getMultiblock(pos, world, result);
                    CardinalComponentsInit.PROVIDER.get(world).addMultiblock(multiblock);
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * @param multiblock The multiblock to try to disassemble
     * @param forced     Whether this multiblock is being disassembled forcefully (i.e. one of its blocks was broken)
     * @return Whether the multiblock was successfully disassembled
     */
    public static boolean tryDisassembleMultiblock(Multiblock multiblock, boolean forced) {
        if (!multiblock.getWorld().isClient) {
            boolean ableToDisassemble = multiblock.onDisassemble(null, true);
            if (ableToDisassemble) {
                CardinalComponentsInit.PROVIDER.get(multiblock.getWorld()).removeMultiblock(multiblock);
                return true;
            }
        }

        return false;
    }

    public static boolean isPartOfMultiblock(World world, BlockPos pos) {
        return getMultiblock(world, pos).isPresent();
    }

    public static Optional<Multiblock> getMultiblock(World world, BlockPos pos) {
        return CardinalComponentsInit.PROVIDER.get(world).getMultiblock(pos);
    }

    @FunctionalInterface
    public interface MultiblockProvider {
        Multiblock getMultiblock(BlockPos pos, World world, MatchResult match);
    }
}
