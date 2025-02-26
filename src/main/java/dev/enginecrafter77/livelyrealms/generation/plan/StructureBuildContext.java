package dev.enginecrafter77.livelyrealms.generation.plan;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public record StructureBuildContext(Level level, BlockPos anchor) {
}
