package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.expression.CellLocator;
import dev.enginecrafter77.livelyrealms.generation.plan.BuildContext;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface CellMutationContext extends GenerationProfileHolder {
	public VirtualStructureMap getSymbolMap();
	public Level getLevel();
	public CellLocator getCellLocator();

	public default BuildContext makeBuildContext(ReadableCellPosition cell)
	{
		return new BuildContext(this.getLevel(), this.getCellLocator().getBlockPositionInsideCell(cell, BlockPos.ZERO));
	}
}
