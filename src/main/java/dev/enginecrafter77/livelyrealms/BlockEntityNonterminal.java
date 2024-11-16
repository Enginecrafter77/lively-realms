package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class BlockEntityNonterminal extends BlockEntity {
	public CellPosition cellPosition;
	public GrammarNonterminal nonterminal;
	public MinecraftStructureMap map;
	public boolean initialized;

	public BlockEntityNonterminal(BlockPos pos, BlockState blockState)
	{
		super(LivelyRealmsMod.BLOCK_ENTITY_TYPE_NONTERMINAL.get(), pos, blockState);
		this.initialized = false;
	}

	public void expand()
	{
		if(this.level == null)
			return;
		if(!this.initialized)
		{
			StructureGenerationContext context = new StructureGenerationContext(this.level, this.worldPosition, 1);
			this.map = new MinecraftStructureMap(context);
			this.nonterminal = this.map.registry.getNonterminal("start").orElseThrow();
			this.cellPosition = CellPosition.ORIGIN;
			this.initialized = true;
		}
		this.map.expand(this.cellPosition);
	}
}
