package dev.enginecrafter77.livelyrealms.generation.plan;

import java.util.List;
import java.util.function.Function;

public class BuildStepIndexer implements Function<BuildStepAction, BuildStep> {
	private final BuildPlan plan;
	private int lastIndex;

	public BuildStepIndexer(BuildPlan plan)
	{
		this.plan = plan;
		this.lastIndex = -1;
	}

	@Override
	public BuildStep apply(BuildStepAction action)
	{
		return new BuildStep(this.plan, ++this.lastIndex, action);
	}

	public static List<BuildStep> index(List<BuildStepAction> steps, BuildPlan plan)
	{
		return steps.stream().map(BuildStepIndexer.in(plan)).toList();
	}

	public static BuildStepIndexer in(BuildPlan plan)
	{
		return new BuildStepIndexer(plan);
	}
}
