package dev.enginecrafter77.livelyrealms.generation;

import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionProvider;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionRegistry;

import java.util.function.Supplier;

public record GenerationProfile(Grammar grammar, SymbolExpressionProvider expressionProvider) {
	public static Supplier<GenerationProfile> using(GenerationProfileConfiguration config)
	{
		return () -> {
			Grammar.GrammarBuilder grammarBuilder = Grammar.builder();
			SymbolExpressionRegistry.SymbolExpressionRegistryBuilder expressionRegistryBuilder = SymbolExpressionRegistry.builder();
			config.configure(grammarBuilder, expressionRegistryBuilder);
			return new GenerationProfile(grammarBuilder.build(), expressionRegistryBuilder.build());
		};
	}

	@FunctionalInterface
	public static interface GenerationProfileConfiguration {
		public void configure(Grammar.GrammarBuilder grammarBuilder, SymbolExpressionRegistry.SymbolExpressionRegistryBuilder expressionRegistryBuilder);
	}
}
