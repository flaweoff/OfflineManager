package net.flawe.offlinemanager.api.util.v1_17_R1.data;

import com.mojang.authlib.GameProfile;
import net.flawe.offlinemanager.api.OfflineManagerAPI;
import net.flawe.offlinemanager.api.data.entity.AbstractPlayerData;
import net.flawe.offlinemanager.api.entity.IUser;
import net.flawe.offlinemanager.api.enums.SavePlayerType;
import net.flawe.offlinemanager.api.event.data.LoadPlayerEvent;
import net.flawe.offlinemanager.api.event.data.SavePlayerEvent;
import net.flawe.offlinemanager.api.inventory.AbstractPlayerInventory;
import net.flawe.offlinemanager.api.inventory.IArmorInventory;
import net.flawe.offlinemanager.api.inventory.IEnderChest;
import net.flawe.offlinemanager.api.util.v1_17_R1.inventory.ArmorInventory;
import net.flawe.offlinemanager.api.util.v1_17_R1.inventory.OfflineEnderChest;
import net.minecraft.nbt.NBTCompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.EntityPlayer;
import net.minecraft.server.level.WorldServer;
import net.minecraft.world.entity.player.PlayerInventory;
import net.minecraft.world.level.World;
import net.minecraft.world.level.storage.WorldNBTStorage;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_17_R1.CraftServer;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftInventoryPlayer;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class PlayerData extends AbstractPlayerData {

    private final OfflineManagerAPI api;
    private final UUID uuid;
    private final String name;
    private final WorldNBTStorage worldNBTStorage;
    private final NBTTagCompound tag;
    private final File playerDir;
    private final net.flawe.offlinemanager.api.util.v1_17_R1.inventory.PlayerInventory playerInventory;
    private final ArmorInventory armorInventory;
    private final IEnderChest enderChest;

    public PlayerData(String name, OfflineManagerAPI api) {
        //noinspection deprecation
        this(Bukkit.getOfflinePlayer(name).getUniqueId(), api);
    }

    public PlayerData(UUID uuid, OfflineManagerAPI api) {
        this(uuid, new TagCompound(((CraftServer) Bukkit.getServer()).getHandle().r.getPlayerData(uuid.toString())), api);
    }

    public PlayerData(UUID uuid, TagCompound compound, OfflineManagerAPI api) {
        super(uuid, compound);
        this.api = api;
        this.uuid = uuid;
        MinecraftServer server = ((CraftServer) Bukkit.getServer()).getServer();
        WorldServer worldServer = server.getWorldServer(World.f);
        if (worldServer == null)
            throw new NullPointerException("Overworld cannot be null!");
        GameProfile profile = new GameProfile(uuid, Bukkit.getOfflinePlayer(uuid).getName());
        EntityPlayer entityPlayer = new EntityPlayer(server, worldServer, profile);
        entityPlayer.load(compound.getTag());
        entityPlayer.loadData(compound.getTag());
        this.name = entityPlayer.getName();
        this.worldNBTStorage = server.k;
        this.tag = compound.getTag();
        this.playerDir = worldNBTStorage.getPlayerDir();
        NBTTagList inventoryList = (NBTTagList) tag.get("Inventory");
        PlayerInventory virtual = new PlayerInventory(entityPlayer);
        virtual.b(inventoryList);
        this.playerInventory = new net.flawe.offlinemanager.api.util.v1_17_R1.inventory.PlayerInventory(new CraftInventoryPlayer(virtual), tag);
        this.armorInventory = new ArmorInventory(this);
        this.enderChest = new OfflineEnderChest(Bukkit.createInventory(null, InventoryType.ENDER_CHEST), tag);
        LoadPlayerEvent event = new LoadPlayerEvent(this);
        Bukkit.getPluginManager().callEvent(event);
    }

    @Override
    public AbstractPlayerInventory getInventory() {
        return playerInventory;
    }

    @Override
    public IArmorInventory getArmorInventory() {
        return armorInventory;
    }

    @Override
    public IEnderChest getEnderChest() {
        return enderChest;
    }

    @Override
    public void save() {
        save(SavePlayerType.DEFAULT);
    }

    @Override
    public void save(SavePlayerType type) {
        SavePlayerEvent event = new SavePlayerEvent(this, type);
        Bukkit.getPluginManager().callEvent(event);
        try {
            File file = new File(this.playerDir, uuid + ".dat.tmp");
            File file1 = new File(this.playerDir, uuid + ".dat");
            NBTCompressedStreamTools.a(tag, new FileOutputStream(file));
            if (file1.exists())
                if (!file1.delete()) throw new IOException(String.format("Failed to delete tmp player file %s", uuid.toString()));
            if (!file.renameTo(file1)) throw new IOException(String.format("Failed to rename player file %s", uuid.toString()));
        } catch (Exception e) {
            ((Plugin) api).getLogger().warning("Failed to save player data for " + name);
            e.printStackTrace();
        }
    }

    @Override
    public IUser getUser() {
        return new OfflineUser((Plugin) api, uuid, tag);
    }

}
