package net.flawe.offlinemanager.expansion;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import net.flawe.offlinemanager.api.OfflineManagerAPI;
import net.flawe.offlinemanager.api.data.entity.IPlayerData;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OfflineManagerExpansion extends PlaceholderExpansion {

    private final OfflineManagerAPI api;

    public OfflineManagerExpansion(OfflineManagerAPI api) {
        this.api = api;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "om";
    }

    @Override
    public @NotNull String getAuthor() {
        return "flaweoff";
    }

    @Override
    public @NotNull String getVersion() {
        return api.getVersion();
    }

    @Override
    public boolean canRegister() {
        return true;
    }

    @Override
    public boolean persist() {
        return true;
    }

    @Override
    public synchronized String onRequest(OfflinePlayer player, @NotNull String params) {
        if (player == null)
            return null;
        if (!player.hasPlayedBefore())
            return null;
        if (!player.isOnline()) {
            IPlayerData playerData = api.getPlayerData(player.getName());
            if (playerData == null)
                return null;
            return onPlaceholderRequest(playerData, params);
        }
        return onPlaceholderRequest(player.getPlayer(), params);
    }

    @Override
    public String onPlaceholderRequest(Player player, @NotNull String params) {
        if (params.equalsIgnoreCase("player_name")) {
            return player.getName();
        }
        if (params.equalsIgnoreCase("player_healths")) {
            return String.valueOf(player.getHealth());
        }
        if (params.equalsIgnoreCase("player_food")) {
            return String.valueOf(player.getFoodLevel());
        }
        if (params.toLowerCase().startsWith("player_loc")) {
            Location loc = player.getLocation();
            if (api.getStorage().hasPlayer(player.getName())) {
                loc = api.getUser(player.getUniqueId()).getLocation();
            }
            if (params.equalsIgnoreCase("player_locX")) {
                return String.valueOf(loc.getX());
            }
            if (params.equalsIgnoreCase("player_locY")) {
                return String.valueOf(loc.getY());
            }
            if (params.equalsIgnoreCase("player_locZ")) {
                return String.valueOf(loc.getZ());
            }
            if (params.equalsIgnoreCase("player_locYaw")) {
                return String.valueOf(loc.getYaw());
            }
            if (params.equalsIgnoreCase("player_locPitch")) {
                return String.valueOf(loc.getPitch());
            }
            if (params.equalsIgnoreCase("player_locWorld")) {
                if (loc.getWorld() != null)
                    return loc.getWorld().getName();
            }
        }
        if (params.equalsIgnoreCase("player_uuid")) {
            return player.getUniqueId().toString();
        }
        return null;
    }

    public synchronized String onPlaceholderRequest(IPlayerData playerData, @NotNull String params) {
        if (params.equalsIgnoreCase("player_name")) {
            return playerData.getName();
        }
        if (params.equalsIgnoreCase("player_healths")) {
            return String.valueOf(playerData.getHealth());
        }
        if (params.equalsIgnoreCase("player_food")) {
            return String.valueOf(playerData.getFoodLevel());
        }
        if (params.toLowerCase().startsWith("player_loc")) {
            Location loc = playerData.getLocation();
            if (params.equalsIgnoreCase("player_locX")) {
                return String.valueOf(loc.getX());
            }
            if (params.equalsIgnoreCase("player_locY")) {
                return String.valueOf(loc.getY());
            }
            if (params.equalsIgnoreCase("player_locZ")) {
                return String.valueOf(loc.getZ());
            }
            if (params.equalsIgnoreCase("player_locYaw")) {
                return String.valueOf(loc.getYaw());
            }
            if (params.equalsIgnoreCase("player_locPitch")) {
                return String.valueOf(loc.getPitch());
            }
            if (params.equalsIgnoreCase("player_locWorld")) {
                if (loc.getWorld() != null)
                    return loc.getWorld().getName();
            }
        }
        if (params.equalsIgnoreCase("player_uuid")) {
            return playerData.getUUID().toString();
        }
        return null;
    }

}
