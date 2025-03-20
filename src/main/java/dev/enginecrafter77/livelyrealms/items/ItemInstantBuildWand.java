package dev.enginecrafter77.livelyrealms.items;

import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.generation.CellMutationTask;
import dev.enginecrafter77.livelyrealms.generation.CellPosition;
import dev.enginecrafter77.livelyrealms.generation.GenerationGridWorldData;
import dev.enginecrafter77.livelyrealms.generation.MinecraftStructureMap;
import dev.enginecrafter77.livelyrealms.generation.plan.PlanInterpreter;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class ItemInstantBuildWand extends Item {
	public ItemInstantBuildWand(Properties properties)
	{
		super(properties);
	}

	@Override
	public InteractionResult useOn(UseOnContext context)
	{
		if(context.getLevel().isClientSide())
			return InteractionResult.PASS;

		GenerationGridWorldData grid = GenerationGridWorldData.get((ServerLevel)context.getLevel());
		MinecraftStructureMap map = Optional.ofNullable(context.getItemInHand().get(LivelyRealmsMod.DC_ASSOCIATED_GENERATION_MAP)).map(grid::get).orElse(null);
		if(map == null)
		{
			map = grid.allMaps().stream().findAny().orElse(null);
			if(map == null)
				return InteractionResult.FAIL;
			context.getItemInHand().set(LivelyRealmsMod.DC_ASSOCIATED_GENERATION_MAP, map.getId());
		}

		CellPosition cellPosition = new CellPosition();
		map.getCellLocator().getEnclosingCell(context.getClickedPos(), cellPosition);
		CellMutationTask task = map.getTaskTracker().getTask(cellPosition);
		if(task == null)
			return InteractionResult.FAIL;

		PlanInterpreter interpreter = task.getPlanInterpreter();
		while(interpreter.hasNextStep())
			interpreter.nextStep().action().perform(task.getContext());

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
