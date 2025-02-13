package dev.enginecrafter77.livelyrealms.generation;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ImmutableCellPosition extends CommonReadableCellPosition {
	private static final int HASH_SALT = 227051551;

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
	public int hashCode()
	{
		return HASH_SALT * this.x + (HASH_SALT<<1) * this.y + (HASH_SALT<<2) * this.x;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(obj == null)
			return false;
		if(!(obj instanceof ImmutableCellPosition))
			return false;
		ImmutableCellPosition other = (ImmutableCellPosition)obj;
		return this.x == other.x && this.y == other.y && this.z == other.z;
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
