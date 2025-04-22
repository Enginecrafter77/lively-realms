package dev.enginecrafter77.livelyrealms.generation.grid;

import com.google.common.base.Predicates;
import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class EnumerableSymbolGrid implements SymbolGrid, Iterable<GridCell> {
	public abstract Set<? extends ReadableCellPosition> getPositions();

	public Set<String> getPalette()
	{
		return this.getPositions().stream().map(this::getSymbolAt).filter(Predicates.notNull()).collect(Collectors.toSet());
	}

	@NotNull
	@Override
	public Iterator<GridCell> iterator()
	{
		return new LatticeIterator(this.getPositions().iterator());
	}

	public boolean match(SymbolGrid in, ReadableCellPosition at)
	{
		CellPosition position = new CellPosition();
		for(GridCell cell : this)
		{
			position.set(cell.getPosition());
			position.add(at);
			String outerSymbol = in.getSymbolAt(position);
			if(!Objects.equals(outerSymbol, cell.getSymbol()))
				return false;
		}
		return true;
	}

	protected class LatticeIterator implements Iterator<GridCell>
	{
		private final Iterator<? extends ReadableCellPosition> positions;
		private final GridCellCursor cursor;

		public LatticeIterator(Iterator<? extends ReadableCellPosition> positions)
		{
			this.positions = positions;
			this.cursor = new GridCellCursor();
		}

		@Override
		public boolean hasNext()
		{
			return this.positions.hasNext();
		}

		@Override
		public GridCell next()
		{
			ReadableCellPosition position = this.positions.next();
			String symbol = EnumerableSymbolGrid.this.getSymbolAt(position);
			this.cursor.update(position, symbol);
			return this.cursor;
		}
	}

}
