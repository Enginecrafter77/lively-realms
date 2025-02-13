package dev.enginecrafter77.livelyrealms.generation;

import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

public abstract class CommonReadableCellPosition implements ReadableCellPosition {
	@Override
	public IntBuffer get(IntBuffer buffer)
	{
		buffer.put(this.x());
		buffer.put(this.y());
		buffer.put(this.z());
		return buffer;
	}

	@Override
	public IntBuffer get(int index, IntBuffer buffer)
	{
		buffer.put(index, this.x());
		buffer.put(index+1, this.y());
		buffer.put(index+2, this.z());
		return buffer;
	}

	private void intToBytes(int value, byte[] dest)
	{
		dest[0] = (byte)((value >> 24) & 0xFF);
		dest[1] = (byte)((value >> 16) & 0xFF);
		dest[2] = (byte)((value >> 8) & 0xFF);
		dest[3] = (byte)(value & 0xFF);
	}

	@Override
	public ByteBuffer get(ByteBuffer buffer)
	{
		byte[] buf = new byte[4];
		intToBytes(this.x(), buf);
		buffer.put(buf);
		intToBytes(this.y(), buf);
		buffer.put(buf);
		intToBytes(this.z(), buf);
		buffer.put(buf);
		return buffer;
	}

	@Override
	public ByteBuffer get(int index, ByteBuffer buffer)
	{
		byte[] buf = new byte[4];
		intToBytes(this.x(), buf);
		buffer.put(index, buf);
		intToBytes(this.y(), buf);
		buffer.put(index + 4, buf);
		intToBytes(this.z(), buf);
		buffer.put(index + 8, buf);
		return buffer;
	}

	@Override
	public Vector3ic getToAddress(long address)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public Vector3i sub(Vector3ic v, Vector3i dest)
	{
		return this.sub(v.x(), v.y(), v.z(), dest);
	}

	@Override
	public Vector3i sub(int x, int y, int z, Vector3i dest)
	{
		return this.add(-x, -y, -z, dest);
	}

	@Override
	public Vector3i add(Vector3ic v, Vector3i dest)
	{
		return this.add(v.x(), v.y(), v.z(), dest);
	}

	@Override
	public Vector3i add(int x, int y, int z, Vector3i dest)
	{
		dest.x = this.x() + x;
		dest.y = this.y() + y;
		dest.z = this.z() + z;
		return dest;
	}

	@Override
	public Vector3i mul(int scalar, Vector3i dest)
	{
		return this.mul(scalar, scalar, scalar, dest);
	}

	@Override
	public Vector3i mul(Vector3ic v, Vector3i dest)
	{
		return this.mul(v.x(), v.y(), v.z(), dest);
	}

	@Override
	public Vector3i mul(int x, int y, int z, Vector3i dest)
	{
		dest.x = this.x() * x;
		dest.y = this.y() * y;
		dest.z = this.z() * z;
		return dest;
	}

	@Override
	public Vector3i div(float scalar, Vector3i dest)
	{
		return this.div((int)scalar, dest);
	}

	@Override
	public Vector3i div(int scalar, Vector3i dest)
	{
		dest.x = this.x() / scalar;
		dest.y = this.y() / scalar;
		dest.z = this.z() / scalar;
		return dest;
	}

	@Override
	public long lengthSquared()
	{
		long x = this.x();
		long y = this.y();
		long z = this.z();
		return x*x + y*y + z*z;
	}

	@Override
	public double length()
	{
		return Math.sqrt(this.lengthSquared());
	}

	@Override
	public double distance(Vector3ic v)
	{
		return this.distance(v.x(), v.y(), v.z());
	}

	@Override
	public double distance(int x, int y, int z)
	{
		return Math.sqrt(this.distanceSquared(x, y, z));
	}

	@Override
	public long gridDistance(Vector3ic v)
	{
		return this.gridDistance(v.x(), v.y(), v.z());
	}

	@Override
	public long gridDistance(int x, int y, int z)
	{
		long dx = this.x() - x;
		long dy = this.y() - y;
		long dz = this.z() - z;
		return dx + dy + dz;
	}

	@Override
	public long distanceSquared(Vector3ic v)
	{
		return this.distanceSquared(v.x(), v.y(), v.z());
	}

	@Override
	public long distanceSquared(int x, int y, int z)
	{
		long dx = this.x() - x;
		long dy = this.y() - y;
		long dz = this.z() - z;
		return dx*dx + dy*dy + dz*dz;
	}

	@Override
	public Vector3i negate(Vector3i dest)
	{
		dest.x = -this.x();
		dest.y = -this.y();
		dest.z = -this.z();
		return dest;
	}

	@Override
	public Vector3i min(Vector3ic v, Vector3i dest)
	{
		dest.x = Math.min(this.x(), v.x());
		dest.y = Math.min(this.y(), v.y());
		dest.z = Math.min(this.z(), v.z());
		return dest;
	}

	@Override
	public Vector3i max(Vector3ic v, Vector3i dest)
	{
		dest.x = Math.max(this.x(), v.x());
		dest.y = Math.max(this.y(), v.y());
		dest.z = Math.max(this.z(), v.z());
		return dest;
	}

	@Override
	public int get(int component) throws IllegalArgumentException
	{
		switch(component)
		{
		case 0:
			return this.x();
		case 1:
			return this.y();
		case 2:
			return this.z();
		default:
			throw new IllegalArgumentException();
		}
	}

	@Override
	public int maxComponent()
	{
		return Math.max(this.x(), Math.max(this.y(), this.z()));
	}

	@Override
	public int minComponent()
	{
		return Math.min(this.x(), Math.min(this.y(), this.z()));
	}

	@Override
	public Vector3i absolute(Vector3i dest)
	{
		dest.x = Math.abs(this.x());
		dest.y = Math.abs(this.y());
		dest.z = Math.abs(this.z());
		return dest;
	}

	@Override
	public boolean equals(int x, int y, int z)
	{
		return this.x() == x && this.y() == y && this.z() == z;
	}
}
