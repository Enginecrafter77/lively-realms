package dev.enginecrafter77.livelyrealms.generation.lattice;

import com.google.common.collect.ImmutableMap;
import dev.enginecrafter77.livelyrealms.generation.ImmutableCellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SparseSymbolLattice extends EnumerableSymbolLattice {
	private final Map<ImmutableCellPosition, String> overrides;

	public SparseSymbolLattice(Map<ImmutableCellPosition, String> overrides)
	{
		this.overrides = overrides;
	}

	@Nullable
	@Override
	public String getSymbolAt(ReadableCellPosition cellPosition)
	{
		return this.overrides.get(ImmutableCellPosition.copyOf(cellPosition));
	}

	@Override
	public Set<? extends ReadableCellPosition> getPositions()
	{
		return this.overrides.keySet();
	}

	public SparseSymbolLatticeBuilder edit()
	{
		return SparseSymbolLattice.builder().copy(this);
	}

	public static SparseSymbolLatticeBuilder builder()
	{
		return new SparseSymbolLatticeBuilder();
	}

	public static class SparseSymbolLatticeBuilder
	{
		private final Map<ImmutableCellPosition, String> overrides;

		public SparseSymbolLatticeBuilder()
		{
			this.overrides = new HashMap<ImmutableCellPosition, String>();
		}

		public SparseSymbolLatticeBuilder copy(EnumerableSymbolLattice lattice)
		{
			for(LatticeCell cell : lattice)
			{
				if(cell.getSymbol() == null)
					continue;
				this.set(cell.getPosition(), cell.getSymbol());
			}
			return this;
		}

		public SparseSymbolLatticeBuilder set(ReadableCellPosition position, String symbol)
		{
			this.overrides.put(ImmutableCellPosition.copyOf(position), symbol);
			return this;
		}

		public SparseSymbolLattice build()
		{
			return new SparseSymbolLattice(ImmutableMap.copyOf(this.overrides));
		}
	}
}
