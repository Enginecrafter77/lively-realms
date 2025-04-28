package dev.enginecrafter77.livelyrealms.structure;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

public class BlockStateDescriptor implements BlockStateProvider {
    private final ResourceLocation name;
    private final Map<String, String> properties;

    @Nullable
    private BlockState memoizedState;

    public BlockStateDescriptor(ResourceLocation name, Map<String, String> properties)
    {
        this.name = name;
        this.properties = properties;
        this.memoizedState = null;
    }

    private BlockState compileState()
    {
        Block block = BuiltInRegistries.BLOCK.get(this.name);
        BlockState state = block.defaultBlockState();
        Collection<Property<?>> props = state.getProperties();
        for(Property<?> prop : props)
        {
            String value = this.properties.get(prop.getName());
            state = setProperty(state, prop, value);
        }
        return state;
    }

    @Override
    public BlockState getBlockState()
    {
        if(this.memoizedState == null)
            this.memoizedState = this.compileState();
        return this.memoizedState;
    }

    public void invalidate()
    {
        this.memoizedState = null;
    }

    private static <T extends Comparable<T>> BlockState setProperty(BlockState state, Property<T> prop, String value)
    {
        T val = prop.getValue(value).orElseThrow();
        return state.setValue(prop, val);
    }
}
