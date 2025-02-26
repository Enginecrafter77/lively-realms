package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.VectorSpaceIterator;
import dev.enginecrafter77.livelyrealms.structure.NaturalVoxelIndexer;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import dev.enginecrafter77.livelyrealms.structure.VoxelIndexer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.function.Predicate;

public class StraightForwardBuildPlan extends StructureBuildPlan {
	private final Structure structure;
	private final VoxelIndexer voxelIndexer;
	private final int significantBlocks;

	public StraightForwardBuildPlan(Structure structure)
	{
		this.structure = structure;
		this.significantBlocks = countBlocksInStructureMatching(structure, this::isBlockSignificant);
		this.voxelIndexer = NaturalVoxelIndexer.in(structure.getSize());
	}

	protected boolean isBlockSignificant(Structure.StructureBlock block)
	{
		return block.getBlockState().getBlock() != Blocks.AIR;
	}

	@Override
	public int getStepCount()
	{
		return this.significantBlocks;
	}

	@Override
	public StructureBuildStep getStep(int stepIndex)
	{
		Vector3i position = new Vector3i();
		Structure.StructureBlock block;
		int significantBlockIndex = -1;
		for(int blockIndex = 0; blockIndex < this.voxelIndexer.volume(); ++blockIndex)
		{
			this.voxelIndexer.fromIndex(blockIndex, position);
			block = this.structure.getBlockAt(position);
			if(this.isBlockSignificant(block))
				++significantBlockIndex;
			if(significantBlockIndex == stepIndex)
				return new PlaceBlockStep(new BlockPos(position.x, position.y, position.z), block.getBlockState());
		}
		throw new IndexOutOfBoundsException(stepIndex);
	}

	private static int countBlocksInStructureMatching(Structure structure, Predicate<Structure.StructureBlock> condition)
	{
		int matching = 0;
		for(Vector3ic position : VectorSpaceIterator.space(structure.getSize()))
		{
			if(condition.test(structure.getBlockAt(position)))
				++matching;
		}
		return matching;
	}
}
