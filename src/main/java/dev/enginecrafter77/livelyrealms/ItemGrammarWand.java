package dev.enginecrafter77.livelyrealms;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class ItemGrammarWand extends Item {
	private static StructureGenerationContext ctx = null;
	private static Grammar grammar = null;
	private static SymbolExpressionProvider symbols = null;

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
			return InteractionResult.SUCCESS;
		}

		Grammar grammar = getGrammar();
		MinecraftStructureMap map = new MinecraftStructureMap(grammar, ctx);
		CellPosition cell = new CellPosition();
		ctx.getEnclosingCell(context.getClickedPos(), cell);

		GrammarRule rule = grammar.rules.stream().filter(GrammarRule.applicable(map, cell)).findFirst().orElse(null);
		if(rule == null)
			return InteractionResult.FAIL;
		rule.apply(map, map, cell);

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
				.withCellSize(1)
				.express("minecraft:iron_block", SingleBlockExpression.of(Blocks.IRON_BLOCK))
				.express("minecraft:gold_block", SingleBlockExpression.of(Blocks.GOLD_BLOCK))
				.express("minecraft:copper_block", SingleBlockExpression.of(Blocks.COPPER_BLOCK)) // Hub3-
				.express("minecraft:red_wool", SingleBlockExpression.of(Blocks.RED_WOOL)) // X-
				.express("minecraft:blue_wool", SingleBlockExpression.of(Blocks.BLUE_WOOL)) // X+
				.express("minecraft:yellow_wool", SingleBlockExpression.of(Blocks.YELLOW_WOOL))
				.express("minecraft:cobblestone_wall", SingleBlockExpression.of(Blocks.COBBLESTONE_WALL))
				.build();
	}

	public static Grammar createGrammar()
	{
		return Grammar.builder()
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(1, 0, 0), MinecraftStructureMap.EPSILON)
						.match(CellPosition.of(-1, 0, 0), MinecraftStructureMap.EPSILON)
						.put(CellPosition.of(-1, 0, 0), "minecraft:red_wool")
						.put(CellPosition.of(1, 0, 0), "minecraft:blue_wool")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(0, 0, 1), MinecraftStructureMap.EPSILON)
						.match(CellPosition.of(0, 0, 2), MinecraftStructureMap.EPSILON)
						.put(CellPosition.of(0, 0, 1), "minecraft:yellow_wool")
						.put(CellPosition.of(0, 0, 2), "minecraft:iron_block")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(0, 0, -1), MinecraftStructureMap.EPSILON)
						.match(CellPosition.of(0, 0, -2), MinecraftStructureMap.EPSILON)
						.put(CellPosition.of(0, 0, -1), "minecraft:yellow_wool")
						.put(CellPosition.of(0, 0, -2), "minecraft:iron_block")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(0, 0, 1), "minecraft:yellow_wool")
						.match(CellPosition.of(0, 0, -1), "minecraft:yellow_wool")
						.identity()
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(0, 0, 1), "minecraft:yellow_wool")
						.put(CellPosition.ORIGIN, "minecraft:copper_block")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(0, 0, -1), "minecraft:yellow_wool")
						.put(CellPosition.ORIGIN, "minecraft:gold_block")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:red_wool")
						.match(CellPosition.of(-1, 0, 0), MinecraftStructureMap.EPSILON)
						.put(CellPosition.ORIGIN, "minecraft:red_wool")
						.put(CellPosition.of(-1, 0, 0), "minecraft:red_wool")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:blue_wool")
						.match(CellPosition.of(1, 0, 0), MinecraftStructureMap.EPSILON)
						.put(CellPosition.ORIGIN, "minecraft:blue_wool")
						.put(CellPosition.of(1, 0, 0), "minecraft:blue_wool")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:yellow_wool")
						.match(CellPosition.of(0, 1, 0), MinecraftStructureMap.EPSILON)
						.put(CellPosition.of(0, 1, 0), "minecraft:cobblestone_wall")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:cobblestone_wall")
						.match(CellPosition.of(0, 1, 0), MinecraftStructureMap.EPSILON)
						.put(CellPosition.of(0, 1, 0), "minecraft:cobblestone_wall")
						.build()
				)
				.build();
	}
}
