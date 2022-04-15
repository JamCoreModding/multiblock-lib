package io.github.jamalam360.pattern;

import com.google.common.collect.Maps;
import net.minecraft.block.pattern.CachedBlockPosition;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Predicate;

/**
 * Wraps around a {@link HashMap} to provide a nice API with `where` rather than `put`
 * @author Jamalam360
 */
public class MultiblockPatternKeyBuilder {
    private final Map<Character, Predicate<CachedBlockPosition>> keys = Maps.newHashMap();

    private MultiblockPatternKeyBuilder(){}

    public static MultiblockPatternKeyBuilder start(){
        return new MultiblockPatternKeyBuilder();
    }

    public MultiblockPatternKeyBuilder where(char key, Predicate<CachedBlockPosition> predicate) {
        keys.put(key, predicate);
        return this;
    }

    public Map<Character, Predicate<CachedBlockPosition>> build(){
        return keys;
    }
}
