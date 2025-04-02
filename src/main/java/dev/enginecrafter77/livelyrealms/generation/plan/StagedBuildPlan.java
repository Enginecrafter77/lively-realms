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
	public BuildStepAction getStepAction(int stepIndex)
	{
		int stageStepIndex = stepIndex;
		for(BuildPlan stage : this.stages)
		{
			if(stage.getStepCount() > stageStepIndex)
			{
				BuildStep stageStep = stage.getStep(stageStepIndex);
				return stageStep.action();
			}
			stageStepIndex -= stage.getStepCount();
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
