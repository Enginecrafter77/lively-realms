package dev.enginecrafter77.livelyrealms.generation.grid;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;

public interface MutableSymbolGrid extends SymbolGrid {
	public void setSymbolAt(ReadableCellPosition position, @Nullable String symbol);
}
