package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface SymbolExpressionContext {
	public Level getLevel();
	public SymbolExpressionProvider getExpressionProvider();
	public void getEnclosingCell(BlockPos pos, CellPosition cellOut);
	public BlockPos getCellAnchorBlockPos(ReadableCellPosition position);
}
