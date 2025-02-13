package dev.enginecrafter77.livelyrealms.structure;

import it.unimi.dsi.fastutil.ints.IntIterable;
import it.unimi.dsi.fastutil.ints.IntIterator;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.LongArrayTag;
import net.neoforged.neoforge.common.util.INBTSerializable;

import javax.annotation.Nonnull;
import java.util.*;

public class LitematicaBitVector implements IntIterable, INBTSerializable<LongArrayTag> {
    private final int bitsPerEntry;
    private final int entriesPerSlot;
    private final long entryMask;

    private int entryCount;
    private long[] data;

    public LitematicaBitVector(int bitsPerEntry, int size)
    {
        this.bitsPerEntry = bitsPerEntry;
        this.entriesPerSlot = (Long.BYTES * 8) / this.bitsPerEntry;
        this.entryMask = ~(-1L << this.bitsPerEntry);

        int bits = size * this.bitsPerEntry;
        int arrayLength = (int)Math.ceil((double)bits / 64D);
        this.setArray(new long[arrayLength]);
    }

    protected void setArray(long[] array)
    {
        this.entryCount = array.length * this.entriesPerSlot;
        this.data = array;
    }

    public int getLength()
    {
        return this.entryCount;
    }

    private void checkValueRange(int value)
    {
        if(value < 0 || value > (int)Math.pow(10, this.bitsPerEntry))
            throw new IllegalArgumentException();
    }

    public void set(int index, int value)
    {
        // I don't really feel confident reproducing this, so I copied it from https://github.com/maruohon/litematica/blob/08423854c5b647e4268633bc5b511d1c50a27f38/src/main/java/litematica/schematic/container/LitematicaBitArray.java
        checkValueRange(value);

        long startOffset = index * (long) this.bitsPerEntry;
        int startArrIndex = (int) (startOffset >> 6); // startOffset / 64
        int endArrIndex = (int) (((index + 1L) * (long) this.bitsPerEntry - 1L) >> 6);
        int startBitOffset = (int) (startOffset & 0x3F); // startOffset % 64
        this.data[startArrIndex] = this.data[startArrIndex] & ~(this.entryMask << startBitOffset) | ((long)value & this.entryMask) << startBitOffset;

        if(startArrIndex != endArrIndex)
        {
            int endOffset = 64 - startBitOffset;
            int j1 = this.bitsPerEntry - endOffset;
            this.data[endArrIndex] = this.data[endArrIndex] >>> j1 << j1 | ((long)value & this.entryMask) >> endOffset;
        }
    }

    public int get(int index)
    {
        // I don't really feel confident reproducing this, so I copied it from https://github.com/maruohon/litematica/blob/08423854c5b647e4268633bc5b511d1c50a27f38/src/main/java/litematica/schematic/container/LitematicaBitArray.java
        long startOffset = index * (long) this.bitsPerEntry;
        int startArrIndex = (int)(startOffset >> 6); // startOffset / 64
        int endArrIndex = (int) (((index + 1L) * (long) this.bitsPerEntry - 1L) >> 6);
        int startBitOffset = (int) (startOffset & 0x3F); // startOffset % 64

        int value;
        if(startArrIndex == endArrIndex)
        {
            value = (int)(this.data[startArrIndex] >>> startBitOffset & this.entryMask);
        }
        else
        {
            int endOffset = 64 - startBitOffset;
            value = (int)((this.data[startArrIndex] >>> startBitOffset | this.data[endArrIndex] << endOffset) & this.entryMask);
        }

        return value;
    }

    @Nonnull
    public LitematicaBitVector copy()
    {
        LitematicaBitVector copy = new LitematicaBitVector(this.bitsPerEntry, this.data.length);
        copy.setArray(Arrays.copyOf(this.data, this.data.length));
        return copy;
    }

    @Nonnull
    @Override
    public IntIterator iterator()
    {
        return new LitematicaBitVectorIterator();
    }

    @Override
    public LongArrayTag serializeNBT(HolderLookup.Provider provider)
    {
        return new LongArrayTag(this.data);
    }

    @Override
    public void deserializeNBT(HolderLookup.Provider provider, LongArrayTag tag)
    {
        this.setArray(tag.getAsLongArray());
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(this.entriesPerSlot, this.entryCount, this.entryMask, this.bitsPerEntry, Arrays.hashCode(this.data));
    }

    @Override
    public boolean equals(Object obj)
    {
        if(!(obj instanceof LitematicaBitVector other))
            return false;

        if(!Objects.equals(this.bitsPerEntry, other.bitsPerEntry))
            return false;

        return Arrays.equals(this.data, other.data);
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder(this.getClass().getSimpleName());
        builder.append('[');
        for(int index = 0; index < this.entryCount; ++index)
        {
            builder.append(this.get(index));
            if(index > 64)
            {
                builder.append("...");
                break;
            }

            if(index != (this.entryCount - 1))
                builder.append(", ");
        }
        builder.append(']');
        return builder.toString();
    }

    public static LitematicaBitVector readFromNBT(int bitsPerEntry, HolderLookup.Provider provider, LongArrayTag nbt)
    {
        LitematicaBitVector vector = new LitematicaBitVector(bitsPerEntry, nbt.size());
        vector.deserializeNBT(provider, nbt);
        return vector;
    }

    public static LitematicaBitVector forEntries(int options, int size)
    {
        return new LitematicaBitVector(optionCountToBitsPerEntry(options), size);
    }

    public static LitematicaBitVector fromArray(int options, long[] array)
    {
        LitematicaBitVector vector = new LitematicaBitVector(optionCountToBitsPerEntry(options), array.length);
        vector.setArray(Arrays.copyOf(array, array.length));
        return vector;
    }

    private static int optionCountToBitsPerEntry(int options)
    {
        return Math.max((int)Math.ceil(Math.log(options) / Math.log(2)), 1);
    }

    private class LitematicaBitVectorIterator implements IntIterator
    {
        private final int limit;
        private int index;

        public LitematicaBitVectorIterator()
        {
            this.limit = LitematicaBitVector.this.getLength() - 1;
            this.index = -1;
        }

        @Override
        public boolean hasNext()
        {
            return this.index < this.limit;
        }

        @Override
        public int nextInt()
        {
            return LitematicaBitVector.this.get(++this.index);
        }
    }
}
