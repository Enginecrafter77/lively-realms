package dev.enginecrafter77.livelyrealms.entity.cap;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;

public class WorkStepLocatorAttachment implements INBTSerializable<CompoundTag> {
	@Nullable
	private AssignedWorkStep.WorkStepLocator assignedTaskLocator;

	public WorkStepLocatorAttachment()
	{
		this.assignedTaskLocator = null;
	}

	@Nullable
	public AssignedWorkStep.WorkStepLocator getAssignedTaskLocator()
	{
		return this.assignedTaskLocator;
	}

	public void setAssignedTaskLocator(@Nullable AssignedWorkStep.WorkStepLocator assignedStep)
	{
		this.assignedTaskLocator = assignedStep;
	}

	@Override
	public CompoundTag serializeNBT(HolderLookup.Provider provider)
	{
		CompoundTag tag = new CompoundTag();
		if(this.assignedTaskLocator == null)
			return tag;
		tag.put("assigned_task", this.assignedTaskLocator.serializeNBT(provider));
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
	{
		if(!nbt.contains("assigned_task"))
		{
			this.assignedTaskLocator = null;
			return;
		}
		this.assignedTaskLocator = AssignedWorkStep.WorkStepLocator.deserializeNBT(provider, nbt.getCompound("assigned_task"));
	}
}
