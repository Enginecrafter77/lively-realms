package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class MinecraftStructureMap implements StructureMap {
	public final StructureGenerationContext context;
	public final GrammarRegistry registry;

	public MinecraftStructureMap(StructureGenerationContext context)
	{
		this.context = context;
		this.registry = GrammarRegistry.builder()
				.registerTerminal("grass", SingleBlockTerminal.of(Blocks.GRASS_BLOCK))
				.registerTerminal("ironblock", SingleBlockTerminal.of(Blocks.IRON_BLOCK))
				.registerTerminal("oak-planks", SingleBlockTerminal.of(Blocks.OAK_PLANKS))
				.registerTerminal("jungle-planks", SingleBlockTerminal.of(Blocks.JUNGLE_PLANKS))
				.registerTerminal("acacia-planks", SingleBlockTerminal.of(Blocks.ACACIA_PLANKS))
				.registerNonterminal("planks-seq", MultiNonterminal.builder().add(CellPosition.ORIGIN, "planks").add(CellPosition.of(1, 0, 0), "planks-seq-or-epsilon").build())
				.registerNonterminal("planks-seq-or-epsilon", AlternativeNonterminal.builder().or("planks-seq").orEpsilon().build())
				.registerNonterminal("planks", AlternativeNonterminal.builder().either("oak-planks", "jungle-planks", "acacia-planks").build())
				.alias("start", "planks-seq")
				.build();
	}

	@Override
	public void addTerminal(CellPosition cell, GrammarTerminal terminal)
	{
		terminal.build(this.context, cell);
	}

	@Override
	public void addNonterminal(CellPosition cell, GrammarNonterminal nonterminal)
	{
		BlockPos pos = this.context.getAnchorBlockPos(cell);
		this.context.level.setBlockAndUpdate(pos, LivelyRealmsMod.BLOCK_NONTERMINAL.get().defaultBlockState());
		BlockEntityNonterminal tile = (BlockEntityNonterminal)this.context.level.getBlockEntity(pos);
		if(tile == null)
			throw new IllegalStateException();
		tile.cellPosition = cell;
		tile.nonterminal = nonterminal;
		tile.map = this;
		tile.initialized = true;
	}

	@Override
	public Optional<GrammarTerminal> getTerminalAt(CellPosition position)
	{
		//return Optional.ofNullable(this.cells.get(position)).flatMap(CellMemberHolder::getTerminal);
		return Optional.empty();
	}

	@Override
	public Optional<GrammarNonterminal> getNonterminalAt(CellPosition position)
	{
		BlockPos pos = this.context.getAnchorBlockPos(position);
		BlockState state = this.context.level.getBlockState(pos);
		if(state.getBlock() != LivelyRealmsMod.BLOCK_NONTERMINAL.get())
			return Optional.empty();
		BlockEntityNonterminal nonterminal = (BlockEntityNonterminal)this.context.level.getBlockEntity(pos);
		if(nonterminal == null)
			return Optional.empty();
		return Optional.of(nonterminal.nonterminal);
	}

	public Optional<GrammarTerminal> expand(CellPosition position)
	{
		Optional<GrammarNonterminal> nonterminal = this.getNonterminalAt(position);
		while(nonterminal.isPresent())
		{
			this.context.level.setBlockAndUpdate(this.context.getAnchorBlockPos(position), Blocks.AIR.defaultBlockState());
			nonterminal.get().expand(this.registry, this, position);
			nonterminal = this.getNonterminalAt(position);
		}
		return this.getTerminalAt(position);
	}
}
