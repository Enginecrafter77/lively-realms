package dev.enginecrafter77.livelyrealms.generation.lattice;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;

public interface SymbolLattice {
	@Nullable
	public String getSymbolAt(ReadableCellPosition position);
}
