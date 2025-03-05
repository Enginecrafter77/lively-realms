package dev.enginecrafter77.livelyrealms.generation.plan;

import com.google.common.collect.ImmutableList;

import java.util.LinkedList;
import java.util.List;

public class BuildPlanBuilder {
	private final List<BuildPlan> stages;

	public BuildPlanBuilder()
	{
		this.stages = new LinkedList<BuildPlan>();
	}

	public BuildPlanBuilder stage(BuildPlan stage)
	{
		this.stages.add(stage);
		return this;
	}

	public BuildStageBuilder beginStage()
	{
		return new BuildStageBuilder();
	}

	public BuildPlan build()
	{
		if(this.stages.size() == 1)
			return this.stages.getFirst();
		return new StagedBuildPlan(this.stages);
	}

	public static BuildPlanBuilder begin()
	{
		return new BuildPlanBuilder();
	}

	public class BuildStageBuilder
	{
		private final ImmutableList.Builder<BuildStepAction> steps;

		public BuildStageBuilder()
		{
			this.steps = ImmutableList.builder();
		}

		public BuildStageBuilder step(BuildStepAction step)
		{
			this.steps.add(step);
			return this;
		}

		public BuildPlanBuilder endStage()
		{
			return BuildPlanBuilder.this.stage(DefiniteStructureBuildPlan.of(this.steps.build()));
		}
	}
}
