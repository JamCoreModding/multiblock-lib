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

package io.github.jamalam360.multiblocklib.impl;

import com.google.common.collect.Maps;
import io.github.jamalam360.Multiblock;
import io.github.jamalam360.components.CardinalComponentsInit;
import io.github.jamalam360.multiblocklib.api.MultiblockLib;
import io.github.jamalam360.multiblocklib.api.MultiblockProvider;
import io.github.jamalam360.pattern.MatchResult;
import io.github.jamalam360.pattern.MultiblockPattern;
import io.github.jamalam360.pattern.MultiblockPatterns;
import io.github.jamalam360.pattern.PatternTester;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Implementation of {@link MultiblockLib}. Should not be used directly by library users - access through {@link MultiblockLib#INSTANCE}.
 *
 * @author Jamalam360
 */
public class MultiblockLibImpl implements MultiblockLib {
    private static final Map<Identifier, Map<Character, Predicate<CachedBlockPosition>>> MULTIBLOCK_PATTERNS_TO_KEYS = Maps.newHashMap();
    private static final Map<Identifier, MultiblockProvider> MULTIBLOCK_PATTERNS_TO_PROVIDERS = Maps.newHashMap();

    @Override
    public void registerMultiblock(Identifier identifier, MultiblockProvider provider, Map<Character, Predicate<CachedBlockPosition>> keys) {
        MULTIBLOCK_PATTERNS_TO_PROVIDERS.put(identifier, provider);
        MULTIBLOCK_PATTERNS_TO_KEYS.put(identifier, keys);
    }

    @Override
    public boolean tryAssembleMultiblock(World world, BlockPos pos) {
        for (MultiblockPattern pattern : MultiblockPatterns.get()) {
            if (tryAssembleMultiblock(pattern, world, pos)) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean tryAssembleMultiblock(Identifier patternId, World world, BlockPos pos) {
        Optional<MultiblockPattern> optional = MultiblockPatterns.get(patternId);
        return optional.filter(multiblockPattern -> tryAssembleMultiblock(multiblockPattern, world, pos)).isPresent();
    }

    @Override
    public boolean tryAssembleMultiblock(MultiblockPattern pattern, World world, BlockPos pos) {
        if (CardinalComponentsInit.PROVIDER.get(world).getMultiblock(pos).isPresent()) {
            return true;
        }

        MatchResult result = PatternTester.tryMatchPattern(pos, world, pattern, MULTIBLOCK_PATTERNS_TO_KEYS.get(pattern.identifier()));
        if (result.matched()) {
            MultiblockProvider provider = MULTIBLOCK_PATTERNS_TO_PROVIDERS.get(pattern.identifier());

            if (provider != null) {
                Multiblock multiblock = provider.getMultiblock(pos, world, result);
                CardinalComponentsInit.PROVIDER.get(world).addMultiblock(multiblock);
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean tryDisassembleMultiblock(Multiblock multiblock, boolean forced) {
        boolean ableToDisassemble = multiblock.onDisassemble(null, forced);
        if (ableToDisassemble) {
            CardinalComponentsInit.PROVIDER.get(multiblock.getWorld()).removeMultiblock(multiblock);
            return true;
        }

        return false;
    }

    @Override
    public Optional<Multiblock> getMultiblock(World world, BlockPos pos) {
        return CardinalComponentsInit.PROVIDER.get(world).getMultiblock(pos);
    }
}
