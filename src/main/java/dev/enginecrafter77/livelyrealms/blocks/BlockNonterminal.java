package dev.enginecrafter77.livelyrealms.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class BlockNonterminal extends Block implements EntityBlock {
	public BlockNonterminal(Properties properties)
	{
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state)
	{
		return new BlockEntityNonterminal(pos, state);
	}
}
