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

package io.github.jamalam360.multiblocklib.api.components;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import io.github.jamalam360.multiblocklib.api.Multiblock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * A cardinal component that stores all created {@link Multiblock}s in the {@link World}.
 *
 * @author Jamalam360
 * @implSpec The implementation should also tick the {@link Multiblock}s using
 * {@link Multiblock#tick}, and save them to NBT.
 * @see io.github.jamalam360.multiblocklib.impl.components.MultiblockProviderImpl
 */
public interface MultiblockProvider extends ComponentV3 {
    Optional<Multiblock> getMultiblock(BlockPos pos);

    Multiblock[] getAllMultiblocks();

    @ApiStatus.Internal
    void addMultiblock(Multiblock multiblock);

    @ApiStatus.Internal
    void removeMultiblock(Multiblock multiblock);
}
