package dev.enginecrafter77.livelyrealms.generation.loader;

import groovy.lang.Closure;

import java.util.function.Consumer;

public class ClosureConsumerAction<T> implements Consumer<T> {
	private final Closure<?> closure;
	private final Object owner;
	private final Object thisObject;
	private final int resolveStrategy;

	private ClosureConsumerAction(Closure<?> closure, Object owner, Object thisObject, int resolveStrategy)
	{
		this.closure = closure;
		this.owner = owner;
		this.thisObject = thisObject;
		this.resolveStrategy = resolveStrategy;
	}

	public Closure<?> rehydrate(T delegate)
	{
		Closure<?> cls = this.closure.rehydrate(delegate, this.owner, this.thisObject);
		cls.setResolveStrategy(this.resolveStrategy);
		return cls;
	}

	@Override
	public void accept(T t)
	{
		this.rehydrate(t).run();
	}

	public static <T> ClosureConsumerAction<T> make(Closure<?> closure)
	{
		return new ClosureConsumerAction<T>(closure.dehydrate(), closure.getOwner(), closure.getThisObject(), closure.getResolveStrategy());
	}
}
