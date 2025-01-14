package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
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
		Vector3d dv = new Vector3d();
		dv.set(pos.getX(), pos.getY(), pos.getZ());
		dv.sub(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
		dv.div(this.provider.getCellSize());
		dv.floor();
		cellOut.set(dv);
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
