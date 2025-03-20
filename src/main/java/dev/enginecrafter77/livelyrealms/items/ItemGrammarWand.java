package dev.enginecrafter77.livelyrealms.items;

import dev.enginecrafter77.livelyrealms.RandomRuleSelector;
import dev.enginecrafter77.livelyrealms.RuleSelector;
import dev.enginecrafter77.livelyrealms.WeightedRandomSelector;
import dev.enginecrafter77.livelyrealms.generation.GenerationGridWorldData;
import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.generation.MinecraftStructureMap;
import dev.enginecrafter77.livelyrealms.generation.*;
import dev.enginecrafter77.livelyrealms.generation.expression.MultiblockExpression;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionRegistry;
import dev.enginecrafter77.livelyrealms.structure.LitematicaStructureLoader;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemGrammarWand extends Item {
	public static final String EPSILON = MinecraftStructureMap.EPSILON;

	public ItemGrammarWand(Properties properties)
	{
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if(context.getLevel().isClientSide)
			return InteractionResult.PASS;

		GenerationGridWorldData grid = GenerationGridWorldData.get((ServerLevel)context.getLevel());
		MinecraftStructureMap map = Optional.ofNullable(context.getItemInHand().get(LivelyRealmsMod.DC_ASSOCIATED_GENERATION_MAP)).map(grid::get).orElse(null);
		if(map == null)
		{
			map = grid.createMap(context.getClickedPos(), LivelyRealmsMod.SAMPLE_PROFILE);
			context.getItemInHand().set(LivelyRealmsMod.DC_ASSOCIATED_GENERATION_MAP, map.getId());

			CellPosition position = new CellPosition();
			map.getCellLocator().getEnclosingCell(context.getClickedPos(), position);
			map.getSymbolMap().setSymbolAt(position, map.getGenerationProfile().grammar().getDefaultStartingSymbol());
		}

		Grammar grammar = map.getGenerationProfile().grammar();
		CellPosition cell = new CellPosition();
		map.getCellLocator().getEnclosingCell(context.getClickedPos(), cell);

		// Use the adjacent cell if player is not crouching
		if(!context.getPlayer().isCrouching())
			cell.add(context.getClickedFace().getNormal());

		List<Grammar.GrammarRuleEntry> rules = grammar.findApplicableRules(map, cell).collect(Collectors.toList());
		if(rules.isEmpty())
			return InteractionResult.FAIL;
		RuleSelector selector = WeightedRandomSelector.builder()
				.beginSet()
				.option("hallX2-east", 10)
				.option("hallX-hall4-east", 1)
				.endSet()
				.beginSet()
				.option("hallX2-west", 10)
				.option("hallX-hall4-west", 1)
				.endSet()
				.build()
				.or(new RandomRuleSelector());
		int ruleIndex = selector.select(map.getGenerationProfile(), map, cell, rules);
		Grammar.GrammarRuleEntry selectedRule = rules.get(ruleIndex);

		SymbolAcceptor acceptor = map.getTaskTracker().mutationAcceptor();
		selectedRule.rule().apply(acceptor, map, cell);
		grid.setDirty();

		return InteractionResult.SUCCESS;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag)
	{
		super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
		UUID map = stack.get(LivelyRealmsMod.DC_ASSOCIATED_GENERATION_MAP);
		tooltipComponents.add(Component.literal(map == null ? "No map" : String.format("Map: %s", map)));
	}

	public static void configureGrammar(Grammar.GrammarBuilder builder, SymbolExpressionRegistry.SymbolExpressionRegistryBuilder expressionRegistryBuilder)
	{
		try
		{
			LitematicaStructureLoader loader = new LitematicaStructureLoader();
			Structure hub4 = loader.load(new File("lr-hub4.litematic").toPath());
			Structure hallX = loader.load(new File("lr-hallx.litematic").toPath());
			Structure hallZ = loader.load(new File("lr-hallz.litematic").toPath());
			expressionRegistryBuilder
					.withCellSize(8)
					.express("hall4", MultiblockExpression.of(hub4))
					.express("hallZ", MultiblockExpression.of(hallZ))
					.express("hallX", MultiblockExpression.of(hallX));
		}
		catch(IOException exc)
		{
			throw new RuntimeException(exc);
		}

		builder
			.withRule("init", SingleSubstitutionRule.put("hall4").at(Grammar.DEFAULT_STARTING_SYMBOL).build())
			.withRule("hall4-hallZ-north", SingleSubstitutionRule.put("hallZ").at(EPSILON).where(Direction.SOUTH, "hall4").where(Direction.NORTH, EPSILON).where(Direction.NORTH, EPSILON, 2).build())
			.withRule("hall4-hallZ-south", SingleSubstitutionRule.put("hallZ").at(EPSILON).where(Direction.NORTH, "hall4").where(Direction.SOUTH, EPSILON).where(Direction.SOUTH, EPSILON, 2).build())
			.withRule("hallZ-hall4-north", SingleSubstitutionRule.put("hall4").at(EPSILON).where(Direction.SOUTH, "hallZ").where(Direction.NORTH, EPSILON).build())
			.withRule("hallZ-hall4-south", SingleSubstitutionRule.put("hall4").at(EPSILON).where(Direction.NORTH, "hallZ").where(Direction.SOUTH, EPSILON).build())
			.withRule("hall4-hallX-east", SingleSubstitutionRule.put("hallX").at(EPSILON).where(Direction.WEST, "hall4").build())
			.withRule("hall4-hallX-west", SingleSubstitutionRule.put("hallX").at(EPSILON).where(Direction.EAST, "hall4").build())
			.withRule("hallX2-east", SingleSubstitutionRule.put("hallX").at(EPSILON).where(Direction.WEST, "hallX").build())
			.withRule("hallX2-west", SingleSubstitutionRule.put("hallX").at(EPSILON).where(Direction.EAST, "hallX").build())
			.withRule("hallX-hall4-west", SingleSubstitutionRule.put("hall4").at(EPSILON).where(Direction.EAST, "hallX").where(Direction.WEST, EPSILON).build())
			.withRule("hallX-hall4-east", SingleSubstitutionRule.put("hall4").at(EPSILON).where(Direction.WEST, "hallX").where(Direction.EAST, EPSILON).build());
	}
}
