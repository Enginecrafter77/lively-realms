package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.function.Predicate;

public class SimpleStructureBuildPlan extends StructureBuildPlan {
	public SimpleStructureBuildPlan(Structure structure)
	{
		super(structure);
	}

	@Override
	public BuildStepAction getActionFor(Vector3ic position)
	{
		return new PlaceBlockAction(toBlockPos(position), this.structure.getBlockAt(position));
	}
}
