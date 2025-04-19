package dev.enginecrafter77.livelyrealms.generation;

import com.google.common.collect.ImmutableSet;

import java.util.Set;

public interface CellMatcher {
	public boolean matches(GeneratorContext context, ReadableCellPosition cell);

	public default CellMatcher and(CellMatcher other)
	{
		return (context, cell) -> {
			if(!this.matches(context, cell))
				return false;
			return other.matches(context, cell);
		};
	}

	public static CellMatcher isSymbol(String symbol)
	{
		return CellMatcher.isSymbolIn(ImmutableSet.of(symbol));
	}

	public static CellMatcher isSymbolIn(Set<String> symbols)
	{
		return (context, cell) -> symbols.contains(context.getSymbolMap().getSymbolAt(cell));
	}

	public static CellMatcher symbolExists(String symbol)
	{
		return (context, cell) -> context.getSymbolMap().symbolSet().contains(symbol);
	}
}
