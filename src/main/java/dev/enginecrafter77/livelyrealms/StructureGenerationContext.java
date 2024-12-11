package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3i;

public class StructureGenerationContext implements SymbolExpressionContext {
	public final Level level;
	public final BlockPos anchor;
	public final SymbolExpressionProvider provider;

	public StructureGenerationContext(Level level, BlockPos anchor, SymbolExpressionProvider provider)
	{
		this.level = level;
		this.anchor = anchor;
		this.provider = provider;
	}

	@Override
	public Level getLevel()
	{
		return this.level;
	}

	@Override
	public SymbolExpressionProvider getExpressionProvider()
	{
		return this.provider;
	}

	@Override
	public void getEnclosingCell(BlockPos pos, CellPosition cellOut)
	{
		cellOut.set(pos.getX(), pos.getY(), pos.getZ());
		cellOut.sub(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
		cellOut.div(this.provider.getCellSize());
	}

	@Override
	public BlockPos getCellAnchorBlockPos(ReadableCellPosition position)
	{
		Vector3i out = new Vector3i();
		this.getCellAnchor(position, out);
		return new BlockPos(out.x, out.y, out.z);
	}

	public void getCellAnchor(ReadableCellPosition position, Vector3i out)
	{
		out.set(position);
		out.mul(this.provider.getCellSize());
		out.add(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
	}
}
