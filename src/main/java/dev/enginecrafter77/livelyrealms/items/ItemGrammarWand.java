package dev.enginecrafter77.livelyrealms.items;

import dev.enginecrafter77.livelyrealms.generation.GenerationGridWorldData;
import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.generation.MinecraftStructureMap;
import dev.enginecrafter77.livelyrealms.generation.*;
import dev.enginecrafter77.livelyrealms.generation.expression.MultiblockExpression;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionRegistry;
import dev.enginecrafter77.livelyrealms.structure.LitematicaStructureLoader;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.context.UseOnContext;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

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
			map = grid.createMap(context.getClickedPos(), LivelyRealmsMod.SAMPLE_PROFILE.getId());
			context.getItemInHand().set(LivelyRealmsMod.DC_ASSOCIATED_GENERATION_MAP, map.getId());

			CellPosition position = new CellPosition();
			map.getGenerationContext().getEnclosingCell(context.getClickedPos(), position);
			map.getSymbolMap().setSymbolAt(position, "start");
		}

		Grammar grammar = map.getGenerationProfile().grammar();
		CellPosition cell = new CellPosition();
		map.getGenerationContext().getEnclosingCell(context.getClickedPos(), cell);

		GrammarRule rule = grammar.rules.stream().filter(GrammarRule.applicable(map, cell)).findFirst().orElse(null);
		if(rule == null)
			return InteractionResult.FAIL;
		SymbolAcceptor acceptor = map.getTaskTracker().mutationAcceptor();
		rule.apply(acceptor, map, cell);
		grid.setDirty();

		return InteractionResult.SUCCESS;
	}

	public static void configureGrammar(Grammar.GrammarBuilder builder, SymbolExpressionRegistry.SymbolExpressionRegistryBuilder expressionRegistryBuilder)
	{
		try
		{
			LitematicaStructureLoader loader = new LitematicaStructureLoader();
			Structure hub4 = loader.load(new File("lr-hub4.litematic").toPath());
			Structure hallX = loader.load(new File("lr-hallx.litematic").toPath());
			Structure hallZ = loader.load(new File("lr-hallz.litematic").toPath());
			expressionRegistryBuilder.withCellSize(8).express("hall4", MultiblockExpression.of(hub4)).express("hallZ", MultiblockExpression.of(hallZ)).express("hallX", MultiblockExpression.of(hallX));
		}
		catch(IOException exc)
		{
			throw new RuntimeException(exc);
		}

		builder.withRule(SubstitutionRule.builder()
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
		);
	}
}
