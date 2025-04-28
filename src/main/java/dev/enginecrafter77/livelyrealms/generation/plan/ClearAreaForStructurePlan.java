package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3ic;

import java.util.function.Predicate;

public class ClearAreaForStructurePlan extends StructureBuildPlan {
	public ClearAreaForStructurePlan(Structure structure)
	{
		super(structure);
	}

	@Override
	public BuildStepAction getActionFor(Vector3ic pos)
	{
		return new ClearForBlockAction(toBlockPos(pos), this.structure.getBlockAt(pos));
	}
}
