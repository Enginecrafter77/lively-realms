package dev.enginecrafter77.livelyrealms.generation.plan;

public abstract class BuildPlan {
	public abstract BuildStepAction getStepAction(int stepIndex);
	public abstract int getStepCount();

	public BuildPlan partial(float from, float to)
	{
		if(from < 0F || from > 1F || to < 0F || to > 1F)
			throw new IndexOutOfBoundsException();
		int stepFrom = (int)Math.ceil(this.getStepCount() * from);
		int stepTo = (int)Math.floor((this.getStepCount() - 1) * to);
		return this.partial(stepFrom, stepTo);
	}

	public BuildPlan partial(int from, int to)
	{
		if(from < 0 || from >= this.getStepCount() || to < 0 || to >= this.getStepCount())
			throw new IndexOutOfBoundsException();
		return new PartialBuildPlan(this, from, to);
	}

	public BuildStep getStep(int stepIndex)
	{
		return new BuildStep(this, stepIndex, this.getStepAction(stepIndex));
	}

	private static class PartialBuildPlan extends BuildPlan
	{
		private final BuildPlan fullPlan;
		private final int from;
		private final int to;

		public PartialBuildPlan(BuildPlan fullPlan, int from, int to)
		{
			this.fullPlan = fullPlan;
			this.from = from;
			this.to = to;
		}

		@Override
		public BuildStepAction getStepAction(int stepIndex)
		{
			return this.fullPlan.getStepAction(this.from + stepIndex);
		}

		@Override
		public int getStepCount()
		{
			return this.to - this.from;
		}
	}
}
