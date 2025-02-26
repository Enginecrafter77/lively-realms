package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.plan.StructureBuildContext;
import dev.enginecrafter77.livelyrealms.generation.plan.StructureBuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.StructureBuildStep;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

public class StructureBuildTask {
	public final StructureBuildContext context;
	public final StructureBuildPlan plan;
	private int nextStep;

	@Nullable
	private StructureBuildStep lastStep;

	public StructureBuildTask(StructureBuildContext context, StructureBuildPlan plan)
	{
		this.context = context;
		this.plan = plan;
		this.nextStep = 0;
		this.lastStep = null;
	}

	public boolean hasNextStep()
	{
		return this.nextStep < this.plan.getStepCount();
	}

	@Nullable
	public StructureBuildStep getLastStep()
	{
		return this.lastStep;
	}

	public StructureBuildStep step()
	{
		this.lastStep = this.plan.getStep(this.nextStep++);
		this.lastStep.perform(this.context);
		return this.lastStep;
	}

	public int getStep()
	{
		return this.nextStep;
	}

	public float getProgress()
	{
		return (float)this.nextStep / (float)this.plan.getStepCount();
	}

	public void setStep(int step)
	{
		if(step == this.nextStep)
			return;
		if(step < this.nextStep)
			throw new IllegalArgumentException("Cannot regress build task");
		this.plan.partial(this.nextStep, step).execute(this.context);
		this.nextStep = step;
	}

	public void saveState(CompoundTag tag)
	{
		tag.putInt("step", this.nextStep);
	}

	public void restoreState(CompoundTag tag)
	{
		this.nextStep = tag.getInt("step");
	}
}
