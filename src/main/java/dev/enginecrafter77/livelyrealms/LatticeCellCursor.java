package dev.enginecrafter77.livelyrealms;

import javax.annotation.Nullable;

public class LatticeCellCursor implements LatticeCell {
	private final CellPosition position;
	@Nullable
	private String symbol;

	public LatticeCellCursor()
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
