package it.masch.bukkit.expBank;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * the CommandExecutor for the /exp command
 * @author Mark Schmale <masch@masch.it>
 */
public class ExpCommandExecutor implements CommandExecutor 
{
    private final FileConfiguration config;

    public ExpCommandExecutor(JavaPlugin plugin) 
    {
        this.config = plugin.getConfig();
    }

    @Override
    public boolean onCommand( CommandSender sender, Command cmnd, 
                              String commandLabel,  String[] args ) 
    {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        } else {
            return false;
        }
        
        int amount = 0;
        boolean amountSet = false, 
                expChanged = false;
               
        String playerName = player.getName().toLowerCase();
        int playerBalance = config.getInt("balance." + playerName, 0);
        int playerExp     = this.getTotalExpForLevel(player.getLevel(), player.getExp());
        
        // is there a amount int the options?
        if(args.length == 2) {
            try {
                amount = Integer.parseInt(args[1]);
                amountSet = true;
            } 
            catch(NullPointerException npe) { // that should not happen!
                sender.sendMessage("Today is Feb. 30?");
            }
            catch(NumberFormatException nfe) {
                sender.sendMessage(args[1] + " is not a number. Back to primary school?");
                return false;
            }
            if(amount < 0) {
                sender.sendMessage("Please give a positive amount");
                return false;
            }
        }
        if(args.length == 0 || args[0].equalsIgnoreCase("show")) {  // just show help
            // goto ende
        }
        else if(args[0].equalsIgnoreCase("dp")) {
            if(!amountSet) {
                amount = playerExp;
            }
            if(playerExp - amount < 0) {
                amount = playerExp;
            }
            playerBalance = playerBalance + amount;
            playerExp = playerExp - amount;
            expChanged = true;
        }
        else if(args[0].equalsIgnoreCase("wd")) {
            if(!amountSet) {
                amount = playerBalance;
            }
            if(playerBalance - amount < 0) {
                amount = playerBalance;
            }
            playerBalance = playerBalance - amount;
            playerExp = playerExp + amount;
            expChanged = true;
        }
        else {
            return false;
        }
        
        //label ende
        if(expChanged) {
            this.config.set("balance." + playerName, playerBalance);
            player.setTotalExperience(0);
            player.setExp(0.0f);
            player.setLevel(0);
            player.giveExp(playerExp);
        }
        sender.sendMessage(  ChatColor.GREEN + "EXP: "
                                + ChatColor.DARK_GREEN + playerExp
                            + ChatColor.WHITE + " | "
                            + ChatColor.GREEN + "Balance: "
                                + ChatColor.DARK_GREEN + playerBalance);
        return true;
    }

    // workaround for buggy player.getTotalExperience();
    private int getTotalExpForLevel(int alvl, float part) 
    {   
        int exp = (int)Math.floor(this.getExpForLevel(alvl) * part);
        while(alvl-- > 0) {
            exp += this.getExpForLevel(alvl);
        }   
        return exp;
    }   

    private int getExpForLevel(int level)
    {   
      if(level >= 30) {
        return 62 + (level - 30) * 7;
      }
      if(level >= 15) {
        return 17 + (level - 15) * 3;
      }
      return 17;    
    }   
}
