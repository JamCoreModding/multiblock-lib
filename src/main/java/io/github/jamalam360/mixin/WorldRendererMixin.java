package io.github.jamalam360.mixin;

import io.github.jamalam360.Multiblock;
import io.github.jamalam360.MultiblockLib;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Optional;

/**
 * @author Jamalam360
 */

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Shadow
    private @Nullable ClientWorld world;

    @Redirect(
            method = "drawBlockOutline",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;getOutlineShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/ShapeContext;)Lnet/minecraft/util/shape/VoxelShape;"
            )
    )
    public VoxelShape multiblocklib$modifyOutlineForMultiblocks(BlockState instance, BlockView blockView, BlockPos blockPos, ShapeContext shapeContext) {
        Optional<Multiblock> multiblock = MultiblockLib.getMultiblock(world, blockPos);
        if (multiblock.isPresent()) {
            return multiblock.get().getOutlineShape(null); //TODO: pass ctx
        } else {
            return instance.getOutlineShape(this.world, blockPos, shapeContext);
        }
    }
}
