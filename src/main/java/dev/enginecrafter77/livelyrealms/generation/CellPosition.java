package dev.enginecrafter77.livelyrealms.generation;

import net.minecraft.core.Vec3i;
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

	public Vector3i add(Vec3i vector)
	{
		this.x += vector.getX();
		this.y += vector.getY();
		this.z += vector.getZ();
		return this;
	}

	public Vector3i set(Vec3i vector)
	{
		this.x = vector.getX();
		this.y = vector.getY();
		this.z = vector.getZ();
		return this;
	}

	public static CellPosition of(int x, int y, int z)
	{
		return new CellPosition(x, y, z);
	}

	public static CellPosition copyOf(Vec3i from)
	{
		CellPosition pos = new CellPosition();
		pos.set(from);
		return pos;
	}
}
