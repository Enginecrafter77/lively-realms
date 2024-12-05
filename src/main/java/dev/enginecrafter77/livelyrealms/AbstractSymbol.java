package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import org.jetbrains.annotations.Nullable;

public class AbstractSymbol implements GrammarSymbol {
	private final String name;

	public AbstractSymbol(String name)
	{
		this.name = name;
	}

	@Override
	public String getName()
	{
		return this.name;
	}

	@Override
	public @Nullable SymbolExpression getExpression()
	{
		return new SymbolExpression() {
			@Override
			public void build(StructureGenerationContext context, ReadableCellPosition position)
			{
				BlockPos pos = context.getCellAnchorBlockPos(position);
				context.level.setBlockAndUpdate(pos, LivelyRealmsMod.BLOCK_NONTERMINAL.get().defaultBlockState());
				BlockEntityNonterminal tile = (BlockEntityNonterminal)context.level.getBlockEntity(pos);
				if(tile == null)
					throw new IllegalStateException();
				tile.symbol = AbstractSymbol.this.name;
			}
		};
	}

	public static AbstractSymbol make(String name)
	{
		return new AbstractSymbol(name);
	}
}
