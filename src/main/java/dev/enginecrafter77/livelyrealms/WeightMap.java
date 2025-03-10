package dev.enginecrafter77.livelyrealms;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

public class WeightMap<T> {
	private final List<T> entries;
	private final WeightVector vector;

	private WeightMap(WeightVector vector, List<T> entries)
	{
		this.vector = vector;
		this.entries = entries;
	}

	public int size()
	{
		return this.vector.size();
	}

	public T at(double num)
	{
		return this.entries.get(this.vector.at(num));
	}

	@Nullable
	public WeightMap<T> filterOptions(Predicate<T> predicate)
	{
		WeightMapBuilder<T> builder = WeightMap.builder();
		for(int index = 0; index < this.vector.size(); ++index)
		{
			T item = this.entries.get(index);
			if(predicate.test(item))
				builder.entry(item, this.vector.weightOf(index));
		}
		if(!builder.isValid())
			return null;
		return builder.build();
	}

	public static <T> WeightMapBuilder<T> builder()
	{
		return new WeightMapBuilder<T>();
	}

	private static record WeightMapBuilderEntry<T>(T item, double weight) {}

	public static class WeightMapBuilder<T>
	{
		private final List<WeightMapBuilderEntry<T>> entries;

		public WeightMapBuilder()
		{
			this.entries = new ArrayList<WeightMapBuilderEntry<T>>();
		}

		public WeightMapBuilder<T> entry(T item, double weight)
		{
			this.entries.add(new WeightMapBuilderEntry<T>(item, weight));
			return this;
		}

		public boolean isValid()
		{
			return !this.entries.isEmpty();
		}

		public WeightMap<T> build()
		{
			double[] weights = this.entries.stream().mapToDouble(WeightMapBuilderEntry::weight).toArray();
			WeightVector vector = WeightVector.make(weights);
			List<T> items = this.entries.stream().map(WeightMapBuilderEntry::item).toList();
			return new WeightMap<T>(vector, items);
		}
	}
}
