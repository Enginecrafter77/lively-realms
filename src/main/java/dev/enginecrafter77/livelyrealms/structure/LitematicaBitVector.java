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
        int max = (int)Math.pow(2, this.bitsPerEntry);
        if(value < 0 || value >= max)
            throw new IllegalArgumentException(String.format("LitematicaBitVector can contain only values 0 <= x < %d. Value %d does not satisfy that range.", max, value));
    }

    public void set(int index, int value)
    {
        checkValueRange(value);

        int startBit = index * this.bitsPerEntry;
        int endBit = ((index+1) * this.bitsPerEntry) - 1;
        int startIndex = startBit >> 6; // =/64
        int endIndex = endBit >> 6; // =/64
        int startEntryBit = startBit & 0x3F; // =%64

        int startWritten = 64 - startEntryBit;
        long startMask = this.entryMask << startEntryBit;
        long endMask = this.entryMask >>> startWritten;
        long startBits = ((long)value << startEntryBit) & startMask;
        long endBits = ((long)value >>> startWritten) & endMask;
        this.data[startIndex] &= ~startMask;
        this.data[startIndex] |= startBits;
        this.data[endIndex] &= ~endMask;
        this.data[endIndex] |= endBits;
    }

    public int get(int index)
    {
        int startBit = index * this.bitsPerEntry;
        int endBit = ((index+1) * this.bitsPerEntry) - 1;
        int startIndex = startBit >> 6; // =/64
        int endIndex = endBit >> 6; // =/64
        int startEntryBit = startBit & 0x3F; // =%64

        long soup = this.data[startIndex] >>> startEntryBit;
        if(startIndex != endIndex)
            soup |= this.data[endIndex] << (64 - startEntryBit);
        soup &= this.entryMask;
        return (int)soup;
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
