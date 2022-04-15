package io.github.jamalam360.mixin;

import io.github.jamalam360.Multiblock;
import io.github.jamalam360.MultiblockLib;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Block.class)
public class BlockMixin {
    @Inject(
            method = "onBreak",
            at = @At("HEAD")
    )
    public void multiblocklib$checkForMultiblockOnBreak(World world, BlockPos pos, BlockState state, PlayerEntity player, CallbackInfo ci){
        Optional<Multiblock> multiblock = MultiblockLib.getMultiblock(world, pos);
        multiblock.ifPresent(value -> MultiblockLib.tryDisassembleMultiblock(value, true));
    }
}
