package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import dev.enginecrafter77.livelyrealms.generation.*;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class WeightedRandomSelector implements RuleSelector {
	private final List<SelectorSet> selectorSets;
	private final Random rng;

	public WeightedRandomSelector(List<SelectorSet> sets, Random rng)
	{
		this.selectorSets = sets;
		this.rng = rng;
	}

	@Nullable
	@Override
	public GrammarRule select(GeneratorContext context, ReadableCellPosition expansionFor, Set<Grammar.GrammarRuleEntry> availableRules)
	{
		Set<String> ruleNames = availableRules.stream()
				.filter(Grammar.GrammarRuleEntry::isNamed)
				.map(Grammar.GrammarRuleEntry::name)
				.collect(Collectors.toUnmodifiableSet());
		SelectorSet set = this.selectorSets.stream()
				.filter(s -> s.matches(ruleNames))
				.findFirst()
				.orElse(null);
		if(set == null)
			return null;
		String selectedName = set.select(ruleNames, this.rng);
		return availableRules.stream()
				.filter(r -> Objects.equals(selectedName, r.name()))
				.findFirst()
				.map(Grammar.GrammarRuleEntry::rule)
				.orElse(null);
	}

	public static WeightedRandomSelectorBuilder builder()
	{
		return new WeightedRandomSelectorBuilder();
	}

	public static record SelectorSet(WeightMap<String> weightMap, double priority)
	{
		public static final Comparator<SelectorSet> BY_PRIORITY = Comparator.comparing(SelectorSet::priority);

		public boolean matches(Set<String> options)
		{
			return this.weightMap.options().containsAll(options) && options.containsAll(this.weightMap.options());
		}

		public String select(Set<String> options, Random rng)
		{
			double num = rng.nextDouble();
			return this.weightMap.at(num);
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
