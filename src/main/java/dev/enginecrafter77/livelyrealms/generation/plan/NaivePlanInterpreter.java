package dev.enginecrafter77.livelyrealms.generation.plan;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class NaivePlanInterpreter implements PlanInterpreter {
	private final BuildPlan plan;
	private int nextStep;

	public NaivePlanInterpreter(BuildPlan plan)
	{
		this.plan = plan;
		this.nextStep = 0;
	}

	@Override
	public boolean hasNextStep()
	{
		return this.nextStep < this.plan.getStepCount();
	}

	@Override
	public BuildStep nextStep()
	{
		return this.plan.getStep(this.nextStep++);
	}

	@Nullable
	@Override
	public BuildStep lastStep()
	{
		if(this.nextStep == 0)
			return null;
		return this.plan.getStep(this.nextStep - 1);
	}

	@Override
	public boolean isDone()
	{
		return this.nextStep == this.plan.getStepCount();
	}

	@Override
	public void saveState(CompoundTag tag)
	{
		tag.putInt("step", this.nextStep);
	}

	@Override
	public void restoreState(CompoundTag tag)
	{
		this.nextStep = tag.getInt("step");
	}
}
