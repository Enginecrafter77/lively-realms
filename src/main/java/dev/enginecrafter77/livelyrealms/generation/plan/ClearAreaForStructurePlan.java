package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.VectorSpaceIterator;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3ic;

import java.util.function.Predicate;

public class ClearAreaForStructurePlan extends BuildPlan {
	private final Predicate<BlockState> shouldClear;
	private final Structure structure;

	private final int toClear;

	public ClearAreaForStructurePlan(Structure structure, Predicate<BlockState> shouldClear)
	{
		this.shouldClear = shouldClear;
		this.structure = structure;
		this.toClear = this.calculateBlocksToClear();
	}

	private int calculateBlocksToClear()
	{
		int matching = 0;
		VectorSpaceIterator iterator = new VectorSpaceIterator(this.structure.getSize());
		while(iterator.hasNext())
		{
			Vector3ic pos = iterator.next();
			Structure.StructureBlock block = this.structure.getBlockAt(pos);
			BlockState state = block.getBlockState();
			if(this.shouldClear.test(state))
				++matching;
		}
		return matching;
	}

	@Override
	public BuildStep getStep(int stepIndex)
	{
		int matchedIndex = -1;
		VectorSpaceIterator iterator = new VectorSpaceIterator(this.structure.getSize());
		while(iterator.hasNext())
		{
			Vector3ic pos = iterator.next();
			Structure.StructureBlock block = this.structure.getBlockAt(pos);
			BlockState state = block.getBlockState();
			if(this.shouldClear.test(state))
				++matchedIndex;
			if(matchedIndex == stepIndex)
			{
				BlockPos destPos = new BlockPos(pos.x(), pos.y(), pos.z());
				return new BuildStep(this, stepIndex, new ClearForBlockAction(destPos, state));
			}
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int getStepCount()
	{
		return this.toClear;
	}
}
