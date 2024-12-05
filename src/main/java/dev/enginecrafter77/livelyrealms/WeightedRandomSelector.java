package dev.enginecrafter77.livelyrealms;

import java.util.Collection;
import java.util.Random;

public class WeightedRandomSelector implements AlternativeSelector {
	private final WeightVector<String> weightVector;
	private final Random rng;

	public WeightedRandomSelector(Random rng, WeightVector<String> vector)
	{
		this.weightVector = vector;
		this.rng = rng;
	}

	@Override
	public String select(Collection<String> options, GrammarContext context, CellPosition position)
	{
		double num = this.rng.nextDouble();
		return this.weightVector.at(num).getItem();
	}

	public static WeightedRandomSelectorBuilder builder()
	{
		return new WeightedRandomSelectorBuilder();
	}

	public static class WeightedRandomSelectorBuilder
	{
		private final WeightVector.WeightVectorBuilder<String> vectorBuilder;
		private Random rng;

		public WeightedRandomSelectorBuilder()
		{
			this.vectorBuilder = WeightVector.builder();
			this.rng = new Random();
		}

		public WeightedRandomSelectorBuilder withOption(String nonterminal, double weight)
		{
			this.vectorBuilder.add(nonterminal, weight);
			return this;
		}

		public WeightedRandomSelectorBuilder withEpsilon(double weight)
		{
			return this.withOption(GrammarSymbol.EPSILON, weight);
		}

		public WeightedRandomSelectorBuilder withRng(Random rng)
		{
			this.rng = rng;
			return this;
		}

		public WeightedRandomSelector build()
		{
			return new WeightedRandomSelector(this.rng, this.vectorBuilder.build());
		}
	}
}
