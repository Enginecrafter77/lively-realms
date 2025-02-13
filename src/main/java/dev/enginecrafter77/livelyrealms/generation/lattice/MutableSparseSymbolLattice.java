package dev.enginecrafter77.livelyrealms.generation.lattice;

import dev.enginecrafter77.livelyrealms.generation.ImmutableCellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class MutableSparseSymbolLattice extends EnumerableSymbolLattice implements MutableSymbolLattice {
	private final Map<ReadableCellPosition, @NonNull String> symbolMap;

	public MutableSparseSymbolLattice()
	{
		this.symbolMap = new HashMap<ReadableCellPosition, String>();
	}

	@Override
	public Set<? extends ReadableCellPosition> getPositions()
	{
		return this.symbolMap.keySet();
	}

	@Override
	public void setSymbolAt(ReadableCellPosition position, @Nullable String symbol)
	{
		if(symbol == null)
			this.symbolMap.remove(position);
		else
			this.symbolMap.put(ImmutableCellPosition.copyOf(position), symbol);
	}

	@Nullable
	@Override
	public String getSymbolAt(ReadableCellPosition position)
	{
		return this.symbolMap.get(ImmutableCellPosition.copyOf(position));
	}
}
