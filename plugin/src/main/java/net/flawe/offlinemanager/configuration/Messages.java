package net.flawe.offlinemanager.configuration;

import net.flawe.annotation.util.Comment;
import net.flawe.annotation.util.ConfigKey;
import net.flawe.annotation.util.Indents;
import net.flawe.offlinemanager.OfflineManager;
import net.flawe.offlinemanager.api.configuration.Configuration;

import java.io.File;
import java.io.IOException;

@Comment({"#OfflineManager 3.0.0-SNAPSHOT", "Created by flawe, LOVEC3327", "Contact https://vk.com/flawesv or https://t.me/flawe_sv", "Messages configuration file"})
public class Messages implements Configuration {

    public Messages(OfflineManager plugin) {
        File file = new File(plugin.getDataFolder(), "messages.yml");
        if (file.exists())
            return;
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Indents(1)
    @ConfigKey("permission-deny")
    private String permissionDeny = "&a[OfflineManager] &eYou not have permissions for do this";
    @ConfigKey("enter-nickname")
    private String enterNickname = "&a[OfflineManager] &eEnter player nickname!";
    @ConfigKey("enter-subcommand")
    private String enterSubCommand = "&a[OfflineManager] &eEnter sub-command!";
    @ConfigKey("player-not-found")
    private String playerNotFound = "&a[OfflineManager] &ePlayer with this name isn't found!";
    @ConfigKey("player-is-online")
    private String playerIsOnline = "&a[OfflineManager] &ePlayer with this name is online.";
    @ConfigKey("command-not-found")
    private String commandNotFound = "&a[OfflineManager] &eThis command isn't found! Use /help for help.";
    @ConfigKey("function-disabled")
    private String functionDisabled = "&a[OfflineManager] &eThe function %function% is disabled!";
    @ConfigKey("already-being-edited")
    private String alreadyBeingEdited = "&a[OfflineManager] &eThis player already being edited other player!";
    @ConfigKey("err-message")
    private String errorMessage = "&a[OfflineManager] &eSomething went wrong...";
    @Indents(1)
    @ConfigKey("teleport-success")
    private String teleportSuccess = "&a[OfflineManager] &eSuccessful teleportation to the player!";
    @ConfigKey("teleport-here")
    private String teleportHere = "&a[OfflineManager] &eThe player successfully teleported to you!";
    @ConfigKey("teleport-another")
    private String teleportAnother = "&a[OfflineManager] &eThe player teleported to another player!";
    @ConfigKey("gamemode-changed")
    private String gamemodeChanged = "&a[OfflineManager] &eSuccessful change gamemode for player!";
    @ConfigKey("heal-player")
    private String healPlayer = "&a[OfflineManager] &eThe player is healed!";
    @ConfigKey("kill-player")
    private String killPlayer = "&a[OfflineManager] &eThe player has been killed!";
    @ConfigKey("feed-player")
    private String feedPlayer = "&a[OfflineManager] &eThe player was fed!";
    @ConfigKey("clear-inventory")
    private String clearInventory = "&a[OfflineManager] &ePlayer's inventory has been cleared!";

    public String getPermissionDeny() {
        return permissionDeny;
    }

    public String getEnterNickname() {
        return enterNickname;
    }

    public String getEnterSubCommand() {
        return enterSubCommand;
    }

    public String getPlayerNotFound() {
        return playerNotFound;
    }

    public String getPlayerIsOnline() {
        return playerIsOnline;
    }

    public String getCommandNotFound() {
        return commandNotFound;
    }

    public String getFunctionDisabled() {
        return functionDisabled;
    }

    public String getAlreadyBeingEdited() {
        return alreadyBeingEdited;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public String getTeleportSuccess() {
        return teleportSuccess;
    }

    public String getTeleportHere() {
        return teleportHere;
    }

    public String getTeleportAnother() {
        return teleportAnother;
    }

    public String getGamemodeChanged() {
        return gamemodeChanged;
    }

    public String getHealPlayer() {
        return healPlayer;
    }

    public String getKillPlayer() {
        return killPlayer;
    }

    public String getFeedPlayer() {
        return feedPlayer;
    }

    public String getClearInventory() {
        return clearInventory;
    }

    @Override
    public String toString() {
        return "Messages{" +
                "permissionDeny='" + permissionDeny + '\'' +
                ", enterNickname='" + enterNickname + '\'' +
                ", enterSubCommand='" + enterSubCommand + '\'' +
                ", playerNotFound='" + playerNotFound + '\'' +
                ", playerIsOnline='" + playerIsOnline + '\'' +
                ", commandNotFound='" + commandNotFound + '\'' +
                ", functionDisabled='" + functionDisabled + '\'' +
                ", alreadyBeingEdited='" + alreadyBeingEdited + '\'' +
                ", teleportSuccess='" + teleportSuccess + '\'' +
                ", teleportHere='" + teleportHere + '\'' +
                ", teleportAnother='" + teleportAnother + '\'' +
                ", gamemodeChanged='" + gamemodeChanged + '\'' +
                ", healPlayer='" + healPlayer + '\'' +
                ", killPlayer='" + killPlayer + '\'' +
                ", feedPlayer='" + feedPlayer + '\'' +
                ", clearInventory='" + clearInventory + '\'' +
                '}';
    }
}
