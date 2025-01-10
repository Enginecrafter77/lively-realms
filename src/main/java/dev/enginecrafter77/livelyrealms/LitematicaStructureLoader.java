package dev.enginecrafter77.livelyrealms;

import com.google.common.collect.ImmutableMap;
import net.minecraft.nbt.*;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

public class LitematicaStructureLoader {
    private static final String NBT_KEY_MCDATAVERSION = "MinecraftDataVersion";
    private static final String NBT_KEY_REGIONS = "Regions";

    private static final String NBT_KEY_REGION_OFFSET = "Position";
    private static final String NBT_KEY_REGION_SIZE = "Size";
    private static final String NBT_KEY_REGION_PALETTE = "BlockStatePalette";
    private static final String NBT_KEY_REGION_BLOCKMAP = "BlockStates";

    public Structure load(InputStream source) throws IOException
    {
        return this.deserializeStructure(NbtIo.readCompressed(source, NbtAccounter.unlimitedHeap()));
    }

    public Structure load(Path path) throws IOException
    {
        return this.deserializeStructure(NbtIo.readCompressed(path, NbtAccounter.unlimitedHeap()));
    }

    public Structure deserializeStructure(CompoundTag tag)
    {
        int dataVersion = tag.getInt(NBT_KEY_MCDATAVERSION);

        Vector3i offset = new Vector3i();
        CompoundTag regions = tag.getCompound(NBT_KEY_REGIONS);
        CompoundStructure.CompoundStructureBuilder builder = CompoundStructure.builder();
        for(String name : regions.getAllKeys())
        {
            CompoundTag regionTag = this.normalizeRegion(regions.getCompound(name));
            Structure region = deserializeRegion(regionTag, dataVersion);
            deserializeVector(regionTag.getCompound(NBT_KEY_REGION_OFFSET), offset);
            builder.add(region, offset);
        }
        return builder.build();
    }

    private void deserializeVector(CompoundTag tag, Vector3i dest)
    {
        dest.x = tag.getInt("x");
        dest.y = tag.getInt("y");
        dest.z = tag.getInt("z");
    }

    private CompoundTag serializeVector(Vector3ic vector)
    {
        CompoundTag tag = new CompoundTag();
        tag.putInt("x", vector.x());
        tag.putInt("y", vector.y());
        tag.putInt("z", vector.z());
        return tag;
    }

    private BlockStateProvider deserializeBlockState(CompoundTag tag)
    {
        ResourceLocation name = ResourceLocation.parse(tag.getString("Name"));
        ImmutableMap.Builder<String, String> builder = ImmutableMap.builder();
        CompoundTag props = tag.getCompound("Properties");
        for(String key : props.getAllKeys())
        {
            String val = props.getString(key);
            builder.put(key, val);
        }
        return new BlockStateDescriptor(name, builder.build());
    }

    public Structure deserializeRegion(CompoundTag regionTag, int version)
    {
        Vector3i size = new Vector3i();
        this.deserializeVector(regionTag.getCompound(NBT_KEY_REGION_SIZE), size);

        ListTag paletteTag = regionTag.getList(NBT_KEY_REGION_PALETTE, Tag.TAG_COMPOUND);
        List<BlockStateProvider> paletteList = new ArrayList<BlockStateProvider>();

        for(int index = 0; index < paletteTag.size(); ++index)
        {
            BlockStateProvider state = this.deserializeBlockState(paletteTag.getCompound(index));
            paletteList.add(state);
        }

        long[] arrayTag = regionTag.getLongArray(NBT_KEY_REGION_BLOCKMAP);
        LitematicaBitVector vector = LitematicaBitVector.fromArray(paletteList.size(), arrayTag);
        return new LitematicaPalettedStructure(paletteList, vector, size);
    }

    public CompoundTag normalizeRegion(CompoundTag regionTag)
    {
        Vector3i size = new Vector3i();
        Vector3i offset = new Vector3i();
        this.deserializeVector(regionTag.getCompound(NBT_KEY_REGION_SIZE), size);
        this.deserializeVector(regionTag.getCompound(NBT_KEY_REGION_OFFSET), offset);
        this.normalizeRegionSizes(size, offset);
        regionTag.put(NBT_KEY_REGION_SIZE, serializeVector(size));
        regionTag.put(NBT_KEY_REGION_OFFSET, serializeVector(offset));
        return regionTag;
    }

    public void normalizeRegionSizes(Vector3i size, Vector3i offset)
    {
        if(size.x < 0)
        {
            size.x = Math.abs(size.x);
            offset.x -= size.x - 1;
        }

        if(size.y < 0)
        {
            size.y = Math.abs(size.y);
            offset.y -= size.y - 1;
        }

        if(size.z < 0)
        {
            size.z = Math.abs(size.z);
            offset.z -= size.z - 1;
        }
    }
}
