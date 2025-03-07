package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import net.minecraft.core.BlockPos;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.joml.Vector3ic;

@SuppressWarnings("ClassCanBeRecord")
public final class ContinuousCellLocator implements CellLocator {
	public final BlockPos anchor;
	public final int cellSize;

	public ContinuousCellLocator(BlockPos anchor, int cellSize)
	{
		this.anchor = anchor;
		this.cellSize = cellSize;
	}

	@Override
	public void getEnclosingCell(BlockPos pos, CellPosition cellOut)
	{
		Vector3d dv = new Vector3d();
		dv.set(pos.getX(), pos.getY(), pos.getZ());
		dv.sub(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
		dv.div(this.cellSize);
		dv.floor();
		cellOut.set(dv);
	}

	@Override
	public void getPositionInsideCell(ReadableCellPosition cellPosition, Vector3ic relativePosition, Vector3i out)
	{
		out.set(cellPosition);
		out.mul(this.cellSize);
		out.add(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
		out.add(relativePosition);
	}
}
