package dev.enginecrafter77.livelyrealms.structure;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3ic;

public class FilteredStructure implements Structure {
	private final StructureFilter filter;
	private final Structure structure;

	public FilteredStructure(Structure structure, StructureFilter filter)
	{
		this.structure = structure;
		this.filter = filter;
	}

	@Override
	public boolean isBlockDefined(Vector3ic position)
	{
		return this.structure.isBlockDefined(position) && this.filter.blockMatches(this.structure, position);
	}

	@Override
	public BlockState getBlockAt(Vector3ic position)
	{
		if(!this.isBlockDefined(position))
			throw new BlockNotDefinedException(this, position);
		return this.structure.getBlockAt(position);
	}

	@Nullable
	@Override
	public BlockEntity getBlockEntityAt(Vector3ic position)
	{
		if(!this.isBlockDefined(position))
			throw new BlockNotDefinedException(this, position);
		return this.structure.getBlockEntityAt(position);
	}

	@Override
	public Vector3ic getSize()
	{
		return this.structure.getSize();
	}

	public static FilteredStructure filter(Structure structure, StructureFilter filter)
	{
		return new FilteredStructure(structure, filter);
	}

	@FunctionalInterface
	public static interface StructureFilter
	{
		public boolean blockMatches(Structure structure, Vector3ic position);
	}
}
