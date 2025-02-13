package dev.enginecrafter77.livelyrealms.generation.lattice;

import com.google.common.base.Predicates;
import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public abstract class EnumerableSymbolLattice implements SymbolLattice, Iterable<LatticeCell> {
	public abstract Set<? extends ReadableCellPosition> getPositions();

	public Set<String> getPalette()
	{
		return this.getPositions().stream().map(this::getSymbolAt).filter(Predicates.notNull()).collect(Collectors.toSet());
	}

	@NotNull
	@Override
	public Iterator<LatticeCell> iterator()
	{
		return new LatticeIterator(this.getPositions().iterator());
	}

	public boolean match(SymbolLattice in, ReadableCellPosition at)
	{
		CellPosition position = new CellPosition();
		for(LatticeCell cell : this)
		{
			position.set(cell.getPosition());
			position.add(at);
			String outerSymbol = in.getSymbolAt(position);
			if(!Objects.equals(outerSymbol, cell.getSymbol()))
				return false;
		}
		return true;
	}

	protected class LatticeIterator implements Iterator<LatticeCell>
	{
		private final Iterator<? extends ReadableCellPosition> positions;
		private final LatticeCellCursor cursor;

		public LatticeIterator(Iterator<? extends ReadableCellPosition> positions)
		{
			this.positions = positions;
			this.cursor = new LatticeCellCursor();
		}

		@Override
		public boolean hasNext()
		{
			return this.positions.hasNext();
		}

		@Override
		public LatticeCell next()
		{
			ReadableCellPosition position = this.positions.next();
			String symbol = EnumerableSymbolLattice.this.getSymbolAt(position);
			this.cursor.update(position, symbol);
			return this.cursor;
		}
	}

}
