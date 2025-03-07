package dev.enginecrafter77.livelyrealms.generation;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;

import javax.annotation.concurrent.Immutable;

@Immutable
public class ImmutableCellPosition extends CommonReadableCellPosition {
	public static final Codec<ImmutableCellPosition> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
							Codec.INT.fieldOf("x").forGetter(ImmutableCellPosition::x),
							Codec.INT.fieldOf("y").forGetter(ImmutableCellPosition::y),
							Codec.INT.fieldOf("z").forGetter(ImmutableCellPosition::z)
					)
					.apply(instance, ImmutableCellPosition::new)
	);
	public static final StreamCodec<ByteBuf, ImmutableCellPosition> STREAM_CODEC = StreamCodec.of((buffer, value) -> {
		buffer.writeInt(value.x);
		buffer.writeInt(value.y);
		buffer.writeInt(value.z);
	}, buffer -> {
		int x = buffer.readInt();
		int y = buffer.readInt();
		int z = buffer.readInt();
		return new ImmutableCellPosition(x, y, z);
	});

	public static final ImmutableCellPosition ZERO = new ImmutableCellPosition(0, 0, 0);

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
	public String toString()
	{
		return String.format("[%d,%d,%d]", this.x, this.y, this.z);
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

	public int[] toArray()
	{
		return new int[] {this.x, this.y, this.z};
	}

	public static ImmutableCellPosition of(int x, int y, int z)
	{
		return new ImmutableCellPosition(x, y, z);
	}

	public static ImmutableCellPosition copyOf(ReadableCellPosition other)
	{
		if(other instanceof ImmutableCellPosition)
			return (ImmutableCellPosition)other;
		return new ImmutableCellPosition(other);
	}

	public static ImmutableCellPosition fromArray(int[] array)
	{
		assert array.length == 3;
		return new ImmutableCellPosition(array[0], array[1], array[2]);
	}
}
