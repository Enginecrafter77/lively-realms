package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.NaturalVoxelIndexer;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import dev.enginecrafter77.livelyrealms.structure.VoxelIndexer;
import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public abstract class StructureBuildPlan extends BuildPlan {
	protected final Structure structure;

	@Nullable
	private VoxelIndexer indexer; // lazy init

	public StructureBuildPlan(Structure structure)
	{
		this.structure = structure;
		this.indexer = null;
	}

	public abstract BuildStepAction getActionFor(Vector3ic position);

	protected VoxelIndexer createIndexer(Structure structure)
	{
		return NaturalVoxelIndexer.in(structure.getSize());
	}

	protected BuildStepAction getUndefinedBlockAction(Vector3ic position)
	{
		return NoopAction.INSTANCE;
	}

	protected VoxelIndexer getIndexer()
	{
		if(this.indexer == null)
			this.indexer = this.createIndexer(this.structure);
		return this.indexer;
	}

	@Override
	public BuildStepAction getStepAction(int stepIndex)
	{
		Vector3i position = new Vector3i();
		this.getIndexer().fromIndex(stepIndex, position);
		if(!this.structure.isBlockDefined(position))
			return this.getUndefinedBlockAction(position);
		return this.getActionFor(position);
	}

	@Override
	public int getStepCount()
	{
		return this.getIndexer().volume();
	}

	public static BlockPos toBlockPos(Vector3ic pos)
	{
		return new BlockPos(pos.x(), pos.y(), pos.z());
	}
}
