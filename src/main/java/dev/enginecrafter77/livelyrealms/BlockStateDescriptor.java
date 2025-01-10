package dev.enginecrafter77.livelyrealms;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;

import java.util.Collection;
import java.util.Map;

public class BlockStateDescriptor implements BlockStateProvider {
    private final ResourceLocation name;
    private final Map<String, String> properties;

    public BlockStateDescriptor(ResourceLocation name, Map<String, String> properties)
    {
        this.name = name;
        this.properties = properties;
    }

    @Override
    public BlockState getBlockState()
    {
        Block block = BuiltInRegistries.BLOCK.get(this.name);
        BlockState state = block.defaultBlockState();
        Collection<Property<?>> props = state.getProperties();
        for(Property<?> prop : props)
        {
            String value = this.properties.get(prop.getName());
            state = this.setProperty(state, prop, value);
        }
        return state;
    }

    private <T extends Comparable<T>> BlockState setProperty(BlockState state, Property<T> prop, String value)
    {
        T val = prop.getValue(value).orElseThrow();
        return state.setValue(prop, val);
    }
}
