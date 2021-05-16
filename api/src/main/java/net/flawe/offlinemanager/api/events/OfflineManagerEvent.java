package net.flawe.offlinemanager.api.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class OfflineManagerEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    public OfflineManagerEvent(boolean async) {
        super(async);
    }

    public OfflineManagerEvent() {

    }

    @NotNull
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

}