package it.masch.bukkit.expBank;

import java.util.logging.Logger;
import org.bukkit.command.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Main class for the EXP-Banking Plugin. 
 * 
 * @author Mark Schmale <masch@masch.it>
 */
public class ExpBankMain  extends JavaPlugin
{
    public static final String VERSION = "0.2.0";
    public static final String NAME = "expBank";

    private static final Logger log = Logger.getLogger("Minecraft");
    private CommandExecutor cmd;

    @Override
    public void onEnable() 
    {
        log.info("[" + NAME + "] version " + VERSION + " enabled");
        this.cmd = new ExpCommandExecutor(this);
        this.getCommand("exp").setExecutor(this.cmd);
    }

    @Override
    public void onDisable() 
    {
        log.info("[" + NAME + "] disabled");
        this.saveConfig();
    }

}
