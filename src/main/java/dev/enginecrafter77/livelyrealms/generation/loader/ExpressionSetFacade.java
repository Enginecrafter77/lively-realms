package dev.enginecrafter77.livelyrealms.generation.loader;

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
import java.util.Objects;

public class ExpressionSetFacade {
	private static final LitematicaStructureLoader LOADER = new LitematicaStructureLoader();

	private final SymbolExpressionRegistry.SymbolExpressionRegistryBuilder builder;
	@Nullable
	private ResourceLocation ignoredBlock;

	public ExpressionSetFacade(SymbolExpressionRegistry.SymbolExpressionRegistryBuilder builder)
	{
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
			return LOADER.load(new File(path).toPath());
		}
		catch(IOException exc)
		{
			throw new RuntimeException(exc);
		}
	}

	private boolean filterStructure(Structure structure, Vector3ic position)
	{
		if(this.ignoredBlock == null)
			return true;
		BlockState state = structure.getBlockAt(position);
		return !Objects.equals(state.getBlockHolder().getRegisteredName(), this.ignoredBlock.toString());
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

		public ExpressionFacade using(BuildPlan plan)
		{
			return this.using(() -> plan);
		}

		public ExpressionFacade using(Structure structure)
		{
			return this.using(new StructureExpression(structure));
		}
	}

	private class StructureExpression implements SymbolExpression
	{
		private final Structure struct;
		private final BuildPlan plan;

		public StructureExpression(Structure struct)
		{
			this.struct = struct;
			this.plan = this.createPlan();
		}

		private BuildPlan createPlan()
		{
			Structure filtered = FilteredStructure.filter(this.struct, ExpressionSetFacade.this::filterStructure);
			return StagedBuildPlan.of(new ClearAreaForStructurePlan(filtered), new SimpleStructureBuildPlan(filtered));
		}

		@NotNull
		@Override
		public BuildPlan getBuildPlan()
		{
			return this.plan;
		}
	}
}
