package dev.enginecrafter77.livelyrealms.generation.lattice;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;

public interface LatticeCell {
	public ReadableCellPosition getPosition();
	@Nullable
	public String getSymbol();
}
