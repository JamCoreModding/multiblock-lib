package io.github.jamalam360.pattern;

import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

import java.util.Map;
import java.util.function.Predicate;

/**
 * @author Jamalam360
 */
@SuppressWarnings("unchecked")
public class PatternTester {
    public static MatchResult tryMatchPattern(BlockPos bottomLeft, World world, MultiblockPattern pattern, Map<Character, Predicate<CachedBlockPosition>> keys) {
        return tryMatchPattern(bottomLeft, world, pattern, keys, 0);
    }

    private static MatchResult tryMatchPattern(BlockPos bottomLeft, World world, MultiblockPattern pattern, Map<Character, Predicate<CachedBlockPosition>> keys, int rotateCount) {
        //TODO: Support clicking on not bottom left of structure

        boolean checkedAllLayers = false;
        int layerNumber = 0;
        int loopCount = 0;
        BlockPos finalPos = bottomLeft.mutableCopy().toImmutable();
        while (!checkedAllLayers) {
            if (layerNumber >= pattern.layers().length) {
                checkedAllLayers = true;
                continue;
            }

            System.out.println("Checking layer " + layerNumber);

            MultiblockPattern.Layer layer = pattern.layers()[layerNumber];
            Predicate<CachedBlockPosition>[][] blocks = constructPredicateListFromLayer(layer, keys);

            switch (rotateCount) {
                case 0:
                    break;
                case 1:
                    blocks = rotateClockwise(blocks);
                    break;
                case 2:
                    blocks = rotateClockwise(rotateClockwise(blocks));
                    break;
                case 3:
                    blocks = rotateClockwise(rotateClockwise(rotateClockwise(blocks)));
                    break;
            }

            boolean layerIsRepeatable = layer.min() != 1 && layer.max() != 1;

            BlockPos.Mutable mutable = new BlockPos.Mutable();
            mutable.setX(bottomLeft.getX());
            mutable.setY(bottomLeft.getY() + loopCount);
            mutable.setZ(bottomLeft.getZ());

            System.out.println("Bottom left of layer: " + mutable);

            if (layerIsRepeatable) {
                int matches = matchesRepeatableLayer(blocks, world, mutable);
                if (matches == -1 || matches < layer.min() || matches > layer.max()) {
                    System.out.println("Repeatable layer failed to match (" + matches + " matches)");

                    if (rotateCount < 4) {
                        return tryMatchPattern(bottomLeft, world, pattern, keys, rotateCount + 1);
                    }

                    return new MatchResult(false, pattern, 0, 0, 0, null);
                } else {
                    System.out.println("Repeatable layer matched (" + matches + " matches)");
                    loopCount += matches;
                    layerNumber++;
                }
            } else {
                if (!matchesLayer(blocks, world, mutable)) {
                    System.out.println("Non repeatable layer failed to match");

                    if (rotateCount < 4) {
                        return tryMatchPattern(bottomLeft, world, pattern, keys, rotateCount + 1);
                    }

                    return new MatchResult(false, pattern, 0, 0, 0, null);
                } else {
                    System.out.println("Non repeatable layer matched");
                    loopCount++;
                    layerNumber++;
                }
            }

            finalPos = mutable.toImmutable();
        }

        return new MatchResult(true, pattern, loopCount, pattern.width(), pattern.depth(), BlockBox.create(bottomLeft, finalPos));
    }

    private static int matchesRepeatableLayer(Predicate<CachedBlockPosition>[][] blocks, World world, BlockPos.Mutable mutable) {
        int matches = 0;
        BlockPos base = mutable.toImmutable();

        while (true) {
            if (matchesLayer(blocks, world, mutable)) {
                matches++;
            } else {
                if (matches == 0) {
                    return -1;
                } else {
                    return matches;
                }
            }
            mutable.move(Direction.UP);
            mutable.setX(base.getX());
            mutable.setZ(base.getZ());
        }
    }

    private static boolean matchesLayer(Predicate<CachedBlockPosition>[][] blocks, World world, BlockPos.Mutable pos) {
        BlockPos bottomLeft = pos.toImmutable();
        for (int rowIndex = 0; rowIndex < blocks.length; rowIndex++) {
            Predicate<CachedBlockPosition>[] row = blocks[rowIndex];
            System.out.println("Checking row " + rowIndex + " with length " + blocks[rowIndex].length);
            pos.setZ(bottomLeft.getZ() + rowIndex);

            for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                System.out.println("Checking column " + columnIndex + " with length " + row.length);
                Predicate<CachedBlockPosition> block = row[columnIndex];
                pos.setX(bottomLeft.getX() + columnIndex);
                if (!block.test(new CachedBlockPosition(world, pos, true))) {
                    System.out.println("Failed at " + pos + " (Found: " + world.getBlockState(pos).getBlock().getTranslationKey() + ")");
                    return false;
                } else {
                    System.out.println("Passed at " + pos + " (Found: " + world.getBlockState(pos).getBlock().getTranslationKey() + ")");
                }
            }
        }

        System.out.println("All blocks of layer matched");
        return true;
    }

    private static Predicate<CachedBlockPosition>[][] constructPredicateListFromLayer(MultiblockPattern.Layer layer, Map<Character, Predicate<CachedBlockPosition>> key) {
        Predicate<CachedBlockPosition>[][] blocks = (Predicate<CachedBlockPosition>[][]) new Predicate[layer.rows()[0].length()][layer.rows().length];

        for (int i = 0; i < layer.rows().length; i++) {
            for (int j = 0; j < layer.rows()[i].length(); j++) {
                char c = layer.rows()[i].charAt(j);
                if (key.containsKey(c)) {
                    blocks[j][i] = key.get(c);
                } else {
                    throw new IllegalArgumentException("Invalid character: " + c);
                }
            }
        }

        return blocks;
    }

    /**
     * Stack overflow lol
     */
    private static Predicate<CachedBlockPosition>[][] rotateClockwise(Predicate<CachedBlockPosition>[][] matrix) {
        int size = matrix.length;
        Predicate<CachedBlockPosition>[][] ret = new Predicate[size][size];

        for (int i = 0; i < size; ++i) {
            for (int j = 0; j < size; ++j) {
                ret[i][j] = matrix[size - j - 1][i]; //***

            }
        }

        return ret;
    }
}
