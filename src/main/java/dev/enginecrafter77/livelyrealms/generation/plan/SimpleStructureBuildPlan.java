package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.function.Predicate;

public class SimpleStructureBuildPlan extends FilteredStructureBuildPlan {
	public SimpleStructureBuildPlan(Structure structure, Predicate<BlockState> predicate)
	{
		super(structure, predicate);
	}

	public SimpleStructureBuildPlan(Structure structure)
	{
		super(structure);
	}

	@Override
	protected BuildStepAction getActionFor(Vector3ic position)
	{
		BlockPos pos = new BlockPos(position.x(), position.y(), position.z());
		return new PlaceBlockAction(pos, this.structure.getBlockAt(position).getBlockState());
	}
}
