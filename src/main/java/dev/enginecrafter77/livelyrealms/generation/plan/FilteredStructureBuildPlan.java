package dev.enginecrafter77.livelyrealms.generation.plan;

import com.google.common.base.Predicates;
import dev.enginecrafter77.livelyrealms.VectorSpaceIterator;
import dev.enginecrafter77.livelyrealms.structure.NaturalVoxelIndexer;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3ic;

import java.util.function.Predicate;

public abstract class FilteredStructureBuildPlan extends BuildPlan {
	private final Predicate<BlockState> filter;
	private final int filteredStepCount;

	protected final Structure structure;

	public FilteredStructureBuildPlan(Structure structure)
	{
		this.filteredStepCount = NaturalVoxelIndexer.in(structure.getSize()).volume();
		this.filter = Predicates.alwaysTrue();
		this.structure = structure;
	}

	public FilteredStructureBuildPlan(Structure structure, Predicate<BlockState> filter)
	{
		this.filteredStepCount = this.calculateFilteredStepCount();
		this.structure = structure;
		this.filter = filter;
	}

	protected abstract BuildStepAction getActionFor(Vector3ic position);

	private int calculateFilteredStepCount()
	{
		int matching = 0;
		VectorSpaceIterator iterator = new VectorSpaceIterator(this.structure.getSize());
		while(iterator.hasNext())
		{
			Vector3ic pos = iterator.next();
			Structure.StructureBlock block = this.structure.getBlockAt(pos);
			BlockState state = block.getBlockState();
			if(this.filter.test(state))
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
			if(this.filter.test(state))
				++matchedIndex;
			if(matchedIndex == stepIndex)
				return new BuildStep(this, stepIndex, this.getActionFor(pos));
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public int getStepCount()
	{
		return this.filteredStepCount;
	}
}
