package dev.enginecrafter77.livelyrealms.generation.plan;

import com.google.common.base.Predicates;
import dev.enginecrafter77.livelyrealms.structure.NaturalVoxelIndexer;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import dev.enginecrafter77.livelyrealms.structure.VoxelIndexer;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.function.Predicate;

public abstract class FilteredStructureBuildPlan extends BuildPlan {
	private final Predicate<BlockState> filter;
	private final VoxelIndexer indexer;
	protected final Structure structure;

	public FilteredStructureBuildPlan(Structure structure)
	{
		this(structure, Predicates.alwaysTrue());
	}

	public FilteredStructureBuildPlan(Structure structure, Predicate<BlockState> filter)
	{
		this.indexer = NaturalVoxelIndexer.in(structure.getSize());
		this.structure = structure;
		this.filter = filter;
	}

	protected abstract BuildStepAction getActionFor(Vector3ic position);

	@Override
	public BuildStepAction getStepAction(int stepIndex)
	{
		Vector3i pos = new Vector3i();
		this.indexer.fromIndex(stepIndex, pos);
		Structure.StructureBlock block = this.structure.getBlockAt(pos);
		BlockState state = block.getBlockState();
		if(!this.filter.test(state))
			return NoopAction.INSTANCE;
		return this.getActionFor(pos);
	}

	@Override
	public int getStepCount()
	{
		return this.indexer.volume();
	}
}
