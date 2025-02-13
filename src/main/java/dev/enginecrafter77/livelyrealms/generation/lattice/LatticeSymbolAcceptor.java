package dev.enginecrafter77.livelyrealms.generation.lattice;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import dev.enginecrafter77.livelyrealms.generation.SymbolAcceptor;

public class LatticeSymbolAcceptor implements SymbolAcceptor {
	private final MutableSymbolLattice target;

	public LatticeSymbolAcceptor(MutableSymbolLattice target)
	{
		this.target = target;
	}

	@Override
	public void acceptSymbol(ReadableCellPosition cell, String symbol)
	{
		this.target.setSymbolAt(cell, symbol);
	}

	public static LatticeSymbolAcceptor to(MutableSymbolLattice target)
	{
		return new LatticeSymbolAcceptor(target);
	}
}
