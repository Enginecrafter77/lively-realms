package dev.enginecrafter77.livelyrealms;

import javax.annotation.Nullable;

public interface SymbolExpressionProvider {
	@Nullable
	public SymbolExpression getExpression(String symbol);
	public int getCellSize();
}
