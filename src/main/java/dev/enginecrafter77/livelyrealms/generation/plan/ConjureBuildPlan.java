package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.Structure;

public class ConjureBuildPlan extends BuildPlan {
	private final BuildStep conjureStep;

	public ConjureBuildPlan(Structure structure)
	{
		this.conjureStep = new BuildStep(this, 0, new ConjureStructureAction(structure));
	}

	@Override
	public BuildStep getStep(int stepIndex)
	{
		if(stepIndex != 0)
			throw new IndexOutOfBoundsException(stepIndex);
		return this.conjureStep;
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
