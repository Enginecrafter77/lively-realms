package dev.enginecrafter77.livelyrealms;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.block.Blocks;

public class ItemGrammarWand extends Item {
	private static StructureGenerationContext ctx = null;
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

		if(context.getPlayer().isCrouching())
		{
			if(ctx == null)
				ctx = new StructureGenerationContext(context.getLevel(), context.getClickedPos(), 1);
			else
				ctx = null;
			return InteractionResult.SUCCESS;
		}

		if(ctx == null)
			return InteractionResult.FAIL;
		Grammar grammar = getGrammar();
		MinecraftStructureMap map = new MinecraftStructureMap(grammar, ctx);
		CellPosition cell = new CellPosition();
		ctx.getEnclosingCell(context.getClickedPos(), cell);

		GrammarContext grammarContext = new GrammarContext(map);

		GrammarRule rule = grammar.rules.stream().filter(GrammarRule.applicable(grammarContext, cell)).findFirst().orElse(null);
		if(rule == null)
			return InteractionResult.FAIL;
		rule.apply(map, grammarContext, cell);

		return InteractionResult.SUCCESS;
	}

	public static Grammar getGrammar()
	{
		if(grammar == null)
			grammar = createGrammar();
		return grammar;
	}

	public static Grammar createGrammar()
	{
		return Grammar.builder()
				.withEpsilon()
				.withExpressedSymbol("minecraft:iron_block", SingleBlockExpression.of(Blocks.IRON_BLOCK)) // Hub4
				.withExpressedSymbol("minecraft:gold_block", SingleBlockExpression.of(Blocks.GOLD_BLOCK)) // Hub3+
				.withExpressedSymbol("minecraft:copper_block", SingleBlockExpression.of(Blocks.COPPER_BLOCK)) // Hub3-
				.withExpressedSymbol("minecraft:red_wool", SingleBlockExpression.of(Blocks.RED_WOOL)) // X-
				.withExpressedSymbol("minecraft:blue_wool", SingleBlockExpression.of(Blocks.BLUE_WOOL)) // X+
				.withExpressedSymbol("minecraft:yellow_wool", SingleBlockExpression.of(Blocks.YELLOW_WOOL))
				.withExpressedSymbol("minecraft:cobblestone_wall", SingleBlockExpression.of(Blocks.COBBLESTONE_WALL))
				.withAbstractSymbol("start")
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "start")
						.put(CellPosition.ORIGIN, "minecraft:iron_block")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(1, 0, 0), GrammarSymbol.EPSILON)
						.match(CellPosition.of(-1, 0, 0), GrammarSymbol.EPSILON)
						.put(CellPosition.of(-1, 0, 0), "minecraft:red_wool")
						.put(CellPosition.of(1, 0, 0), "minecraft:blue_wool")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(0, 0, 1), GrammarSymbol.EPSILON)
						.match(CellPosition.of(0, 0, 2), GrammarSymbol.EPSILON)
						.put(CellPosition.of(0, 0, 1), "minecraft:yellow_wool")
						.put(CellPosition.of(0, 0, 2), "minecraft:iron_block")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:iron_block")
						.match(CellPosition.of(0, 0, -1), GrammarSymbol.EPSILON)
						.match(CellPosition.of(0, 0, -2), GrammarSymbol.EPSILON)
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
						.match(CellPosition.of(-1, 0, 0), GrammarSymbol.EPSILON)
						.put(CellPosition.ORIGIN, "minecraft:red_wool")
						.put(CellPosition.of(-1, 0, 0), "minecraft:red_wool")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:blue_wool")
						.match(CellPosition.of(1, 0, 0), GrammarSymbol.EPSILON)
						.put(CellPosition.ORIGIN, "minecraft:blue_wool")
						.put(CellPosition.of(1, 0, 0), "minecraft:blue_wool")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:yellow_wool")
						.match(CellPosition.of(0, 1, 0), GrammarSymbol.EPSILON)
						.put(CellPosition.of(0, 1, 0), "minecraft:cobblestone_wall")
						.build()
				)
				.withRule(SubstitutionRule.builder()
						.match(CellPosition.ORIGIN, "minecraft:cobblestone_wall")
						.match(CellPosition.of(0, 1, 0), GrammarSymbol.EPSILON)
						.put(CellPosition.of(0, 1, 0), "minecraft:cobblestone_wall")
						.build()
				)
				.build();
	}
}
