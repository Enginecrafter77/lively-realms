package dev.enginecrafter77.livelyrealms.generation.plan;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class StagedBuildPlan extends BuildPlan {
	private final List<BuildPlan> stages;
	private final int stepCount;

	public StagedBuildPlan(List<BuildPlan> stages)
	{
		this.stages = stages;
		this.stepCount = stages.stream().mapToInt(BuildPlan::getStepCount).sum();
	}

	@Override
	public BuildStep getStep(int stepIndex)
	{
		for(BuildPlan stage : this.stages)
		{
			if(stage.getStepCount() > stepIndex)
				return stage.getStep(stepIndex);
			stepIndex -= stage.getStepCount();
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int getStepCount()
	{
		return this.stepCount;
	}

	public static StagedBuildPlan of(BuildPlan... stages)
	{
		return new StagedBuildPlan(ImmutableList.copyOf(stages));
	}
}
