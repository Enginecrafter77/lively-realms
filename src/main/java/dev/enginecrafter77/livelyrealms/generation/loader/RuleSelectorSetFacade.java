package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.WeightedRandomSelector.WeightedRandomSelectorBuilder.SelectorSetBuilder;

public class RuleSelectorSetFacade {
	private final SelectorSetBuilder setBuilder;

	public RuleSelectorSetFacade(SelectorSetBuilder setBuilder)
	{
		this.setBuilder = setBuilder;
	}

	public RuleSelectorOptionFacade option(String symbol)
	{
		return new RuleSelectorOptionFacade(symbol);
	}

	public class RuleSelectorOptionFacade
	{
		private final String symbol;

		public RuleSelectorOptionFacade(String symbol)
		{
			this.symbol = symbol;
		}

		public void with(double probability)
		{
			RuleSelectorSetFacade.this.setBuilder.option(this.symbol, probability);
		}
	}
}
