package dev.enginecrafter77.livelyrealms.generation.grid;

import com.google.common.collect.ImmutableMap;
import dev.enginecrafter77.livelyrealms.generation.ImmutableCellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class SparseSymbolGrid extends EnumerableSymbolGrid {
	private final Map<ImmutableCellPosition, String> overrides;

	public SparseSymbolGrid(Map<ImmutableCellPosition, String> overrides)
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

	public SparseSymbolGridBuilder edit()
	{
		return SparseSymbolGrid.builder().copy(this);
	}

	public static SparseSymbolGridBuilder builder()
	{
		return new SparseSymbolGridBuilder();
	}

	public static class SparseSymbolGridBuilder
	{
		private final Map<ImmutableCellPosition, String> overrides;

		public SparseSymbolGridBuilder()
		{
			this.overrides = new HashMap<ImmutableCellPosition, String>();
		}

		public SparseSymbolGridBuilder copy(EnumerableSymbolGrid lattice)
		{
			for(GridCell cell : lattice)
			{
				if(cell.getSymbol() == null)
					continue;
				this.set(cell.getPosition(), cell.getSymbol());
			}
			return this;
		}

		public SparseSymbolGridBuilder set(ReadableCellPosition position, String symbol)
		{
			this.overrides.put(ImmutableCellPosition.copyOf(position), symbol);
			return this;
		}

		public SparseSymbolGrid build()
		{
			return new SparseSymbolGrid(ImmutableMap.copyOf(this.overrides));
		}
	}
}
