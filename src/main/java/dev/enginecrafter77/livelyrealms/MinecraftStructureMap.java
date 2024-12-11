package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import javax.annotation.Nullable;

public class MinecraftStructureMap implements GrammarContext, SymbolLattice, SymbolAcceptor {
	public static final String EPSILON = "epsilon";

	public final SymbolExpressionContext expressionContext;
	public final Grammar grammar;

	public MinecraftStructureMap(Grammar grammar, SymbolExpressionContext context)
	{
		this.expressionContext = context;
		this.grammar = grammar;
	}

	@Override
	public SymbolLattice getEnvironment()
	{
		return this;
	}

	@Nullable
	@Override
	public String getSymbolAt(ReadableCellPosition position)
	{
		BlockPos pos = this.expressionContext.getCellAnchorBlockPos(position);
		Block block = this.expressionContext.getLevel().getBlockState(pos).getBlock();
		if(block == LivelyRealmsMod.BLOCK_NONTERMINAL.get())
		{
			BlockEntityNonterminal tile = (BlockEntityNonterminal)this.expressionContext.getLevel().getBlockEntity(pos);
			if(tile == null)
				return null;
			return tile.symbol;
		}
		if(block == Blocks.AIR)
			return EPSILON;
		return BuiltInRegistries.BLOCK.getKey(block).toString();
	}

	@Override
	public void acceptSymbol(ReadableCellPosition cell, String symbol)
	{
		SymbolExpression expression = this.expressionContext.getExpressionProvider().getExpression(symbol);
		if(expression == null)
			return;
		expression.build(this.expressionContext, cell);
	}
}
