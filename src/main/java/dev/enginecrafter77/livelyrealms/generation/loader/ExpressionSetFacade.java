package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.expression.MultiblockExpression;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpression;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionRegistry;
import dev.enginecrafter77.livelyrealms.generation.plan.BuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.ClearAreaForStructurePlan;
import dev.enginecrafter77.livelyrealms.generation.plan.SimpleStructureBuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.StagedBuildPlan;
import dev.enginecrafter77.livelyrealms.structure.FilteredStructure;
import dev.enginecrafter77.livelyrealms.structure.LitematicaStructureLoader;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.joml.Vector3ic;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;

public class ExpressionSetFacade {
	private static final LitematicaStructureLoader LOADER = new LitematicaStructureLoader();

	private final SymbolExpressionRegistry.SymbolExpressionRegistryBuilder builder;
	private final Path directoryIn;

	@Nullable
	private ResourceLocation ignoredBlock;

	public ExpressionSetFacade(Path directoryIn, SymbolExpressionRegistry.SymbolExpressionRegistryBuilder builder)
	{
		this.directoryIn = directoryIn;
		this.builder = builder;
		this.ignoredBlock = null;
	}

	public void cellSize(int cellSize)
	{
		this.builder.withCellSize(cellSize);
	}

	public void ignoreBlock(String name)
	{
		this.ignoredBlock = ResourceLocation.parse(name);
	}

	public ExpressionFacade express(String symbol)
	{
		return new ExpressionFacade(symbol);
	}

	public Structure structure(String path)
	{
		try
		{
			Path structureFile = this.directoryIn.resolve(path);
			Structure structure = LOADER.load(structureFile);
			if(this.ignoredBlock != null)
				structure = FilteredStructure.filter(structure, new IgnoreBlockStructureFilter(this.ignoredBlock));
			return structure;
		}
		catch(IOException exc)
		{
			throw new RuntimeException(exc);
		}
	}

	public class ExpressionFacade
	{
		private final String symbol;

		public ExpressionFacade(String symbol)
		{
			this.symbol = symbol;
		}

		public ExpressionFacade using(SymbolExpression expression)
		{
			ExpressionSetFacade.this.builder.express(this.symbol, expression);
			return this;
		}

		public ExpressionFacade using(Structure structure)
		{
			return this.using(MultiblockExpression.of(structure));
		}
	}

	private record IgnoreBlockStructureFilter(ResourceLocation ignoreBlock) implements FilteredStructure.StructureFilter
	{
		@Override
		public boolean blockMatches(Structure structure, Vector3ic position)
		{
			BlockState state = structure.getBlockAt(position);
			return !Objects.equals(state.getBlockHolder().getRegisteredName(), this.ignoreBlock.toString());
		}
	}
}
