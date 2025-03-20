package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import dev.enginecrafter77.livelyrealms.generation.GenerationProfile;
import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.GrammarContext;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class WeightedRandomSelector implements RuleSelector {
	private final List<SelectorSet> selectorSets;
	private final Random rng;

	public WeightedRandomSelector(List<SelectorSet> sets, Random rng)
	{
		this.selectorSets = sets;
		this.rng = rng;
	}

	@Override
	public int select(GenerationProfile profile, GrammarContext context, ReadableCellPosition expansionFor, List<Grammar.GrammarRuleEntry> availableRules)
	{
		List<String> ruleNames = availableRules.stream().filter(Grammar.GrammarRuleEntry::isNamed).map(Grammar.GrammarRuleEntry::name).toList();
		SelectorSet set = this.selectorSets.stream()
				.filter(s -> s.matches(ruleNames))
				.findFirst()
				.orElse(null);
		if(set == null)
			return -1;
		return set.select(ruleNames, this.rng);
	}

	public static WeightedRandomSelectorBuilder builder()
	{
		return new WeightedRandomSelectorBuilder();
	}

	public static record SelectorSet(WeightMap<String> weightMap, double priority)
	{
		public static final Comparator<SelectorSet> BY_PRIORITY = Comparator.comparing(SelectorSet::priority);

		public boolean matches(Collection<String> options)
		{
			return Objects.equals(ImmutableSet.copyOf(options), ImmutableSet.copyOf(this.weightMap.options()));
		}

		public int select(List<String> options, Random rng)
		{
			double num = rng.nextDouble();
			String option = this.weightMap.at(num);
			return options.indexOf(option);
		}
	}

	public static class WeightedRandomSelectorBuilder
	{
		private final ImmutableSet.Builder<SelectorSet> sets;

		@Nullable
		private Random rng;

		public WeightedRandomSelectorBuilder()
		{
			this.sets = ImmutableSet.builder();
			this.rng = null;
		}

		public SelectorSetBuilder beginSet()
		{
			return new SelectorSetBuilder();
		}

		public WeightedRandomSelectorBuilder withSet(SelectorSet set)
		{
			this.sets.add(set);
			return this;
		}

		public WeightedRandomSelectorBuilder withRNG(Random rng)
		{
			this.rng = rng;
			return this;
		}

		public WeightedRandomSelector build()
		{
			Random rng = this.rng;
			if(rng == null)
				rng = new Random();
			return new WeightedRandomSelector(this.sets.build().stream().sorted(SelectorSet.BY_PRIORITY).toList(), rng);
		}

		public class SelectorSetBuilder
		{
			private final WeightMap.WeightMapBuilder<String> weightMapBuilder;
			private double priority;

			public SelectorSetBuilder()
			{
				this.weightMapBuilder = WeightMap.builder();
				this.priority = 1.0;
			}

			public SelectorSetBuilder withPriority(double priority)
			{
				this.priority = priority;
				return this;
			}

			public SelectorSetBuilder option(String symbol, double probability)
			{
				this.weightMapBuilder.entry(symbol, probability);
				return this;
			}

			public WeightedRandomSelectorBuilder endSet()
			{
				return WeightedRandomSelectorBuilder.this.withSet(new SelectorSet(this.weightMapBuilder.build(), this.priority));
			}
		}
	}
}
