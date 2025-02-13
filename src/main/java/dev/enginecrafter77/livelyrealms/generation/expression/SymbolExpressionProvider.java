package dev.enginecrafter77.livelyrealms.generation.expression;

import javax.annotation.Nullable;

public interface SymbolExpressionProvider {
	@Nullable
	public SymbolExpression getExpression(String symbol);
	public int getCellSize();
}
