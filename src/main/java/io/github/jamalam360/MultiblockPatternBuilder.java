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
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Jamalam360
 */
public class MultiblockPatternBuilder {
    private final List<String[]> layers = new ArrayList<>();
    private final Map<Character, Predicate<CachedBlockPosition>> charMap = Maps.newHashMap();

    private MultiblockPatternBuilder() {
    }

    public static MultiblockPatternBuilder start() {
        return new MultiblockPatternBuilder();
    }

    public MultiblockPatternBuilder layer(String... rows) {
        layers.add(rows);
        return this;
    }

    public MultiblockPatternBuilder where(char key, Predicate<CachedBlockPosition> predicate) {
        this.charMap.put(key, predicate);
        return this;
    }

    public BlockPattern build() {
        BlockPatternBuilder builder = BlockPatternBuilder.start();
        int height = layers.size();
        int width = layers.get(0)[0].length();
        int depth = layers.get(0).length;

        for (int h = 0; h < height; h++) {
            ArrayList<String> list = new ArrayList<>();
            for (int d = 0; d < depth; d++) {
                list.add(layers.get(h)[d]);
            }
            builder.aisle(list.toArray(new String[0]));
        }

        for (Map.Entry<Character, Predicate<CachedBlockPosition>> entry : charMap.entrySet()) {
            builder.where(entry.getKey(), entry.getValue());
        }

        return builder.build();
    }
}
