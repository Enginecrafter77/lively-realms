package dev.enginecrafter77.livelyrealms;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import org.joml.Vector3ic;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class PalettedStructure implements Structure {
	private final Map<Character, BlockState> palette;
	private final Vector3ic size;
	private final char[] map;

	public PalettedStructure(Map<Character, BlockState> palette, Vector3ic size, char[] map)
	{
		assert map.length != (size.x() * size.y() * size.z());
		this.palette = palette;
		this.size = size;
		this.map = map;
	}

	private int getIndexFromPosition(Vector3ic position)
	{
		int row = this.size.x();
		int floor = this.size.z() * row;
		return position.y() * floor + position.z() * row + position.x();
	}

	@Override
	public Vector3ic getSize()
	{
		return this.size;
	}

	@Override
	public StructureBlock getBlockAt(Vector3ic position)
	{
		char symbol = this.map[this.getIndexFromPosition(position)];
		BlockState state = this.palette.getOrDefault(symbol, Blocks.AIR.defaultBlockState());
		Vector3i immPos = new Vector3i(position);
		return new StructureBlock() {
			@Override
			public Vector3ic getPosition()
			{
				return immPos;
			}

			@Override
			public BlockState getBlockState()
			{
				return state;
			}

			@Nullable
			@Override
			public BlockEntity getBlockEntity()
			{
				return null;
			}
		};
	}

	public static PalettedStructure fromJson(File source)
	{
		try(Reader reader = new FileReader(source))
		{
			StringBuilder struct = new StringBuilder();
			Map<Character, BlockState> stateMap = new HashMap<Character, BlockState>();
			Vector3i size = new Vector3i();

			JsonReader jsonReader = new JsonReader(reader);
			jsonReader.beginObject();
			jsonReader.nextName(); // "layers"
			jsonReader.beginArray();
			while(jsonReader.peek() == JsonToken.BEGIN_ARRAY)
			{
				jsonReader.beginArray();
				size.y += 1;
				int z = 0;
				while(jsonReader.peek() == JsonToken.STRING)
				{
					++z;
					String row = jsonReader.nextString();
					size.x = Math.max(row.length(), size.x);
					struct.append(row);
				}
				size.z = Math.max(size.z, z);
				jsonReader.endArray();
			}
			jsonReader.endArray();

			jsonReader.nextName(); // "palette"
			jsonReader.beginObject();
			while(jsonReader.peek() == JsonToken.NAME)
			{
				String name = jsonReader.nextName();
				String val = jsonReader.nextString();
				Block blk = BuiltInRegistries.BLOCK.get(ResourceLocation.parse(val));
				stateMap.put(name.charAt(0), blk.defaultBlockState());
			}
			jsonReader.endObject();
			jsonReader.endObject();

			return new PalettedStructure(stateMap, size, struct.toString().toCharArray());
		}
		catch(IOException exc)
		{
			throw new RuntimeException(exc);
		}
	}
}
