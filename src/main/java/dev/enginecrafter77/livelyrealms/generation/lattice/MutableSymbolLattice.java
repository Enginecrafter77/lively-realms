package dev.enginecrafter77.livelyrealms.generation.lattice;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;

public interface MutableSymbolLattice extends SymbolLattice {
	public void setSymbolAt(ReadableCellPosition position, @Nullable String symbol);
}
