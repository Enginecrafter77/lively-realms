package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.Iterator;

public class BlockPosIterator implements Iterator<BlockPos> {
	private final BlockPos.MutableBlockPos mutableBlockPos;
	private final VectorSpaceIterator iterator;

	public BlockPosIterator(Vector3ic space)
	{
		this.iterator = new VectorSpaceIterator(space);
		this.mutableBlockPos = new BlockPos.MutableBlockPos();
	}

	@Override
	public boolean hasNext()
	{
		return this.iterator.hasNext();
	}

	@Override
	public BlockPos next()
	{
		Vector3ic pos = this.iterator.next();
		this.mutableBlockPos.set(pos.x(), pos.y(), pos.z());
		return this.mutableBlockPos;
	}

	public static BlockPosIterator of(Vector3ic space)
	{
		return new BlockPosIterator(space);
	}

	public static BlockPosIterator of(Vec3i space)
	{
		return new BlockPosIterator(new Vector3i(space.getX(), space.getY(), space.getZ()));
	}

	public static Iterable<BlockPos> blocks(Vector3ic space)
	{
		return new Iterable<BlockPos>() {
			@Override
			public @NotNull Iterator<BlockPos> iterator()
			{
				return BlockPosIterator.of(space);
			}
		};
	}

	public static Iterable<BlockPos> blocks(Vec3i space)
	{
		return new Iterable<BlockPos>() {
			@Override
			public @NotNull Iterator<BlockPos> iterator()
			{
				return BlockPosIterator.of(space);
			}
		};
	}
}
