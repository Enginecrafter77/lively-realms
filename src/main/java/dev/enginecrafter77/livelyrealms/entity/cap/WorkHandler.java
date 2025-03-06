package dev.enginecrafter77.livelyrealms.entity.cap;

import org.jetbrains.annotations.Nullable;

public interface WorkHandler {
	@Nullable
	public AssignedWorkStep getAssignedStep();
	public void setAssignedStep(@Nullable AssignedWorkStep task);
}
