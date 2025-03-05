package dev.enginecrafter77.livelyrealms.generation.plan;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class DefiniteStructureBuildPlan extends BuildPlan {
	private final List<BuildStep> steps;

	public DefiniteStructureBuildPlan(List<BuildStepAction> steps)
	{
		this.steps = BuildStepIndexer.index(steps, this);
	}

	@Override
	public BuildStep getStep(int stepIndex)
	{
		return this.steps.get(stepIndex);
	}

	@Override
	public int getStepCount()
	{
		return this.steps.size();
	}

	public static DefiniteStructureBuildPlan of(List<BuildStepAction> steps)
	{
		return new DefiniteStructureBuildPlan(ImmutableList.copyOf(steps));
	}

	public static DefiniteStructureBuildPlan of(BuildStepAction... steps)
	{
		return DefiniteStructureBuildPlan.of(ImmutableList.copyOf(steps));
	}
}
