package dev.enginecrafter77.livelyrealms.generation.loader;

import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpression;
import dev.enginecrafter77.livelyrealms.generation.expression.SymbolExpressionRegistry;
import dev.enginecrafter77.livelyrealms.generation.plan.BuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.ClearAreaForStructurePlan;
import dev.enginecrafter77.livelyrealms.generation.plan.SimpleStructureBuildPlan;
import dev.enginecrafter77.livelyrealms.generation.plan.StagedBuildPlan;
import dev.enginecrafter77.livelyrealms.structure.LitematicaStructureLoader;
import dev.enginecrafter77.livelyrealms.structure.Structure;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;

public class ExpressionSetFacade {
	private static final LitematicaStructureLoader LOADER = new LitematicaStructureLoader();

	private final SymbolExpressionRegistry.SymbolExpressionRegistryBuilder builder;
	@Nullable
	private ResourceLocation ignoredBlock;

	public ExpressionSetFacade(SymbolExpressionRegistry.SymbolExpressionRegistryBuilder builder)
	{
		this.builder = builder;
		this.ignoredBlock = ResourceLocation.fromNamespaceAndPath("minecraft", "bedrock");
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

	public SymbolExpression structure(String path)
	{
		try
		{
			Structure structure = LOADER.load(new File(path).toPath());
			return new StructureExpression(structure);
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

		public void using(SymbolExpression expression)
		{
			ExpressionSetFacade.this.builder.express(this.symbol, expression);
		}

		public void using(BuildPlan plan)
		{
			this.using(() -> plan);
		}

		public void using(Structure structure)
		{
			this.using(new StructureExpression(structure));
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
			return StagedBuildPlan.of(new ClearAreaForStructurePlan(this.struct, this::isExplicitBlock), new SimpleStructureBuildPlan(this.struct, this::isExplicitBlock));
		}

		@NotNull
		@Override
		public BuildPlan getBuildPlan()
		{
			return this.plan;
		}

		private boolean isExplicitBlock(BlockState state)
		{
			if(ignoredBlock == null)
				return true;
			return !state.getBlockHolder().getRegisteredName().equals(ignoredBlock.toString());
		}
	}
}
