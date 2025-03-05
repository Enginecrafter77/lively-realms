package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.NaturalVoxelIndexer;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import dev.enginecrafter77.livelyrealms.structure.VoxelIndexer;
import net.minecraft.core.BlockPos;
import org.joml.Vector3i;

public class SimpleStructureBuildPlan extends BuildPlan {
	private final Structure structure;
	private final VoxelIndexer voxelIndexer;

	public SimpleStructureBuildPlan(Structure structure)
	{
		this.structure = structure;
		this.voxelIndexer = NaturalVoxelIndexer.in(structure.getSize());
	}

	@Override
	public int getStepCount()
	{
		return this.voxelIndexer.volume();
	}

	@Override
	public BuildStep getStep(int stepIndex)
	{
		Vector3i position = new Vector3i();
		this.voxelIndexer.fromIndex(stepIndex, position);
		return this.makeStep(stepIndex, new PlaceBlockAction(new BlockPos(position.x, position.y, position.z), structure.getBlockAt(position).getBlockState()));
	}
}
