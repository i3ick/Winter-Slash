package me.i3ick.com;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;

/**
 * Scoreboard class.
 *
 * The logic is: each 'objective' (line 26, 27) holds (shows) the score of our fellow non-existent players:
 * - 'Alive' (his score is always the number of the red or blue team)
 * - 'Frozen' (his score is always the number of the red or blue team)
 */
public class WinterSlashScoreboard {
    private WinterSlashMain plugin;
    public Scoreboard board;
    private Objective redTeam, greenTeam;
    public Score aliveRed, aliveGreen, frozenRed, frozenGreen;

    public WinterSlashScoreboard (WinterSlashMain WinterSlashMain){
        plugin = WinterSlashMain;
        init(); //just to keep the constructor clean.
    }

    public void init(){ //Something like 'main' //Grab the ready template from bukkit.
        board = Bukkit.getServer().getScoreboardManager().getNewScoreboard();

        redTeam = board.registerNewObjective(ChatColor.RED + "Red team", "dummy"); //'Red Team' is how it will be show in the scoreboard.
        greenTeam = board.registerNewObjective(ChatColor.GREEN+ "Green team", "dummy"); //'u0167' means § who means &. Basically, colors.

        aliveRed = redTeam.getScore(Bukkit.getOfflinePlayer("Alive:"));  //see class description line 11~13
        frozenRed = redTeam.getScore(Bukkit.getOfflinePlayer("Frozen:"));

        aliveGreen = greenTeam.getScore(Bukkit.getOfflinePlayer("Alive:"));
        frozenGreen = greenTeam.getScore(Bukkit.getOfflinePlayer("Frozen:"));

        redTeam.setDisplaySlot(DisplaySlot.SIDEBAR);    //Where to put the objective
        greenTeam.setDisplaySlot(DisplaySlot.SIDEBAR);
    }
}
