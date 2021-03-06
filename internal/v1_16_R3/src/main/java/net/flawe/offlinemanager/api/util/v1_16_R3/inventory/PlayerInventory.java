package net.flawe.offlinemanager.api.util.v1_16_R3.inventory;

import net.flawe.offlinemanager.api.inventory.AbstractPlayerInventory;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagInt;
import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftInventoryPlayer;

public class PlayerInventory extends AbstractPlayerInventory {

    private final NBTTagCompound tag;

    public PlayerInventory(org.bukkit.inventory.PlayerInventory inventory, NBTTagCompound tag) {
        super(inventory);
        this.tag = tag;
    }

    @Override
    protected void update() {
        NBTTagList inventory = new NBTTagList();
        ((CraftInventoryPlayer) this.inventory).getInventory().a(inventory);
        tag.set("Inventory", inventory);
    }

    @Override
    public void setHeldItemSlot(int i) {
        tag.set("SelectedItemSlot", NBTTagInt.a(i));
    }
}
