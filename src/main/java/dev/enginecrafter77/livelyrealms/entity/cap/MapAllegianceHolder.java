package dev.enginecrafter77.livelyrealms.entity.cap;

import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface MapAllegianceHolder {
	@Nullable
	public UUID getAllegiance();
	public void setAllegiance(@Nullable UUID allegiance);
}
