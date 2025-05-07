package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.RuleSelector;
import dev.enginecrafter77.livelyrealms.generation.GeneratorContext;
import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.GrammarRule;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class RuleSelectorEnvironment {
	public final GeneratorContext context;
	public final ReadableCellPosition position;
	public final Set<Grammar.GrammarRuleEntry> rules;

	@Nullable
	private GrammarRule selected;

	public RuleSelectorEnvironment(GeneratorContext context, ReadableCellPosition position, Set<Grammar.GrammarRuleEntry> rules)
	{
		this.context = context;
		this.position = position;
		this.rules = rules;
		this.selected = null;
	}

	private Stream<String> ruleNames()
	{
		return this.rules.stream().map(Grammar.GrammarRuleEntry::name).filter(Objects::nonNull);
	}

	public boolean isSelectingFrom(String... ruleNames)
	{
		return this.isSelectingFrom(Arrays.asList(ruleNames));
	}

	public boolean isSelectingFrom(Collection<String> ruleNames)
	{
		if(this.rules.stream().anyMatch(Predicate.not(Grammar.GrammarRuleEntry.named())))
			return false;

		Set<String> queriedNames = new HashSet<>(ruleNames);
		Set<String> names = this.ruleNames().collect(Collectors.toUnmodifiableSet());
		return queriedNames.containsAll(names) && names.containsAll(queriedNames);
	}

	public boolean hasOption(String name)
	{
		return this.ruleNames().anyMatch(n -> Objects.equals(n, name));
	}

	public void reset()
	{
		this.selected = null;
	}

	public void select(String name)
	{
		this.selected = this.rules.stream()
				.filter(Grammar.GrammarRuleEntry.named(name))
				.map(Grammar.GrammarRuleEntry::rule)
				.findAny()
				.orElse(null);
	}

	public void select(Grammar.GrammarRuleEntry rule)
	{
		if(!this.rules.contains(rule))
			throw new IllegalArgumentException("Cannot select rule outside of allowed rules!");
		this.selected = rule.rule();
	}

	static RuleSelector wrap(Consumer<RuleSelectorEnvironment> action)
	{
		return new EnvironmentConfiguringSelector(action);
	}

	public static class EnvironmentConfiguringSelector implements RuleSelector
	{
		private final Consumer<RuleSelectorEnvironment> action;

		public EnvironmentConfiguringSelector(Consumer<RuleSelectorEnvironment> action)
		{
			this.action = action;
		}

		@Nullable
		@Override
		public GrammarRule select(GeneratorContext context, ReadableCellPosition expansionFor, Set<Grammar.GrammarRuleEntry> availableRules)
		{
			RuleSelectorEnvironment delegate = new RuleSelectorEnvironment(context, expansionFor, availableRules);
			this.action.accept(delegate);
			return delegate.selected;
		}
	}
}
