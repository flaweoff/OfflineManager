package net.flawe.offlinemanager.api.util.v1_16_R3;

import com.mojang.authlib.GameProfile;
import net.flawe.offlinemanager.OfflineManager;
import net.flawe.offlinemanager.api.IArmorInventory;
import net.flawe.offlinemanager.api.IEnderChest;
import net.flawe.offlinemanager.api.IInventory;
import net.flawe.offlinemanager.api.IUser;
import net.flawe.offlinemanager.api.enums.SavePlayerType;
import net.flawe.offlinemanager.events.LoadPlayerEvent;
import net.flawe.offlinemanager.events.SavePlayerEvent;
import net.minecraft.server.v1_16_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.UUID;

public class OfflineUser implements IUser {

    private final OfflinePlayer offlinePlayer;
    private final OfflineManager plugin;
    private final Player player;
    private final WorldNBTStorage storage = getWorldServer().getMinecraftServer().getPlayerList().playerFileData;
    private final UUID uuid;

    private GameMode gameMode;

    public OfflineUser(OfflineManager plugin, String name) {
        this(plugin, Bukkit.getOfflinePlayer(name));
    }

    public OfflineUser(OfflineManager plugin, UUID uuid) {
        this(plugin ,Bukkit.getOfflinePlayer(uuid));
    }

    public OfflineUser(OfflineManager plugin, OfflinePlayer offlinePlayer) {
        this.offlinePlayer = offlinePlayer;
        this.plugin = plugin;
        this.player = getEntityPlayer().getBukkitEntity().getPlayer();
        if (player != null) {
            LoadPlayerEvent event = new LoadPlayerEvent(player);
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(event));
            player.loadData();
        }
        this.uuid = offlinePlayer.getUniqueId();
    }

    private MinecraftServer getMinecraftServer() {
        return ((CraftServer) Bukkit.getServer()).getServer();
    }

    private EntityPlayer getEntityPlayer() {
        GameProfile profile = new GameProfile(offlinePlayer.getUniqueId(), offlinePlayer.getName());
        return new EntityPlayer(getMinecraftServer(), getWorldServer(), profile, new PlayerInteractManager(getWorldServer()));
    }

    private WorldServer getWorldServer() {
        return getMinecraftServer().getWorldServer(World.OVERWORLD);
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public OfflinePlayer getOfflinePlayer() {
        return offlinePlayer;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public Location getLocation() {
        NBTTagCompound tag = storage.getPlayerData(offlinePlayer.getUniqueId().toString());
        NBTTagList pos = (NBTTagList) tag.get("Pos");
        NBTTagList rotation = (NBTTagList) tag.get("Rotation");
        if (pos == null || rotation == null)
            return player.getLocation();
        double x = pos.h(0);
        double y = pos.h(1);
        double z = pos.h(2);
        float yaw = rotation.i(0);
        float pitch = rotation.i(1);
        org.bukkit.World world = Bukkit.getWorld(new UUID(tag.getLong("WorldUUIDMost"), tag.getLong("WorldUUIDLeast")));
        return new Location(world, x, y, z, yaw, pitch);
    }

    @Override
    public IInventory getInventory() {
        return new OfflineInventory(player);
    }

    @Override
    public IEnderChest getEnderChest() {
        return new OfflineEnderChest(player);
    }

    @Override
    public IArmorInventory getArmorInventory() {
        return new ArmorInventory(player);
    }

    @Override
    public GameMode getGameMode() {
        return gameMode == null ? player.getGameMode() : gameMode;
    }

    @Override
    public void kill() {
        ((CraftPlayer) player).getHandle().setHealth(0);
    }

    @Override
    public void setGameMode(GameMode gameMode) {
        int value = gameMode.getValue();
        NBTTagCompound tag = storage.getPlayerData(uuid.toString());
        tag.setInt("playerGameType", value);
        tagSave(tag, SavePlayerType.GAMEMODE);
    }

    @Override
    public void teleport(Location location) {
        NBTTagCompound tag = storage.getPlayerData(uuid.toString());
        if (tag == null)
            return;
        NBTTagList pos = new NBTTagList();
        NBTTagList rotation = new NBTTagList();
        pos.add(0, NBTTagDouble.a(location.getX()));
        pos.add(1, NBTTagDouble.a(location.getY()));
        pos.add(2, NBTTagDouble.a(location.getZ()));
        rotation.add(0, NBTTagFloat.a(location.getYaw()));
        rotation.add(1, NBTTagFloat.a(location.getPitch()));
        tag.set("Pos", pos);
        tag.set("Rotation", rotation);
        tag.setLong("WorldUUIDMost", location.getWorld().getUID().getMostSignificantBits());
        tag.setLong("WorldUUIDLeast", location.getWorld().getUID().getLeastSignificantBits());
        WorldServer w = ((CraftWorld) location.getWorld()).getHandle();
        tag.setString("Dimension", w.getDimensionKey().a().toString());
        tagSave(tag, SavePlayerType.LOCATION);
    }

    @Override
    public void teleport(Player player) {
        teleport(player.getLocation());
    }

    @Override
    public void save() {
        SavePlayerEvent event = new SavePlayerEvent(player, SavePlayerType.DEFAULT);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        player.saveData();
    }

    @Override
    public void save(SavePlayerType type) {
        SavePlayerEvent event = new SavePlayerEvent(player, type);
        Bukkit.getScheduler().runTask(plugin, () -> {
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            player.saveData();
        });
    }

    private void tagSave(NBTTagCompound tag, SavePlayerType type) {
        SavePlayerEvent event = new SavePlayerEvent(player, type);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            try (FileOutputStream stream = new FileOutputStream(new File(storage.getPlayerDir(), uuid + ".dat"))) {
                NBTCompressedStreamTools.a(tag, stream);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
