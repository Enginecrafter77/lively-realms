package dev.enginecrafter77.livelyrealms.generation.grid;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import dev.enginecrafter77.livelyrealms.generation.SymbolAcceptor;

public class LatticeSymbolAcceptor implements SymbolAcceptor {
	private final MutableSymbolGrid target;

	public LatticeSymbolAcceptor(MutableSymbolGrid target)
	{
		this.target = target;
	}

	@Override
	public void acceptSymbol(ReadableCellPosition cell, String symbol)
	{
		this.target.setSymbolAt(cell, symbol);
	}

	public static LatticeSymbolAcceptor to(MutableSymbolGrid target)
	{
		return new LatticeSymbolAcceptor(target);
	}
}
