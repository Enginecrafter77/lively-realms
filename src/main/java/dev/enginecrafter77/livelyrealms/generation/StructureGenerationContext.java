package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.joml.Vector3ic;

public class StructureGenerationContext implements SymbolExpressionContext {
	public final Level level;
	public final BlockPos anchor;
	public final GenerationProfile profile;

	public StructureGenerationContext(Level level, BlockPos anchor, GenerationProfile profile)
	{
		this.level = level;
		this.anchor = anchor;
		this.profile = profile;
	}

	@Override
	public Level getLevel()
	{
		return this.level;
	}

	@Override
	public GenerationProfile getGenerationProfile()
	{
		return this.profile;
	}

	@Override
	public void getEnclosingCell(BlockPos pos, CellPosition cellOut)
	{
		Vector3d dv = new Vector3d();
		dv.set(pos.getX(), pos.getY(), pos.getZ());
		dv.sub(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
		dv.div(this.getGenerationProfile().expressionProvider().getCellSize());
		dv.floor();
		cellOut.set(dv);
	}

	@Override
	public void getPositionInsideCell(ReadableCellPosition cellPosition, Vector3ic relativePosition, Vector3i out)
	{
		out.set(cellPosition);
		out.mul(this.profile.expressionProvider().getCellSize());
		out.add(this.anchor.getX(), this.anchor.getY(), this.anchor.getZ());
		out.add(relativePosition);
	}
}
