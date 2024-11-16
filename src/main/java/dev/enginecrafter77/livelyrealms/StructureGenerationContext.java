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

	public void anchorCell(CellPosition position, Vector3i out)
	{
		position.anchor(this.cellSize, out);
		out.add(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
	}

	public BlockPos getAnchorBlockPos(CellPosition position)
	{
		Vector3i out = new Vector3i();
		this.anchorCell(position, out);
		return new BlockPos(out.x, out.y, out.z);
	}
}
