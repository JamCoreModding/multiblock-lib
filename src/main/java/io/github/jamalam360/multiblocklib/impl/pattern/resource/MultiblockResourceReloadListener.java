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

package io.github.jamalam360.multiblocklib.impl.pattern.resource;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.github.jamalam360.multiblocklib.impl.MultiblockLogger;
import io.github.jamalam360.multiblocklib.api.pattern.MultiblockPatterns;
import io.github.jamalam360.multiblocklib.api.pattern.MultiblockPattern;
import net.fabricmc.fabric.api.resource.IdentifiableResourceReloadListener;
import net.minecraft.resource.JsonDataLoader;
import net.minecraft.resource.ResourceManager;
import net.minecraft.util.Identifier;
import net.minecraft.util.profiler.Profiler;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Adds all the JSON files in the {@code data/[namespace]/multiblock_patterns} directory to the
 * {@link MultiblockPatterns} list.
 *
 * @author Jamalam360
 */
public class MultiblockResourceReloadListener extends JsonDataLoader implements IdentifiableResourceReloadListener {
    public MultiblockResourceReloadListener() {
        super(new Gson(), "multiblock_patterns");
    }

    @Override
    public void apply(Map<Identifier, JsonElement> prepared, ResourceManager manager, Profiler profiler) {
        MultiblockPatterns.INSTANCE.clear();
        AtomicInteger count = new AtomicInteger();
        prepared.forEach((id, element) -> {
            if (element.isJsonObject()) {
                MultiblockPatterns.INSTANCE.add(MultiblockPattern.deserialize(id, element));
                count.getAndIncrement();
            } else {
                MultiblockLogger.LOGGER.warn("Failed to load pattern {}", id);
                MultiblockLogger.INSTANCE.warn("MultiblockPattern {} is not a JSON object", id);
            }
        });
        MultiblockLogger.INSTANCE.info("Loaded {} multiblock patterns", count.get());
    }

    @Override
    public Identifier getFabricId() {
        return new Identifier("multiblocklib", "multiblock_patterns");
    }
}
