package dev.enginecrafter77.livelyrealms;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ImmutableCellPosition extends CommonReadableCellPosition {
	private final int x;
	private final int y;
	private final int z;

	public ImmutableCellPosition(int x, int y, int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}

	public ImmutableCellPosition(ReadableCellPosition other)
	{
		this(other.x(), other.y(), other.z());
	}

	@Override
	public int x()
	{
		return this.x;
	}

	@Override
	public int y()
	{
		return this.y;
	}

	@Override
	public int z()
	{
		return this.z;
	}

	public static ImmutableCellPosition copyOf(ReadableCellPosition other)
	{
		if(other instanceof ImmutableCellPosition)
			return (ImmutableCellPosition)other;
		return new ImmutableCellPosition(other);
	}
}
