package dev.enginecrafter77.livelyrealms;

import org.joml.Vector3i;

public class CellPosition extends Vector3i implements ReadableCellPosition {
	public static final CellPosition ORIGIN = new CellPosition(0, 0, 0);

	public CellPosition(int x, int y, int z)
	{
		super(x, y, z);
	}

	public CellPosition(Vector3i vec)
	{
		super(vec);
	}

	public CellPosition()
	{
		super();
	}

	public CellPosition copy()
	{
		return new CellPosition(this);
	}

	public ImmutableCellPosition freeze()
	{
		return new ImmutableCellPosition(this);
	}

	public void getPosition(Vector3i dest)
	{
		dest.set(this);
	}

	public void anchor(int cellSize, Vector3i dest)
	{
		dest.set(this);
		dest.mul(cellSize);
	}

	public static CellPosition of(int x, int y, int z)
	{
		return new CellPosition(x, y, z);
	}
}
