package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.lattice.MutableSparseSymbolLattice;
import dev.enginecrafter77.livelyrealms.generation.lattice.MutableSymbolLattice;
import dev.enginecrafter77.livelyrealms.generation.lattice.SymbolLattice;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.Objects;

public class VirtualStructureMap implements GrammarContext, MutableSymbolLattice {
	private final MutableSparseSymbolLattice lattice;

	@Nonnull
	private final String epsilon;

	public VirtualStructureMap(@Nonnull String epsilon)
	{
		this.lattice = new MutableSparseSymbolLattice();
		this.epsilon = epsilon;
	}

	@Override
	public SymbolLattice getEnvironment()
	{
		return this;
	}

	@Override
	public void setSymbolAt(ReadableCellPosition position, @Nullable String symbol)
	{
		if(symbol == null)
			return;
		if(Objects.equals(symbol, this.epsilon))
			symbol = null;
		this.lattice.setSymbolAt(position, symbol);
	}

	@Override
	public @Nullable String getSymbolAt(ReadableCellPosition position)
	{
		String symbol = this.lattice.getSymbolAt(position);
		if(symbol == null)
			return this.epsilon;
		return symbol;
	}
}
