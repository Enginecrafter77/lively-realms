package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3i;

public class StructureGenerationContext {
	public final Level level;
	public final BlockPos anchor;
	public final int cellSize;

	public StructureGenerationContext(Level level, BlockPos anchor, int cellSize)
	{
		this.level = level;
		this.anchor = anchor;
		this.cellSize = cellSize;
	}

	public void getEnclosingCell(BlockPos pos, CellPosition cellOut)
	{
		cellOut.set(pos.getX(), pos.getY(), pos.getZ());
		cellOut.sub(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
		cellOut.div(this.cellSize);
	}

	public void getCellAnchor(ReadableCellPosition position, Vector3i out)
	{
		out.set(position);
		out.mul(this.cellSize);
		out.add(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
	}

	public BlockPos getCellAnchorBlockPos(ReadableCellPosition position)
	{
		Vector3i out = new Vector3i();
		this.getCellAnchor(position, out);
		return new BlockPos(out.x, out.y, out.z);
	}
}
