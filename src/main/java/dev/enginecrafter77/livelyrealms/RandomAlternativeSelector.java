package dev.enginecrafter77.livelyrealms;

import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Random;

public class RandomAlternativeSelector<T> implements AlternativeSelector<T> {
	private final Random rng;

	public RandomAlternativeSelector(Random rng)
	{
		this.rng = rng;
	}

	@Override
	public T select(Collection<T> options, GrammarTermResolver resolver, StructureMap map, CellPosition position)
	{
		int count = options.size();
		int index = this.rng.nextInt(count);
		for(T item : options)
		{
			if(index-- == 0)
				return item;
		}
		throw new NoSuchElementException();
	}

	public static <T> RandomAlternativeSelector<T> with(Random rng)
	{
		return new RandomAlternativeSelector<T>(rng);
	}

	public static <T> RandomAlternativeSelector<T> get()
	{
		return new RandomAlternativeSelector<T>(new Random());
	}
}
