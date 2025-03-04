package dev.enginecrafter77.livelyrealms.generation.plan;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Objects;

public class PlaceBlockStep implements BuildStep {
	public final BlockPos relativePosition;
	public final BlockState blockState;

	public PlaceBlockStep(BlockPos relativePosition, BlockState blockState)
	{
		this.relativePosition = relativePosition;
		this.blockState = blockState;
	}

	@Override
	public void perform(BuildContext context)
	{
		context.setBlockAndUpdate(this.relativePosition, this.blockState);
	}

	@Override
	public boolean isComplete(BuildContext context)
	{
		return Objects.equals(this.blockState, context.getBlockState(this.relativePosition));
	}

	public static PlaceBlockStep clear(BlockPos pos)
	{
		return new PlaceBlockStep(pos, Blocks.AIR.defaultBlockState());
	}
}
