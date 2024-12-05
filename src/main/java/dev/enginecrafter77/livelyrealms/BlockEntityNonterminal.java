package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityNonterminal extends BlockEntity {
	public String symbol;

	public BlockEntityNonterminal(BlockPos pos, BlockState blockState)
	{
		super(LivelyRealmsMod.BLOCK_ENTITY_TYPE_NONTERMINAL.get(), pos, blockState);
		this.symbol = "start";
	}

	@Override
	protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.loadAdditional(tag, registries);
		this.symbol = tag.getString("symbol");
	}

	@Override
	protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries)
	{
		super.saveAdditional(tag, registries);
		tag.putString("symbol", this.symbol);
	}
}
