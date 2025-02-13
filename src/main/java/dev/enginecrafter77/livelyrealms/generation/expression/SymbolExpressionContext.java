package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface SymbolExpressionContext {
	public Level getLevel();
	public SymbolExpressionProvider getExpressionProvider();
	public void getEnclosingCell(BlockPos pos, CellPosition cellOut);
	public BlockPos getCellAnchorBlockPos(ReadableCellPosition position);
}
