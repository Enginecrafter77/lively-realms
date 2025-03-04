package dev.enginecrafter77.livelyrealms.generation.plan;

import org.jetbrains.annotations.Nullable;

public interface BuildContextOwner {
	@Nullable
	public BuildContext getContext();
}
