package net.flawe.offlinemanager.api.util.v1_17_R1.data;

import net.flawe.offlinemanager.api.nbt.ITagCompound;
import net.flawe.offlinemanager.api.nbt.TagValue;
import net.flawe.offlinemanager.api.nbt.type.*;
import net.minecraft.nbt.*;
import org.jetbrains.annotations.NotNull;

public class TagCompound implements ITagCompound {

    private final NBTTagCompound tag;

    public TagCompound(NBTTagCompound tag) {
        this.tag = tag;
    }

    @Override
    public TagValue<?> getTagValue(@NotNull String key) {
        NBTBase nbtBase = tag.get(key);
        return getTagValue(nbtBase);
    }

    @Override
    public ITagCompound getTagCompound(@NotNull String key) {
        NBTBase base = tag.get(key);
        if (!(base instanceof NBTTagCompound)) return null;
        return new TagCompound((NBTTagCompound) base);
    }

    @Override
    public void setValue(@NotNull String key, @NotNull TagValue<?> value) {
        tag.set(key, getNBTValue(value));
    }

    private NBTBase getNBTValue(TagValue<?> value) {
        switch (value.getType()) {
            case BYTE:
                return NBTTagByte.a(((TagByte) value).getValue());
            case SHORT:
                return NBTTagShort.a(((TagShort) value).getValue());
            case INTEGER:
                return NBTTagInt.a(((TagInteger) value).getValue());
            case LONG:
                return NBTTagLong.a(((TagLong) value).getValue());
            case FLOAT:
                return NBTTagFloat.a(((TagFloat) value).getValue());
            case DOUBLE:
                return NBTTagDouble.a(((TagDouble) value).getValue());
            case STRING:
                return NBTTagString.a(((TagString) value).getValue());
            case LIST:
                NBTTagList list = new NBTTagList();
                TagList tagList = (TagList) value;
                for (TagValue<?> val : tagList.getValue())
                    list.add(getNBTValue(val));
                return list;
        }
        return null;
    }

    private TagValue<?> getTagValue(NBTBase base) {
        if (base == null) return null;
        switch (base.getTypeId()) {
            case TagTypes.BYTE:
                return new TagByte(((NBTTagByte) base).asByte());
            case TagTypes.SHORT:
                return new TagShort(((NBTTagShort) base).asShort());
            case TagTypes.INTEGER:
                return new TagInteger(((NBTTagInt) base).asInt());
            case TagTypes.LONG:
                return new TagLong(((NBTTagLong) base).asLong());
            case TagTypes.FLOAT:
                return new TagFloat(((NBTTagFloat) base).asFloat());
            case TagTypes.DOUBLE:
                return new TagDouble(((NBTTagDouble) base).asDouble());
            case TagTypes.STRING:
                return new TagString(base.asString());
            case TagTypes.LIST:
                TagList list = new TagList();
                for (NBTBase val : (NBTTagList) base)
                    list.addValue(getTagValue(val));
                return list;
        }
        return null;
    }

    public NBTTagCompound getTag() {
        return tag;
    }

}

