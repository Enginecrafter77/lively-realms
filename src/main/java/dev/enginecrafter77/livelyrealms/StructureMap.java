package dev.enginecrafter77.livelyrealms;

import javax.annotation.Nullable;

public interface StructureMap extends SymbolLattice {
	public void putSymbol(ReadableCellPosition position, GrammarSymbol symbol);
	@Nullable
	public GrammarSymbol getSymbol(ReadableCellPosition position);

	@Nullable
	@Override
	public default String getSymbolAt(ReadableCellPosition position)
	{
		GrammarSymbol symbol = this.getSymbol(position);
		if(symbol == null)
			return null;
		return symbol.getName();
	}
}
