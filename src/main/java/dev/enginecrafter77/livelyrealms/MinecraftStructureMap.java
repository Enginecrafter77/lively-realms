package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

public class MinecraftStructureMap implements StructureMap, SymbolLattice, SymbolAcceptor {
	public final StructureGenerationContext context;
	public final Grammar grammar;

	public MinecraftStructureMap(Grammar grammar, StructureGenerationContext context)
	{
		this.context = context;
		this.grammar = grammar;
	}

	@Override
	public void putSymbol(ReadableCellPosition position, GrammarSymbol symbol)
	{
		SymbolExpression expression = symbol.getExpression();
		if(expression == null)
			return;
		expression.build(this.context, position);
	}

	@Nullable
	@Override
	public GrammarSymbol getSymbol(ReadableCellPosition position)
	{
		BlockPos pos = this.context.getCellAnchorBlockPos(position);
		Block block = this.context.level.getBlockState(pos).getBlock();
		if(block == LivelyRealmsMod.BLOCK_NONTERMINAL.get())
		{
			BlockEntityNonterminal tile = (BlockEntityNonterminal)this.context.level.getBlockEntity(pos);
			if(tile == null)
				return null;
			return this.grammar.symbols.get(tile.symbol);
		}
		if(block == Blocks.AIR)
			return EpsilonSymbol.INSTANCE;
		return this.grammar.symbols.get(BuiltInRegistries.BLOCK.getKey(block).toString());
	}

	@Override
	public void acceptSymbol(ReadableCellPosition cell, String symbol)
	{
		this.putSymbol(cell, this.grammar.symbols.getOrDefault(symbol, EpsilonSymbol.INSTANCE));
	}
}
