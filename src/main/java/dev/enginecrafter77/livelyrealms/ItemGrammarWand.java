package dev.enginecrafter77.livelyrealms;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import java.io.File;

public class ItemGrammarWand extends Item {
	private static final String EPSILON = VirtualStructureMap.EPSILON;
	private static final VirtualStructureMap map = new VirtualStructureMap();

	private static StructureGenerationContext ctx = null;
	private static SymbolExpressionProvider symbols = null;
	private static Grammar grammar = null;

	public ItemGrammarWand(Properties properties)
	{
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if(context.getLevel().isClientSide)
			return InteractionResult.PASS;

		if(ctx == null)
		{
			ctx = new StructureGenerationContext(context.getLevel(), context.getClickedPos(), getSymbols());
			CellPosition position = new CellPosition();
			ctx.getEnclosingCell(context.getClickedPos(), position);
			map.acceptSymbol(position, "start");
		}

		Grammar grammar = getGrammar();
		CellPosition cell = new CellPosition();
		ctx.getEnclosingCell(context.getClickedPos(), cell);

		GrammarRule rule = grammar.rules.stream().filter(GrammarRule.applicable(map, cell)).findFirst().orElse(null);
		if(rule == null)
			return InteractionResult.FAIL;
		SymbolExpressingAcceptorAdapter adapter = new SymbolExpressingAcceptorAdapter(map, ctx);
		rule.apply(adapter, map, cell);

		return InteractionResult.SUCCESS;
	}

	public static Grammar getGrammar()
	{
		if(grammar == null)
			grammar = createGrammar();
		return grammar;
	}

	public static SymbolExpressionProvider getSymbols()
	{
		if(symbols == null)
			symbols = createSymbols();
		return symbols;
	}

	public static SymbolExpressionProvider createSymbols()
	{
		return SymbolExpressionRegistry.builder()
				.withCellSize(8)
				.express("hall4", MultiblockExpression.of(PalettedStructure.fromJson(new File("hall-hub4.json"))))
				.express("hallZ", MultiblockExpression.of(PalettedStructure.fromJson(new File("hall-straight-z.json"))))
				.express("hallX", MultiblockExpression.of(PalettedStructure.fromJson(new File("hall-straight-x.json"))))
				.build();
	}

	public static Grammar createGrammar()
	{
		return Grammar.builder()
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "start")
						.put(CellPosition.ORIGIN, "hall4")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "hall4")
						.match(CellPosition.of(1, 0, 0), EPSILON)
						.match(CellPosition.of(-1, 0, 0), EPSILON)
						.put(CellPosition.of(-1, 0, 0), "hallX")
						.put(CellPosition.of(1, 0, 0), "hallX")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "hall4")
						.match(CellPosition.of(0, 0, 1), EPSILON)
						.match(CellPosition.of(0, 0, 2), EPSILON)
						.put(CellPosition.of(0, 0, 1), "hallZ")
						.put(CellPosition.of(0, 0, 2), "hall4")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "hall4")
						.match(CellPosition.of(0, 0, -1), EPSILON)
						.match(CellPosition.of(0, 0, -2), EPSILON)
						.put(CellPosition.of(0, 0, -1), "hallZ")
						.put(CellPosition.of(0, 0, -2), "hall4")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "hallX")
						.match(CellPosition.of(-1, 0, 0), EPSILON)
						.put(CellPosition.of(-1, 0, 0), "hallX")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "hallX")
						.match(CellPosition.of(1, 0, 0), EPSILON)
						.put(CellPosition.of(1, 0, 0), "hallX")
						.build()
				)
				.build();
	}
}
