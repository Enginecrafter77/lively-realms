package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.RuleSelector;
import dev.enginecrafter77.livelyrealms.WeightedRandomSelector;
import groovy.lang.Closure;
import groovy.lang.DelegatesTo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class RuleSelectorFacade {
	private final WeightedRandomSelector.WeightedRandomSelectorBuilder weightedRandomSelectorBuilder;
	private final List<RuleSelector> auxiliarySelectors;

	public RuleSelectorFacade()
	{
		this.weightedRandomSelectorBuilder = WeightedRandomSelector.builder();
		this.auxiliarySelectors = new ArrayList<RuleSelector>();
	}

	public void set(@DelegatesTo(RuleSelectorSetFacade.class) Closure<?> closure)
	{
		WeightedRandomSelector.WeightedRandomSelectorBuilder.SelectorSetBuilder setBuilder = this.weightedRandomSelectorBuilder.beginSet();
		closure.setDelegate(new RuleSelectorSetFacade(setBuilder));
		closure.run();
		setBuilder.endSet();
	}

	public void selector(RuleSelector ruleSelector)
	{
		this.auxiliarySelectors.add(ruleSelector);
	}

	public void selector(@DelegatesTo(RuleSelectorContext.class) Closure<?> closure)
	{
		closure.setResolveStrategy(Closure.DELEGATE_FIRST);
		this.auxiliarySelectors.add(RuleSelectorContext.wrap(ClosureConsumerAction.make(closure)));
	}

	private RuleSelector compoundAuxSelector()
	{
		return (profile, context, expansionFor, availableRules) -> {
			int ret;
			for(RuleSelector selector : this.auxiliarySelectors)
			{
				ret = selector.select(profile, context, expansionFor, availableRules);
				if(ret != -1)
					return ret;
			}
			return -1;
		};
	}

	RuleSelector assemble()
	{
		return this.weightedRandomSelectorBuilder.build().or(this.compoundAuxSelector());
	}
}
