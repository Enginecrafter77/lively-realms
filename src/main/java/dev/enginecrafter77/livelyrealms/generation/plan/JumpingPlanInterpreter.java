package dev.enginecrafter77.livelyrealms.generation.plan;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;

public abstract class JumpingPlanInterpreter implements PlanInterpreter {
	private final BuildPlan plan;

	@Nullable
	private BuildStep lastStepRef;

	private int lastStep;
	private int nextStep;

	public JumpingPlanInterpreter(BuildPlan plan)
	{
		this.plan = plan;
		this.lastStepRef = null;
		this.lastStep = -1;
		this.nextStep = -1;
	}

	protected abstract boolean isStepAcceptable(BuildStep step);

	private int findNextStep()
	{
		for(int nextStep = this.lastStep + 1; nextStep < this.plan.getStepCount(); ++nextStep)
		{
			if(this.isStepAcceptable(this.plan.getStep(nextStep)))
				return nextStep;
		}
		return this.plan.getStepCount(); // we're done
	}

	private void invalidateNextStep()
	{
		this.nextStep = -1;
	}

	private void rotateStepIndices()
	{
		this.lastStep = this.nextStep;
		this.nextStep = -1;
	}

	private int getNextStepIndex()
	{
		if(this.nextStep == -1)
			this.nextStep = this.findNextStep();
		return this.nextStep;
	}

	@Override
	public boolean hasNextStep()
	{
		return this.getNextStepIndex() < this.plan.getStepCount();
	}

	@Override
	public boolean isDone()
	{
		return this.getNextStepIndex() == this.plan.getStepCount();
	}

	@Override
	public BuildStep nextStep()
	{
		if(!this.hasNextStep())
			throw new NoSuchElementException();
		this.lastStepRef = this.plan.getStep(this.getNextStepIndex());
		this.rotateStepIndices();
		return this.lastStepRef;
	}

	@Nullable
	@Override
	public BuildStep lastStep()
	{
		if(this.lastStep == -1)
			return null;
		if(this.lastStepRef == null)
			this.lastStepRef = this.plan.getStep(this.lastStep);
		return this.lastStepRef;
	}

	@Override
	public void saveState(CompoundTag tag)
	{
		tag.putInt("last_step", this.lastStep);
	}

	@Override
	public void restoreState(CompoundTag tag)
	{
		this.lastStep = tag.getInt("last_step");
		this.invalidateNextStep();
	}
}
