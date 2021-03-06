package net.flawe.offlinemanager.api.event.inventory;

import net.flawe.offlinemanager.api.data.entity.IPlayerData;
import net.flawe.offlinemanager.api.entity.IUser;
import net.flawe.offlinemanager.api.enums.InventoryType;
import net.flawe.offlinemanager.api.event.OfflineManagerEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;

/**
 * Called when player opens offline player inventory
 * @author flawe
 */
public class OpenOfflineInventoryEvent extends OfflineManagerEvent implements Cancellable {

    private final Player player;
    private final IUser target;
    private final IPlayerData playerData;
    private final Inventory inventory;
    private boolean cancelled;
    private final InventoryType inventoryType;

    @Deprecated
    public OpenOfflineInventoryEvent(@NotNull Player player, @NotNull IUser target, @NotNull Inventory inventory, @NotNull InventoryType type) {
        this.player = player;
        this.target = target;
        this.inventory = inventory;
        this.inventoryType = type;
        this.playerData = target.getPlayerData();
    }

    public OpenOfflineInventoryEvent(@NotNull Player player, @NotNull IPlayerData playerData, @NotNull Inventory inventory, @NotNull InventoryType type) {
        this.player = player;
        this.playerData = playerData;
        this.inventoryType = type;
        this.inventory = inventory;
        this.target = playerData.getUser();
    }

    /**
     * Gets the player who open the inventory
     * @return player whose open the inventory
     */
    @NotNull
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets the inventory owner
     * @deprecated use getPlayerData
     * @return owner of inventory
     */
    @Deprecated
    @NotNull
    public IUser getTarget() {
        return target;
    }

    /**
     * Gets the inventory owner
     * @return owner of inventory
     */
    @NotNull
    public IPlayerData getPlayerData() {
        return playerData;
    }

    /**
     * Gets the inventory who's gonna be open
     * @return inventory based of target player data
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Gets the inventory type
     * @return type of inventory
     */
    @NotNull
    public InventoryType getInventoryType() {
        return inventoryType;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean b) {
        this.cancelled = b;
    }

}
