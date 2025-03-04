package dev.enginecrafter77.livelyrealms.generation.plan;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class DefiniteStructureBuildPlan extends BuildPlan {
	private final List<BuildStep> steps;

	public DefiniteStructureBuildPlan(List<BuildStep> steps)
	{
		this.steps = steps;
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

	public static DefiniteStructureBuildPlan of(List<BuildStep> steps)
	{
		return new DefiniteStructureBuildPlan(ImmutableList.copyOf(steps));
	}

	public static DefiniteStructureBuildPlan of(BuildStep... steps)
	{
		return DefiniteStructureBuildPlan.of(ImmutableList.copyOf(steps));
	}
}
