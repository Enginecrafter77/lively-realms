package dev.enginecrafter77.livelyrealms.generation.plan;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;

import java.util.Objects;

public class PlaceBlockAction extends BuildStepAction {
	public final BlockPos relativePosition;
	public final BlockState blockState;

	public PlaceBlockAction(BlockPos relativePosition, BlockState blockState)
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

	@Override
	public void getHotspot(BuildContext context, Vector3d dest)
	{
		dest.x = context.anchor().getX() + this.relativePosition.getX() + 0.5D;
		dest.y = context.anchor().getY() + this.relativePosition.getY() + 0.5D;
		dest.z = context.anchor().getZ() + this.relativePosition.getZ() + 0.5D;
	}

	@Override
	public boolean hasHotspot()
	{
		return true;
	}

	public static PlaceBlockAction clear(BlockPos pos)
	{
		return new PlaceBlockAction(pos, Blocks.AIR.defaultBlockState());
	}
}
