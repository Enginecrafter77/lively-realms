package dev.enginecrafter77.livelyrealms;

import net.minecraft.nbt.CompoundTag;

public interface PersistentStateHolder {
	public void saveState(CompoundTag tag);
	public void restoreState(CompoundTag tag);
}
