package dev.enginecrafter77.livelyrealms.generation.plan;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class DefiniteStructureBuildPlan extends StructureBuildPlan {
	private final List<StructureBuildStep> steps;

	public DefiniteStructureBuildPlan(List<StructureBuildStep> steps)
	{
		this.steps = steps;
	}

	@Override
	public StructureBuildStep getStep(int stepIndex)
	{
		return this.steps.get(stepIndex);
	}

	@Override
	public int getStepCount()
	{
		return this.steps.size();
	}

	public static BuildPlanBuilder builder()
	{
		return new BuildPlanBuilder();
	}

	public static class BuildPlanBuilder
	{
		private final ImmutableList.Builder<StructureBuildStep> stepBuilder;

		public BuildPlanBuilder()
		{
			this.stepBuilder = ImmutableList.builder();
		}

		public BuildPlanBuilder then(StructureBuildStep step)
		{
			this.stepBuilder.add(step);
			return this;
		}

		public DefiniteStructureBuildPlan build()
		{
			return new DefiniteStructureBuildPlan(this.stepBuilder.build());
		}
	}
}
