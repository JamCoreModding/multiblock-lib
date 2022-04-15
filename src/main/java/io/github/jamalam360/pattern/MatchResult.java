package io.github.jamalam360.pattern;

import net.minecraft.util.math.BlockBox;

/**
 * @author Jamalam360
 */
public record MatchResult(boolean matched, MultiblockPattern pattern, int height, int width, int depth, BlockBox box) {
}