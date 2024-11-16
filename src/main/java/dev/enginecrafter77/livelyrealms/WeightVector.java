package dev.enginecrafter77.livelyrealms;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;

public class WeightVector<T> {
	private final List<WeightVectorEntry<T>> entries;

	private WeightVector(List<WeightVectorEntry<T>> entries)
	{
		this.entries = entries;
	}

	public WeightVectorEntry<T> at(double num)
	{
		Iterator<WeightVectorEntry<T>> iterator = this.entries.iterator();
		WeightVectorEntry<T> entry = null;
		while(num >= 0)
		{
			entry = iterator.next();
			num -= entry.weight;
		}
		if(entry == null)
			throw new IllegalStateException();
		return entry;
	}

	public static <T> WeightVectorBuilder<T> builder()
	{
		return new WeightVectorBuilder<T>();
	}

	public static class WeightVectorEntry<T>
	{
		private final T item;
		private final double weight;

		public WeightVectorEntry(T item, double weight)
		{
			this.item = item;
			this.weight = weight;
		}

		public WeightVectorEntry<T> normalized(double sum)
		{
			return new WeightVectorEntry<T>(this.item, this.weight / sum);
		}

		public double getWeight()
		{
			return this.weight;
		}

		public T getItem()
		{
			return this.item;
		}
	}

	public static class WeightVectorBuilder<T>
	{
		private final List<WeightVectorEntry<T>> entries;

		public WeightVectorBuilder()
		{
			this.entries = new ArrayList<WeightVectorEntry<T>>();
		}

		public void add(T item, double weight)
		{
			this.entries.add(new WeightVectorEntry<T>(item, weight));
		}

		public WeightVector<T> build()
		{
			double sum = this.entries.parallelStream().mapToDouble(WeightVectorEntry::getWeight).sum();
			Function<WeightVectorEntry<T>, WeightVectorEntry<T>> normalizer = (WeightVectorEntry<T> entry) -> entry.normalized(sum);
			return new WeightVector<T>(this.entries.stream().map(normalizer).toList());
		}
	}
}
