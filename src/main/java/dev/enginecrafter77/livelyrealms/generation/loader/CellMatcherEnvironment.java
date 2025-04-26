package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.CellMatcher;
import dev.enginecrafter77.livelyrealms.generation.GeneratorContext;
import dev.enginecrafter77.livelyrealms.generation.ReadableCellPosition;

import java.util.function.Consumer;

public class CellMatcherEnvironment {
	public final GeneratorContext context;
	public final ReadableCellPosition cell;

	private boolean matches;

	public CellMatcherEnvironment(GeneratorContext context, ReadableCellPosition cell)
	{
		this.context = context;
		this.cell = cell;
		this.matches = false;
	}

	public void match()
	{
		this.matches = true;
	}

	public void unmatch()
	{
		this.matches = false;
	}

	static CellMatcher wrap(Consumer<CellMatcherEnvironment> action)
	{
		return new CellMatcher() {
			@Override
			public boolean matches(GeneratorContext context, ReadableCellPosition cell)
			{
				CellMatcherEnvironment environment = new CellMatcherEnvironment(context, cell);
				action.accept(environment);
				return environment.matches;
			}
		};
	}
}
