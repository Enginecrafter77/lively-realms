package dev.enginecrafter77.livelyrealms.entity.cap;

import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class WorkStepHolder implements WorkHandler {
	private final WorkStepLocatorHolder locatorHolder;
	private final ServerLevel level;

	@Nullable
	private AssignedWorkStep cachedTaskRef;

	public WorkStepHolder(WorkStepLocatorHolder locatorHolder, ServerLevel level)
	{
		this.locatorHolder = locatorHolder;
		this.level = level;
		this.cachedTaskRef = null;
	}

	@Nullable
	private AssignedWorkStep resolveStoredTask()
	{
		AssignedWorkStep.WorkStepLocator locator = this.locatorHolder.getAssignedTaskLocator();
		if(locator == null)
			return null;
		return locator.resolve(this.level);
	}

	private void commitCache()
	{
		this.locatorHolder.setAssignedTaskLocator(Optional.ofNullable(this.cachedTaskRef).map(AssignedWorkStep::locator).orElse(null));
	}

	public void invalidateCache()
	{
		this.cachedTaskRef = null;
	}

	@Nullable
	@Override
	public AssignedWorkStep getAssignedStep()
	{
		if(this.cachedTaskRef == null)
			this.cachedTaskRef = this.resolveStoredTask();
		return this.cachedTaskRef;
	}

	@Override
	public void setAssignedStep(@Nullable AssignedWorkStep task)
	{
		this.cachedTaskRef = task;
		this.commitCache();
	}
}
