package me.duncanruns.reversecrafingtable.mixin;

import me.duncanruns.reversecrafingtable.ReverseCraftingTable;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CraftingTableBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(CraftingTableBlock.class)
public abstract class CraftingTableBlockMixin extends Block {
    public CraftingTableBlockMixin(Settings settings) {
        super(settings);
    }

    @Override
    public void onBlockBreakStart(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        if (player.isSneaking()) {
            world.setBlockState(pos, ReverseCraftingTable.BLOCK.getDefaultState());
        }
    }
}
