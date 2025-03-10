package dev.enginecrafter77.livelyrealms;

import java.util.Arrays;

public class WeightVector {
	private final double[] weights;

	private WeightVector(double[] weights)
	{
		this.weights = weights;
	}

	public int size()
	{
		return this.weights.length;
	}

	public double weightOf(int index)
	{
		return this.weights[index];
	}

	public int at(double value)
	{
		if(value > 1.0 || value < 0.0)
			throw new IllegalArgumentException();
		int index = 0;
		while(value >= this.weights[index])
		{
			value -= this.weights[index];
			++index;
		}
		return index;
	}

	private static double[] normalizeWeights(double[] weights)
	{
		double sum = Arrays.stream(weights).sum();
		if(sum == 1.0D)
			return weights;
		return Arrays.stream(weights).map(w -> w / sum).toArray();
	}

	public static WeightVector make(double[] weights)
	{
		if(weights.length < 1)
			throw new IllegalArgumentException("Weights array must contain at least 1 element");
		return new WeightVector(normalizeWeights(weights));
	}
}
