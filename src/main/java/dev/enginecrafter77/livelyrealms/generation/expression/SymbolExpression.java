package dev.enginecrafter77.livelyrealms.generation.expression;

import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

public interface SymbolExpression {
	public void build(SymbolExpressionContext context, ReadableCellPosition position);
}
