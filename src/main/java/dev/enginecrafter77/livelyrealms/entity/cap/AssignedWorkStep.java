package dev.enginecrafter77.livelyrealms.entity.cap;

import dev.enginecrafter77.livelyrealms.generation.*;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpression;
import dev.enginecrafter77.livelyrealms.generation.plan.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.UUID;

public final class AssignedWorkStep implements BuildContextOwner {
	public final WorkStepLocator locator;
	public final BuildContext context;
	public final BuildStep step;

	private AssignedWorkStep(WorkStepLocator locator, BuildContext context, BuildStep step)
	{
		this.locator = locator;
		this.context = context;
		this.step = step;
	}

	public WorkStepLocator locator()
	{
		return this.locator;
	}

	public BuildStep step()
	{
		return this.step;
	}

	@Override
	public BuildContext getContext()
	{
		return this.context;
	}

	@Override
	public String toString()
	{
		return this.locator + "+A";
	}

	public static AssignedWorkStep make(MinecraftStructureMap map, CellMutationTask task, BuildStep step)
	{
		WorkStepLocator locator = new WorkStepLocator(map.getId(), ImmutableCellPosition.copyOf(task.getCellPosition()), task.getToSymbol(), step.index());
		return new AssignedWorkStep(locator, task.getContext(), step);
	}

	public static record WorkStepLocator(UUID mapId, ImmutableCellPosition cell, String symbol, int step) {
		private static final Logger LOGGER = LoggerFactory.getLogger(WorkStepLocator.class);

		@Nullable
		public AssignedWorkStep resolve(ServerLevel level)
		{
			MinecraftStructureMap map = GenerationGridWorldData.get(level).get(this.mapId);
			if(map == null)
			{
				LOGGER.warn("Resolve: map {} could not be found", this.mapId);
				return null;
			}
			GenerationProfile profile = map.getGenerationProfile();
			SymbolExpression expression = profile.expressionProvider().getExpression(this.symbol);
			if(expression == null)
			{
				LOGGER.warn("Resolve: expression for symbol {} could not be found", this.symbol);
				return null;
			}
			BuildContext context = new BuildContext(level, map.getCellLocator().getBlockPositionInsideCell(this.cell, BlockPos.ZERO));
			BuildStep step = expression.getBuildPlan().getStep(this.step);
			return new AssignedWorkStep(this, context, step);
		}

		public CompoundTag serializeNBT(HolderLookup.Provider provider)
		{
			CompoundTag tag = new CompoundTag();
			tag.put("mapId", NbtUtils.createUUID(this.mapId));
			tag.putIntArray("cell", this.cell.toArray());
			tag.putString("symbol", this.symbol);
			tag.putInt("step", this.step);
			return tag;
		}

		public static WorkStepLocator deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt)
		{
			UUID id = NbtUtils.loadUUID(Objects.requireNonNull(nbt.get("mapId")));
			ImmutableCellPosition cell = ImmutableCellPosition.fromArray(nbt.getIntArray("cell"));
			String symbol = nbt.getString("symbol");
			int step = nbt.getInt("step");
			return new WorkStepLocator(id, cell, symbol, step);
		}
	}
}
