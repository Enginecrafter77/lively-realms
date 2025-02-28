package dev.enginecrafter77.livelyrealms.generation.plan;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class StagedBuildPlan extends StructureBuildPlan {
	private final List<StructureBuildPlan> stages;
	private final int stepCount;

	public StagedBuildPlan(List<StructureBuildPlan> stages)
	{
		this.stages = stages;
		this.stepCount = stages.stream().mapToInt(StructureBuildPlan::getStepCount).sum();
	}

	@Override
	public StructureBuildStep getStep(int stepIndex)
	{
		for(StructureBuildPlan stage : this.stages)
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

	public static StagedBuildPlan of(StructureBuildPlan... stages)
	{
		return new StagedBuildPlan(ImmutableList.copyOf(stages));
	}
}
