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
			map.getSymbolMap().setSymbolAt(position, map.getGenerationProfile().grammar().startingSymbol());
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
		Grammar.GrammarRuleEntry selectedRule = rules.getFirst();
		if(rules.size() >= 2)
		{
			RuleSelector selector = grammar.ruleSelector().or(new RandomRuleSelector());
			int ruleIndex = selector.select(map, cell, rules);
			selectedRule = rules.get(ruleIndex);
		}

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
}
