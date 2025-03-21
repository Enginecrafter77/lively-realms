package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.Grammar;
import dev.enginecrafter77.livelyrealms.generation.SingleSubstitutionRule;
import net.minecraft.core.Direction;

import javax.annotation.Nullable;

public class GrammarRuleFacade {
	private final Grammar.GrammarBuilder grammarBuilder;
	private final SingleSubstitutionRule.SingleSubstitutionRuleBuilder builder;
	@Nullable
	private String name;

	public GrammarRuleFacade(Grammar.GrammarBuilder grammarBuilder, String symbol)
	{
		this.grammarBuilder = grammarBuilder;
		this.builder = SingleSubstitutionRule.put(symbol);
		this.name = null;
	}

	public GrammarRuleFacade named(String name)
	{
		this.name = name;
		return this;
	}

	public GrammarRuleFacade at(String symbol)
	{
		this.builder.at(symbol);
		return this;
	}

	public WhereClauseConfiguration where(Direction direction)
	{
		WhereClauseConfiguration def = new WhereClauseConfiguration();
		def.then(direction);
		return def;
	}

	public WhereClauseConfiguration and(Direction direction)
	{
		return this.where(direction);
	}

	void push()
	{
		this.grammarBuilder.withRule(this.name, this.builder.build());
	}

	public class WhereClauseConfiguration
	{
		private final CellPosition offset;
		@Nullable
		private Direction lastDirection;

		public WhereClauseConfiguration()
		{
			this.offset = new CellPosition();
			this.lastDirection = null;
		}

		public WhereClauseConfiguration by(int steps)
		{
			if(this.lastDirection == null)
				throw new IllegalStateException();
			this.offset.add(this.lastDirection.getNormal().multiply(steps-1));
			return this;
		}

		public WhereClauseConfiguration then(Direction direction)
		{
			this.lastDirection = direction;
			this.offset.add(direction.getNormal());
			return this;
		}

		public GrammarRuleFacade is(String symbol)
		{
			GrammarRuleFacade.this.builder.where(this.offset, symbol);
			return GrammarRuleFacade.this;
		}
	}
}
