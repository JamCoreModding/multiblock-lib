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

package io.github.jamalam360.components;

import com.google.common.collect.Maps;
import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import io.github.jamalam360.Multiblock;
import io.github.jamalam360.MultiblockLib;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author Jamalam360
 */
public class MultiblockProviderImpl implements MultiblockProvider, ServerTickingComponent, AutoSyncedComponent {
    private final World provider;
    private final Map<BlockPos[], Multiblock> MULTIBLOCKS = Maps.newHashMap();

    protected MultiblockProviderImpl(World provider) {
        this.provider = provider;
    }

    @Override
    public void serverTick() {
        for (Multiblock multiblock : getAllMultiblocks()) {
            multiblock.tick(null);
        }
    }

    @ApiStatus.Internal
    @Override
    public void addMultiblock(Multiblock multiblock) {
        List<BlockPos> positions = new ArrayList<>();
        BlockPos.stream(multiblock.getBox()).forEach(pos -> positions.add(pos.toImmutable()));

        MULTIBLOCKS.put(positions.toArray(new BlockPos[0]), multiblock);
        System.out.println(MULTIBLOCKS);
    }

    @ApiStatus.Internal
    @Override
    public void removeMultiblock(Multiblock multiblock) {
        final List<BlockPos[]> keys = new ArrayList<>();
        MULTIBLOCKS.forEach((key, value) -> {
            if (value == multiblock) {
                keys.add(key);
            }
        });

        for (BlockPos[] key : keys) {
            MULTIBLOCKS.remove(key);
        }
    }

    @Override
    public Optional<Multiblock> getMultiblock(BlockPos pos) {
        List<Map.Entry<BlockPos[], Multiblock>> filtered = MULTIBLOCKS.entrySet().stream()
                .filter(entry -> Arrays.asList(entry.getKey()).contains(pos))
                .toList();

        if (filtered.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(filtered.get(0).getValue());
        }
    }

    @Override
    public Multiblock[] getAllMultiblocks() {
        return MULTIBLOCKS.values().toArray(new Multiblock[0]);
    }

    @Override
    public void writeToNbt(@NotNull NbtCompound tag) {
        NbtCompound compound = new NbtCompound();

        int multiblockNumber = 0;
        for (Map.Entry<BlockPos[], Multiblock> entry : MULTIBLOCKS.entrySet()) {
            multiblockNumber++;
            NbtCompound multiblockTag = new NbtCompound();
            multiblockTag.putIntArray("BottomLeft", new int[]{entry.getValue().getBottomLeftPos().getX(), entry.getValue().getBottomLeftPos().getY(), entry.getValue().getBottomLeftPos().getZ()});
            multiblockTag.putString("PatternIdentifier", entry.getValue().getMatchResult().pattern().identifier().toString());
            compound.put("Multiblock" + multiblockNumber, multiblockTag);
        }
        compound.putInt("MultiblockLength", multiblockNumber);

        tag.put("Multiblocks", compound);
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        NbtCompound compound = tag.getCompound("Multiblocks");
        int multiblockLength = compound.getInt("MultiblockLength");

        for (int i = 1; i <= multiblockLength; i++) {
            NbtCompound multiblockTag = compound.getCompound("Multiblock" + i);
            int[] bottomLeftArr = multiblockTag.getIntArray("BottomLeft");
            BlockPos bottomLeft = new BlockPos(bottomLeftArr[0], bottomLeftArr[1], bottomLeftArr[2]);
            Identifier identifier = new Identifier(multiblockTag.getString("PatternIdentifier"));

            MultiblockLib.tryAssembleMultiblock(identifier, provider, bottomLeft);
        }
    }
}
