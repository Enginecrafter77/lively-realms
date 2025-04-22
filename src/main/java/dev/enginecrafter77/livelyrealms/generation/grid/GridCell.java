package dev.enginecrafter77.livelyrealms.generation.grid;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;

public interface GridCell {
	public ReadableCellPosition getPosition();
	@Nullable
	public String getSymbol();
}
