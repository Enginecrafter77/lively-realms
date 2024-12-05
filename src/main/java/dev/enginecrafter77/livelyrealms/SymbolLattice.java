package dev.enginecrafter77.livelyrealms;

import javax.annotation.Nullable;

public interface SymbolLattice {
	@Nullable
	public String getSymbolAt(ReadableCellPosition position);
}
