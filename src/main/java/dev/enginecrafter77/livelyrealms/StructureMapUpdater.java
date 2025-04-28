package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableSet;
import dev.enginecrafter77.livelyrealms.generation.*;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StructureMapUpdater {
	private static final Comparator<ReadableCellPosition> POSITION_COMPARATOR = Comparator.comparingInt(ReadableCellPosition::y).thenComparingInt(ReadableCellPosition::z).thenComparingInt(ReadableCellPosition::x);
	private final Set<ImmutableCellPosition> markedForUpdate;

	public StructureMapUpdater()
	{
		this.markedForUpdate = new TreeSet<ImmutableCellPosition>(POSITION_COMPARATOR);
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
			map.expand(neighbor);
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
					this.markedForUpdate.add(ImmutableCellPosition.copyOf(task.getCellPosition()));
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
