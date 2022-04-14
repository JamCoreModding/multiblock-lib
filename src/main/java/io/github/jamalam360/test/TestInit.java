/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jamalam360
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package io.github.jamalam360.test;

import io.github.jamalam360.MultiblockLib;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

/**
 * @author Jamalam360
 */
public class TestInit implements ModInitializer {
    @Override
    public void onInitialize() {
        MultiblockLib.registerMultiblock(TestMultiblock::new, BlockPatternBuilder.start()
                .aisle("III", "ICI", "III")
                .aisle("ICI", "CCC", "ICI")
                .aisle("III", "ICI", "III")
                .where('I', CachedBlockPosition.matchesBlockState(state -> state.getBlock() == Blocks.GLASS))
                .where('C', CachedBlockPosition.matchesBlockState(state -> state.getBlock() == Blocks.COPPER_BLOCK))
                .build()
        );

        Registry.register(Registry.ITEM, new Identifier("multiblocklib", "test_assembler"), new TestAssemblerItem());
    }

     static class TestAssemblerItem extends Item {
        public TestAssemblerItem() {
            super(new FabricItemSettings().group(ItemGroup.TOOLS));
        }

        @Override
        public ActionResult useOnBlock(ItemUsageContext context) {
            Block block = context.getWorld().getBlockState(context.getBlockPos()).getBlock();
            if (block == Blocks.IRON_BLOCK || block == Blocks.COPPER_BLOCK) {
                if (MultiblockLib.tryAssembleMultiblock(context.getWorld(), context.getBlockPos())) {
                    return ActionResult.SUCCESS;
                }
            }

            return ActionResult.PASS;
        }
    }
}
