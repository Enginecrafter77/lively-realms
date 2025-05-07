package dev.enginecrafter77.livelyrealms.items;

import dev.enginecrafter77.livelyrealms.generation.GenerationGridWorldData;
import dev.enginecrafter77.livelyrealms.LivelyRealmsMod;
import dev.enginecrafter77.livelyrealms.generation.MinecraftStructureMap;
import dev.enginecrafter77.livelyrealms.generation.*;
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
			map.seed(position);

			return InteractionResult.SUCCESS;
		}

		CellPosition cell = new CellPosition();
		map.getCellLocator().getEnclosingCell(context.getClickedPos(), cell);

		// Use the adjacent cell if player is not crouching
		if(!context.getPlayer().isCrouching())
			cell.add(context.getClickedFace().getNormal());
		map.expand(cell);

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
