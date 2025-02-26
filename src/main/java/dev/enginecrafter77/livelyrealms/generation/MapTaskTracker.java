package dev.enginecrafter77.livelyrealms.generation;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.neoforged.neoforge.common.util.INBTSerializable;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.util.*;

public class MapTaskTracker implements INBTSerializable<ListTag> {
	private final List<CellMutationTask> activeTasks;
	private final CellMutationContext context;

	public MapTaskTracker(CellMutationContext context)
	{
		this.activeTasks = new ArrayList<CellMutationTask>();
		this.context = context;
	}

	public SymbolAcceptor mutationAcceptor()
	{
		return new SymbolAcceptor() {
			@Override
			public void acceptSymbol(ReadableCellPosition cell, String symbol)
			{
				CellMutationTask task = CellMutationTask.create(MapTaskTracker.this.context, cell, symbol);
				MapTaskTracker.this.activeTasks.add(task);
			}
		};
	}

	public List<CellMutationTask> getActiveTasks()
	{
		return this.activeTasks;
	}

	@UnknownNullability
	@Override
	public ListTag serializeNBT(HolderLookup.Provider provider)
	{
		ListTag tag = new ListTag();
		for(CellMutationTask task : this.activeTasks)
			tag.add(task.serializeNBT(provider));
		return tag;
	}

	@Override
	public void deserializeNBT(HolderLookup.Provider provider, @Nullable ListTag tags)
	{
		if(tags == null)
			return;
		for(int index = 0; index < tags.size(); ++index)
		{
			CompoundTag tag = tags.getCompound(index);
			CellMutationTask task = new CellMutationTask(this.context);
			task.deserializeNBT(provider, tag);
			this.activeTasks.add(task);
		}
	}
}
