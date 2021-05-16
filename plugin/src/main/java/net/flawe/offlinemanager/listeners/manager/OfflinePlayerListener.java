package net.flawe.offlinemanager.listeners.manager;

import net.flawe.offlinemanager.api.events.data.LoadPlayerEvent;
import net.flawe.offlinemanager.api.events.data.SavePlayerEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class OfflinePlayerListener implements Listener {

    @EventHandler
    public void onLoad(LoadPlayerEvent e) {
        e.setCanceled(true);
    }

    @EventHandler
    public void onSave(SavePlayerEvent e) {

    }

}