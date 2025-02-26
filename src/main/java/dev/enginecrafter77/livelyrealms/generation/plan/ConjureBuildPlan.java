package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.Structure;

public class ConjureBuildPlan extends StructureBuildPlan {
	private final Structure structure;

	public ConjureBuildPlan(Structure structure)
	{
		this.structure = structure;
	}

	@Override
	public StructureBuildStep getStep(int stepIndex)
	{
		return new ConjureStructureStep(this.structure);
	}

	@Override
	public int getStepCount()
	{
		return 1;
	}

	public static ConjureBuildPlan of(Structure structure)
	{
		return new ConjureBuildPlan(structure);
	}
}
