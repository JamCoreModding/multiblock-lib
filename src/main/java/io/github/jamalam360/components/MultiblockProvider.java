package io.github.jamalam360.components;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import io.github.jamalam360.Multiblock;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.ApiStatus;

import java.util.Optional;

/**
 * @author Jamalam360
 */
public interface MultiblockProvider extends ComponentV3 {
    Optional<Multiblock> getMultiblock(BlockPos pos);
    Multiblock[] getAllMultiblocks();
    @ApiStatus.Internal
    void addMultiblock(Multiblock multiblock);
    @ApiStatus.Internal
    void removeMultiblock(Multiblock multiblock);
}
