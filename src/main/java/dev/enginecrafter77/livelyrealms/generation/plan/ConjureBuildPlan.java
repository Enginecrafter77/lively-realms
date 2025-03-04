package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.Structure;

public class ConjureBuildPlan extends BuildPlan {
	private final Structure structure;

	public ConjureBuildPlan(Structure structure)
	{
		this.structure = structure;
	}

	@Override
	public BuildStep getStep(int stepIndex)
	{
		if(stepIndex != 0)
			throw new IndexOutOfBoundsException(stepIndex);
		return new ConjureStep(this.structure);
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
