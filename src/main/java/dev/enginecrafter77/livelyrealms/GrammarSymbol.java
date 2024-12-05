package dev.enginecrafter77.livelyrealms;

import javax.annotation.Nullable;

public interface GrammarSymbol {
	public static final String EPSILON = "epsilon";

	public String getName();

	@Nullable
	public SymbolExpression getExpression();
}
