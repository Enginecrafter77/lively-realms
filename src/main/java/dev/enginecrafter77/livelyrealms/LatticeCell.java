package dev.enginecrafter77.livelyrealms;

import javax.annotation.Nullable;

public interface LatticeCell {
	public ReadableCellPosition getPosition();
	@Nullable
	public String getSymbol();
}
