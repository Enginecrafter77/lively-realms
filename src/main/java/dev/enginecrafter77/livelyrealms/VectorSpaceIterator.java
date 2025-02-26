package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.structure.NaturalVoxelIndexer;
import dev.enginecrafter77.livelyrealms.structure.VoxelIndexer;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.Iterator;

public class VectorSpaceIterator implements Iterator<Vector3ic> {
	public final VoxelIndexer indexer;
	private final Vector3i out;
	private int index;

	public VectorSpaceIterator(Vector3ic space)
	{
		this.indexer = new NaturalVoxelIndexer(space);
		this.out = new Vector3i();
		this.index = -1;
	}

	@Override
	public boolean hasNext()
	{
		return (this.index + 1) < this.indexer.volume();
	}

	@Override
	public Vector3ic next()
	{
		++this.index;
		this.indexer.fromIndex(this.index, this.out);
		return this.out;
	}

	public static VectorSpaceIterator of(Vector3ic space)
	{
		return new VectorSpaceIterator(space);
	}

	public static Iterable<Vector3ic> space(Vector3ic space)
	{
		return new Iterable<Vector3ic>() {
			@Override
			public @NotNull Iterator<Vector3ic> iterator()
			{
				return VectorSpaceIterator.of(space);
			}
		};
	}
}
