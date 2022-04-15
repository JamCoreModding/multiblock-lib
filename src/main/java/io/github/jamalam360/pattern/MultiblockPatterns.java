package io.github.jamalam360.pattern;

import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author Jamalam360
 */
public class MultiblockPatterns {
    protected static final List<MultiblockPattern> PATTERNS = new ArrayList<>();

    public static List<MultiblockPattern> get() {
        return PATTERNS;
    }

    public static Optional<MultiblockPattern> get(Identifier identifier) {
        for (MultiblockPattern pattern : PATTERNS) {
            if (pattern.identifier().equals(identifier)) {
                return Optional.of(pattern);
            }
        }

        return Optional.empty();
    }

    protected static void add(MultiblockPattern pattern) {
        PATTERNS.add(pattern);
    }

    protected static void clear() {
        PATTERNS.clear();
    }
}
