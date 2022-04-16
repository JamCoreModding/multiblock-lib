package io.github.jamalam360.multiblocklib.testmod;

import io.github.jamalam360.multiblocklib.api.Multiblock;
import io.github.jamalam360.multiblocklib.api.pattern.MatchResult;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * @author Jamalam360
 */
public class BigChestMultiblock extends Multiblock implements NamedScreenHandlerFactory {
    private final SimpleInventory inventory = new SimpleInventory(54);

    public BigChestMultiblock(BlockPos pos, World world, MatchResult match) {
        super(pos, world, match);
    }

    @Override
    public ActionResult onUse(World world, BlockPos clickPos, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        if (world.isClient) {
            return ActionResult.SUCCESS;
        } else {
            player.openHandledScreen(this);
            return ActionResult.CONSUME;
        }
    }

    @Override
    public NbtCompound writeTag() {
        NbtCompound compound = super.writeTag();
        compound.put("Inventory", inventory.toNbtList());
        return compound;
    }

    @Override
    public void readTag(NbtCompound tag) {
        super.readTag(tag);
        inventory.readNbtList(tag.getList("Inventory", 10));
    }

    @Override
    public Text getDisplayName() {
        return new LiteralText("The Big Chest :yeef:");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int i, PlayerInventory playerInventory, PlayerEntity playerEntity) {
        return GenericContainerScreenHandler.createGeneric9x6(i, playerInventory, inventory);
    }
}
