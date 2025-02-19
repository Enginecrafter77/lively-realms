package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.GrammarContext;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomAlternativeSelector implements AlternativeSelector {
	private final Random rng;

	public RandomAlternativeSelector(Random rng)
	{
		this.rng = rng;
	}

	@Override
	public String select(Collection<String> options, GrammarContext context, CellPosition position)
	{
		int count = options.size();
		int index = this.rng.nextInt(count);
		for(String item : options)
		{
			if(index-- == 0)
				return item;
		}
		throw new NoSuchElementException();
	}

	public static RandomAlternativeSelector with(Random rng)
	{
		return new RandomAlternativeSelector(rng);
	}

	public static RandomAlternativeSelector get()
	{
		return new RandomAlternativeSelector(new Random());
	}
}
