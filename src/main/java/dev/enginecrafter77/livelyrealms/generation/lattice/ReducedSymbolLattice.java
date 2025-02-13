package dev.enginecrafter77.livelyrealms.generation.lattice;

import com.google.common.collect.ImmutableMap;
import dev.enginecrafter77.livelyrealms.generation.ImmutableCellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

public final class ReducedSymbolLattice extends EnumerableSymbolLattice {
	private final Map<ImmutableCellPosition, String> overrides;

	public ReducedSymbolLattice(Map<ImmutableCellPosition, String> overrides)
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

	public static ReducedSymbolLatticeBuilder builder()
	{
		return new ReducedSymbolLatticeBuilder();
	}

	public static class ReducedSymbolLatticeBuilder
	{
		private final ImmutableMap.Builder<ImmutableCellPosition, String> overrides;

		public ReducedSymbolLatticeBuilder()
		{
			this.overrides = ImmutableMap.builder();
		}

		public ReducedSymbolLatticeBuilder copy(EnumerableSymbolLattice lattice)
		{
			for(LatticeCell cell : lattice)
			{
				if(cell.getSymbol() == null)
					continue;
				this.set(cell.getPosition(), cell.getSymbol());
			}
			return this;
		}

		public ReducedSymbolLatticeBuilder set(ReadableCellPosition position, String symbol)
		{
			this.overrides.put(ImmutableCellPosition.copyOf(position), symbol);
			return this;
		}

		public ReducedSymbolLattice build()
		{
			Map<ImmutableCellPosition, String> overrides = this.overrides.build();
			return new ReducedSymbolLattice(overrides);
		}
	}
}
