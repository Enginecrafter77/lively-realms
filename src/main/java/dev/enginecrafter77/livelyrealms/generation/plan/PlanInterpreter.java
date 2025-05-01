package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.PersistentStateHolder;
import org.jetbrains.annotations.Nullable;

public interface PlanInterpreter extends PersistentStateHolder {
	public boolean hasNextStep();
	public BuildStep nextStep();
	public boolean isDone();

	@Nullable
	public BuildStep lastStep();
}
