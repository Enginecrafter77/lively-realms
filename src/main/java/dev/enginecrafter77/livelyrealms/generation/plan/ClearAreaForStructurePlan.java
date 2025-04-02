package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3ic;

import java.util.function.Predicate;

public class ClearAreaForStructurePlan extends FilteredStructureBuildPlan {
	public ClearAreaForStructurePlan(Structure structure)
	{
		super(structure);
	}

	public ClearAreaForStructurePlan(Structure structure, Predicate<BlockState> shouldClear)
	{
		super(structure, shouldClear);
	}

	@Override
	protected BuildStepAction getActionFor(Vector3ic pos)
	{
		BlockPos destPos = new BlockPos(pos.x(), pos.y(), pos.z());
		return new ClearForBlockAction(destPos, this.structure.getBlockAt(pos).getBlockState());
	}
}
