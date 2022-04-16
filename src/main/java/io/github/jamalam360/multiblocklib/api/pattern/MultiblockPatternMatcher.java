package io.github.jamalam360.multiblocklib.api.pattern;

import io.github.jamalam360.multiblocklib.impl.pattern.MultiblockPatternMatcherImpl;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Tests a given {@link BlockPos} in a {@link World} to see if it matches a {@link MultiblockPattern}.
 *
 * @see MultiblockPatternMatcherImpl
 * @author Jamalam360
 */
public interface MultiblockPatternMatcher {
    MultiblockPatternMatcher INSTANCE = new MultiblockPatternMatcherImpl();

    /**
     * @param bottomLeft The bottom left corner of the pattern, when facing in the positive X direction.
     * @param world The world to test the pattern in.
     * @param pattern The pattern to test against.
     * @param keys A map of character keys to block predicates to use to match {@link MultiblockPattern} rows to.
     * @return An {@link Optional} containing the {@link MatchResult} if the pattern matches,
     * or an empty {@link Optional} if it does not.
     */
    Optional<MatchResult> tryMatchPattern(BlockPos bottomLeft, World world, MultiblockPattern pattern, Map<Character, Predicate<CachedBlockPosition>> keys);
}
