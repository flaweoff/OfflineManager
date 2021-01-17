package net.flawe.offlinemanager.commands.subs;

import net.flawe.offlinemanager.commands.OMCommand;
import net.flawe.offlinemanager.util.ColorUtils;
import net.flawe.offlinemanager.util.configuration.PlaceholderUtil;
import org.bukkit.entity.Player;

import static net.flawe.offlinemanager.util.constants.Messages.*;

public class ReloadCommand extends OMCommand {

    public ReloadCommand(String name, String help, String permission) {
        super(name, help, permission);
    }

    @Override
    public void execute(Player player, String[] args) {
        addPlaceholder("%player%", player.getName());
        addPlaceholder("%permission%", getPermission());
        if (!hasPermission(player)) {
            String msg = api.getConfigManager().getMessageString(player, permissionDeny);
            player.sendMessage(PlaceholderUtil.fillPlaceholders(msg, getPlaceholders()));
            return;
        }
        api.getConfigManager().reloadConfig();
        api.getConfigManager().reloadMessages();
        player.sendMessage(ColorUtils.getFormattedString("&a[OfflineManager] &eConfiguration file success reloaded!"));
    }
}
