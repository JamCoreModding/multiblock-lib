package io.github.jamalam360.mixin;

import io.github.jamalam360.Multiblock;
import io.github.jamalam360.MultiblockLib;
import net.minecraft.block.BlockState;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.WorldRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

/**
 * @author Jamalam360
 */

@Mixin(WorldRenderer.class)
public abstract class WorldRendererMixin {
    @Shadow
    private @Nullable ClientWorld world;

    @Shadow
    private static void drawShapeOutline(MatrixStack matrices, VertexConsumer vertexConsumer, VoxelShape voxelShape, double d, double e, double f, float g, float h, float i, float j) {
    }

    @Inject(
            method = "drawBlockOutline",
            at = @At("HEAD"),
            cancellable = true
    )
    public void multiblocklib$modifyOutlineForMultiblocks(MatrixStack matrices, VertexConsumer vertexConsumer, Entity entity, double d, double e, double f, BlockPos blockPos, BlockState blockState, CallbackInfo ci) {
        Optional<Multiblock> multiblock = MultiblockLib.getMultiblock(world, blockPos);
        if (multiblock.isPresent()) {
            drawShapeOutline(
                    matrices,
                    vertexConsumer,
                    multiblock.get().getOutlineShape(null), //TODO: Pass context
                    (double) multiblock.get().getBottomLeftPos().getX() - d,
                    (double) multiblock.get().getBottomLeftPos().getY() - e,
                    (double) multiblock.get().getBottomLeftPos().getZ() - f,
                    0.0F,
                    0.0F,
                    0.0F,
                    0.4F
            );
            ci.cancel();
        }
    }
}
