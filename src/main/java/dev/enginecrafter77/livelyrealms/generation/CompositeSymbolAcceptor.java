package dev.enginecrafter77.livelyrealms.generation;

import com.google.common.collect.ImmutableList;

import java.util.List;

public class CompositeSymbolAcceptor implements SymbolAcceptor {
	private final List<SymbolAcceptor> receivers;

	public CompositeSymbolAcceptor(List<SymbolAcceptor> receivers)
	{
		this.receivers = receivers;
	}

	@Override
	public void acceptSymbol(ReadableCellPosition cell, String symbol)
	{
		for(SymbolAcceptor receiver : this.receivers)
			receiver.acceptSymbol(cell, symbol);
	}

	public static CompositeSymbolAcceptorBuilder builder()
	{
		return new CompositeSymbolAcceptorBuilder();
	}

	public static class CompositeSymbolAcceptorBuilder
	{
		private final ImmutableList.Builder<SymbolAcceptor> builder;

		public CompositeSymbolAcceptorBuilder()
		{
			this.builder = ImmutableList.builder();
		}

		public CompositeSymbolAcceptorBuilder with(SymbolAcceptor acceptor)
		{
			this.builder.add(acceptor);
			return this;
		}

		public CompositeSymbolAcceptor build()
		{
			return new CompositeSymbolAcceptor(this.builder.build());
		}
	}
}
