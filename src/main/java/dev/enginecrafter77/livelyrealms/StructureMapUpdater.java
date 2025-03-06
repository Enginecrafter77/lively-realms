package dev.enginecrafter77.livelyrealms;

import dev.enginecrafter77.livelyrealms.generation.CellMutationTask;
import dev.enginecrafter77.livelyrealms.generation.GenerationGridWorldData;
import dev.enginecrafter77.livelyrealms.generation.MinecraftStructureMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.Iterator;

public class StructureMapUpdater {
	@SubscribeEvent
	public static void updateStructureMaps(ServerTickEvent.Post event)
	{
		ServerLevel level = event.getServer().getLevel(Level.OVERWORLD);
		if(level == null)
			return;
		GenerationGridWorldData data = GenerationGridWorldData.get(level);
		for(MinecraftStructureMap map : data.allMaps())
		{
			Iterator<CellMutationTask> itr = map.getTaskTracker().allTasks().iterator();
			while(itr.hasNext())
			{
				CellMutationTask task = itr.next();
				if(task.isDone())
				{
					task.commit();
					itr.remove();
					data.setDirty();
				}
			}
		}
	}
}
