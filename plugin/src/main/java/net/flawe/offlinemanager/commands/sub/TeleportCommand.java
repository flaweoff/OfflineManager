package net.flawe.offlinemanager.commands.sub;

import net.flawe.offlinemanager.api.data.entity.IPlayerData;
import net.flawe.offlinemanager.api.enums.SavePlayerType;
import net.flawe.offlinemanager.api.event.entity.player.OfflinePlayerTeleportEvent;
import net.flawe.offlinemanager.api.event.entity.player.TeleportToOfflinePlayerEvent;
import net.flawe.offlinemanager.commands.OMCommand;
import net.flawe.offlinemanager.placeholders.Placeholder;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;


public class TeleportCommand extends OMCommand {

    public TeleportCommand(String name, String help, String permission, String[] aliases) {
        super(name, help, permission, aliases);
        addPlaceholders
                (
                        new Placeholder("%fucntion%", "Teleport"),
                        new Placeholder("%permission%", permission)
                );
    }

    @Override
    public void execute(Player player, String[] args) {
        removePlaceholder("%to%");
        addPlaceholder(new Placeholder("%player%", player.getName()));
        if (!settings.getTeleportConfiguration().enabled()) {
            sendPlayerMessage(player, messages.getFunctionDisabled());
            return;
        }
        if (!hasPermission(player)) {
            sendPlayerMessage(player, messages.getPermissionDeny());
            return;
        }
        if (args.length == 1) {
            sendPlayerMessage(player, messages.getEnterNickname());
            return;
        }
        String playerName = args[1];
        addPlaceholder("%target%", playerName);
        Player target = Bukkit.getPlayerExact(playerName);
        if (target != null && target.isOnline()) {
            sendPlayerMessage(player, messages.getPlayerIsOnline());
            return;
        }
        if (!api.getStorage().hasPlayer(playerName)) {
            sendPlayerMessage(player, messages.getPlayerNotFound());
            return;
        }
        IPlayerData playerData = api.getPlayerData(playerName);
        if (args.length == 2) {
            TeleportToOfflinePlayerEvent event = new TeleportToOfflinePlayerEvent(player, playerData, player.getLocation(), playerData.getLocation());
            Bukkit.getPluginManager().callEvent(event);
            if (event.isCancelled())
                return;
            player.teleport(playerData.getLocation());
            sendPlayerMessage(player, messages.getTeleportSuccess());
            return;
        }
        String toPlayer = args[2];
        addPlaceholder(new Placeholder("%to%", toPlayer));
        Player to = Bukkit.getPlayerExact(toPlayer);
        OfflinePlayerTeleportEvent event;
        if (to != null && to.isOnline()) {
            event = new OfflinePlayerTeleportEvent(player, playerData, to.getLocation(), playerData.getLocation());
            if (event.isCancelled())
                return;
            playerData.setLocation(to.getLocation());
            playerData.save(SavePlayerType.LOCATION);
            sendPlayerMessage(player, messages.getTeleportAnother());
            return;
        }
        if (!api.getStorage().hasPlayer(toPlayer)) {
            sendPlayerMessage(player, messages.getPlayerNotFound());
            return;
        }
        IPlayerData targetPlayer = api.getPlayerData(toPlayer);
        event = new OfflinePlayerTeleportEvent(player, playerData, targetPlayer.getLocation(), playerData.getLocation());
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled())
            return;
        playerData.setLocation(targetPlayer.getLocation());
        playerData.save(SavePlayerType.LOCATION);
        sendPlayerMessage(player, messages.getTeleportAnother());
    }
}
