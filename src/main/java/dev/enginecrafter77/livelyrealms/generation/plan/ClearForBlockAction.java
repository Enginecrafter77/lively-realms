package dev.enginecrafter77.livelyrealms.generation.plan;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class ClearForBlockAction extends PlaceBlockAction {
	private final BlockState finalBlock;

	public ClearForBlockAction(BlockPos pos, BlockState finalBlock)
	{
		super(pos, Blocks.AIR.defaultBlockState());
		this.finalBlock = finalBlock;
	}

	@Override
	public void perform(BuildContext context)
	{
		context.setBlockAndUpdate(this.relativePosition, Blocks.AIR.defaultBlockState());
	}

	@Override
	public boolean isComplete(BuildContext context)
	{
		BlockState existing = context.getBlockState(this.relativePosition);
		return existing.isAir() || Objects.equals(existing, this.finalBlock);
	}
}
