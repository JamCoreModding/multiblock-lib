package io.github.jamalam360.multiblocklib.api.pattern;

import io.github.jamalam360.multiblocklib.api.MultiblockLib;
import io.github.jamalam360.multiblocklib.impl.pattern.MultiblockPatternsImpl;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * Keeps a record of al the {@link MultiblockPattern}s that have been added via JSON.
 *
 * @see MultiblockPatternsImpl
 * @author Jamalam360
 */
public interface MultiblockPatterns {
    MultiblockPatterns INSTANCE = new MultiblockPatternsImpl();

    /**
     * @return All patterns that have been discovered and registered (all discovered JSON files).
     * @implNote Patterns returned by this method are not guaranteed to have been registered with
     * {@link MultiblockLib#registerMultiblock}
     */
    MultiblockPattern[] getPatterns();


    /**
     * @param namespace The namespace of the mod to get the patterns from.
     * @return All the patterns that have been discovered in the given namespace
     * (usually a mod ID).
     */
    MultiblockPattern[] getPatterns(String namespace);

    Optional<MultiblockPattern> getPattern(Identifier id);

    /**
     * Users of MultiblockLib should not call this method directly - let the resource reload listener
     * handle it for you.
     *
     * @param pattern The pattern to register.
     */
    @ApiStatus.Internal
    void add(MultiblockPattern pattern);

    /**
     * Users of MultiblockLib should not call this method directly - let the resource reload listener
     * handle it for you.
     */
    @ApiStatus.Internal
    void clear();
}
