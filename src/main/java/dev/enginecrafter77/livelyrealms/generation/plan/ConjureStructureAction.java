package dev.enginecrafter77.livelyrealms.generation.plan;

import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.state.BlockState;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.util.Objects;

public class ConjureStructureAction extends BuildStepAction {
	private final Structure structure;
	private final Vec3i offset;

	public ConjureStructureAction(Structure structure, Vec3i offset)
	{
		this.structure = structure;
		this.offset = offset;
	}

	public ConjureStructureAction(Structure structure)
	{
		this(structure, BlockPos.ZERO);
	}

	private Iterable<BlockPos> structureSpace()
	{
		Vector3ic size = this.structure.getSize();
		return BlockPos.betweenClosed(0, 0, 0, size.x() - 1, size.y() - 1, size.z() - 1);
	}

	@Override
	public void perform(BuildContext context)
	{
		context = context.shifted(this.offset);
		Vector3i vPos = new Vector3i();
		for(BlockPos pos : this.structureSpace())
		{
			vPos.set(pos.getX(), pos.getY(), pos.getZ());
			Structure.StructureBlock block = this.structure.getBlockAt(vPos);
			context.setBlockAndUpdate(pos, block.getBlockState());
		}
	}

	@Override
	public boolean isComplete(BuildContext context)
	{
		context = context.shifted(this.offset);
		Vector3i vPos = new Vector3i();
		for(BlockPos pos : this.structureSpace())
		{
			vPos.set(pos.getX(), pos.getY(), pos.getZ());
			Structure.StructureBlock block = this.structure.getBlockAt(vPos);
			BlockState state = context.getBlockState(pos);
			if(!Objects.equals(state, block.getBlockState()))
				return false;
		}
		return true;
	}

	@Override
	public boolean hasHotspot()
	{
		return true;
	}

	@Override
	public void getHotspot(BuildContext context, Vector3d dest)
	{
		BlockPos anchor = context.anchor();
		Vector3ic size = this.structure.getSize();
		dest.x = anchor.getX() + this.offset.getX() + size.x() / 2F;
		dest.y = anchor.getY() + this.offset.getY() + size.y() / 2F;
		dest.z = anchor.getZ() + this.offset.getZ() + size.z() / 2F;
	}
}
