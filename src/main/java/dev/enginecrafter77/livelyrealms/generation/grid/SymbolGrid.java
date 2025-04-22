package dev.enginecrafter77.livelyrealms.generation.grid;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;

public interface SymbolGrid {
	@Nullable
	public String getSymbolAt(ReadableCellPosition position);
}
