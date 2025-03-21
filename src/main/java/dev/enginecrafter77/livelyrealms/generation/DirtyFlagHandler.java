package dev.enginecrafter77.livelyrealms.generation;

public interface DirtyFlagHandler {
	public static final DirtyFlagHandler NOOP = () -> {};

	public void markDirty();
}
