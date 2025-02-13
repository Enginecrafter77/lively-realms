package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.lattice.SymbolLattice;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class VirtualStructureMap implements SymbolLattice, GrammarContext, SymbolAcceptor {
	public static final String EPSILON = "eps";

	private final Map<ReadableCellPosition, String> symbols;

	public VirtualStructureMap()
	{
		this.symbols = new HashMap<ReadableCellPosition, String>();
	}

	@Nullable
	@Override
	public String getSymbolAt(ReadableCellPosition position)
	{
		return this.symbols.getOrDefault(ImmutableCellPosition.copyOf(position), EPSILON);
	}

	@Override
	public SymbolLattice getEnvironment()
	{
		return this;
	}

	@Override
	public void acceptSymbol(ReadableCellPosition cell, String symbol)
	{
		this.symbols.put(ImmutableCellPosition.copyOf(cell), symbol);
	}
}
