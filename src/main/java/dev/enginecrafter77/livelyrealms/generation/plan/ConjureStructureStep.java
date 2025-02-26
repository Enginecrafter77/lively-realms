package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.VectorSpaceIterator;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import org.joml.Vector3ic;

public class ConjureStructureStep implements StructureBuildStep {
	private final Structure structure;
	private final Vec3i offset;

	public ConjureStructureStep(Structure structure, Vec3i offset)
	{
		this.structure = structure;
		this.offset = offset;
	}

	public ConjureStructureStep(Structure structure)
	{
		this(structure, BlockPos.ZERO);
	}

	@Override
	public void perform(StructureBuildContext context)
	{
		BlockPos anchor = context.anchor().offset(this.offset);
		for(Vector3ic pos : VectorSpaceIterator.space(this.structure.getSize()))
		{
			Structure.StructureBlock block = this.structure.getBlockAt(pos);
			BlockPos absolutePos = anchor.offset(pos.x(), pos.y(), pos.z());
			context.level().setBlockAndUpdate(absolutePos, block.getBlockState());
		}
	}
}
