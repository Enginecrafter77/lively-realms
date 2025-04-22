package dev.enginecrafter77.livelyrealms.generation.grid;

import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import javax.annotation.Nullable;

public class GridCellCursor implements GridCell {
	private final CellPosition position;
	@Nullable
	private String symbol;

	public GridCellCursor()
	{
		this.position = new CellPosition();
		this.symbol = null;
	}

	public void update(ReadableCellPosition position, @Nullable String symbol)
	{
		this.position.set(position);
		this.symbol = symbol;
	}

	@Override
	public ReadableCellPosition getPosition()
	{
		return this.position;
	}

	@Nullable
	@Override
	public String getSymbol()
	{
		return this.symbol;
	}
}
