package dev.enginecrafter77.livelyrealms.generation.plan;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PlaceBlockStep implements StructureBuildStep {
	public final BlockPos relativePosition;
	public final BlockState blockState;

	public PlaceBlockStep(BlockPos relativePosition, BlockState blockState)
	{
		this.relativePosition = relativePosition;
		this.blockState = blockState;
	}

	@Override
	public void perform(StructureBuildContext context)
	{
		BlockPos worldPos = context.anchor().offset(this.relativePosition);
		context.level().setBlockAndUpdate(worldPos, this.blockState);
	}
}
