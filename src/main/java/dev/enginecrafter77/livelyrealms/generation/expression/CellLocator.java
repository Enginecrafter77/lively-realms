package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import net.minecraft.core.BlockPos;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public interface CellLocator {
	public void getEnclosingCell(BlockPos pos, CellPosition cellOut);
	public void getPositionInsideCell(ReadableCellPosition cell, Vector3ic relativePosition, Vector3i out);

	public default BlockPos getBlockPositionInsideCell(ReadableCellPosition cell, Vector3ic relativePosition)
	{
		Vector3i out = new Vector3i();
		this.getPositionInsideCell(cell, relativePosition, out);
		return new BlockPos(out.x, out.y, out.z);
	}

	public default BlockPos getBlockPositionInsideCell(ReadableCellPosition cell, BlockPos relativePosition)
	{
		return this.getBlockPositionInsideCell(cell, new Vector3i(relativePosition.getX(), relativePosition.getY(), relativePosition.getZ()));
	}
}
