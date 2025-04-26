package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableSet;
import dev.enginecrafter77.livelyrealms.generation.*;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureMapUpdater {
	private final List<ReadableCellPosition> markedForUpdate;

	public StructureMapUpdater()
	{
		this.markedForUpdate = new ArrayList<ReadableCellPosition>();
	}

	public void expand(MinecraftStructureMap map, ReadableCellPosition cell)
	{
		Grammar grammar = map.getGenerationProfile().grammar();
		List<Grammar.GrammarRuleEntry> rules = grammar.findApplicableRules(map, cell).collect(Collectors.toList());
		if(rules.isEmpty())
			return;
		Grammar.GrammarRuleEntry selectedRule = rules.getFirst();
		if(rules.size() >= 2)
		{
			RuleSelector selector = grammar.ruleSelector().or(new RandomRuleSelector());
			int ruleIndex = selector.select(map, cell, rules);
			if(ruleIndex == -1)
				return;
			selectedRule = rules.get(ruleIndex);
		}

		SymbolAcceptor acceptor = map.getTaskTracker().mutationAcceptor();
		selectedRule.rule().apply(acceptor, map, cell);
		map.markDirty();
	}

	public Collection<ReadableCellPosition> getNeighborhood(ReadableCellPosition cell)
	{
		ImmutableSet.Builder<ReadableCellPosition> builder = ImmutableSet.builder();
		CellPosition neighborPos = new CellPosition();
		for(Direction dir : Direction.values())
		{
			neighborPos.set(cell);
			neighborPos.add(dir.getNormal());
			builder.add(ImmutableCellPosition.copyOf(neighborPos));
		}
		return builder.build();
	}

	public void expandNeighbors(MinecraftStructureMap map, ReadableCellPosition cell)
	{
		for(ReadableCellPosition neighbor : this.getNeighborhood(cell))
			this.expand(map, neighbor);
	}

	@SubscribeEvent
	public void updateStructureMaps(ServerTickEvent.Post event)
	{
		ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
		if(level == null)
			return;
		GenerationGridWorldData data = GenerationGridWorldData.get(level);
		for(MinecraftStructureMap map : data.allMaps())
		{
			Iterator<CellMutationTask> itr = map.getTaskTracker().allTasks().iterator();
			this.markedForUpdate.clear();
			while(itr.hasNext())
			{
				CellMutationTask task = itr.next();
				if(task.isDone() && task.validate())
				{
					task.commit();
					itr.remove();
					this.markedForUpdate.add(task.getCellPosition());
					data.setDirty();
				}
			}

			for(ReadableCellPosition cell : this.markedForUpdate)
			{
				this.expandNeighbors(map, cell);
			}
		}
	}
}
