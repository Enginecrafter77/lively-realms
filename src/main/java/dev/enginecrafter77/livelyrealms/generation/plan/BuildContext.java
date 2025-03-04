package dev.enginecrafter77.livelyrealms.generation.plan;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public record BuildContext(Level level, BlockPos anchor) {
	public BlockState getBlockState(Vec3i relativePosition)
	{
		return this.level.getBlockState(this.getWorldPosition(relativePosition));
	}

	public void setBlockAndUpdate(Vec3i relativePosition, BlockState state)
	{
		this.level.setBlockAndUpdate(this.getWorldPosition(relativePosition), state);
	}

	public BlockPos getWorldPosition(Vec3i relativePosition)
	{
		return this.anchor.offset(relativePosition);
	}

	public BuildContext shifted(Vec3i offset)
	{
		return new BuildContext(this.level, this.anchor.offset(offset));
	}
}
