package uk.co.vidhucraft.Admin360;

import java.awt.List;
import java.io.IOException;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Admin360 extends JavaPlugin {
	public static List adminsOnline = new List();
	public ConfigurationManager configurationManager;
	public PlayerRequest playerRequestManager;
	public AdminHonor adminHonorManager;
	
	@Override
	public void onEnable(){
		//Initiate Metrics
		try{
			Metrics metrics = new Metrics(this);
			metrics.start();
		}catch(IOException ex){
			System.out.println("[Admin360] Failed to initiate Plugin Metrics");
		}
		
		//Initiate Classes
		configurationManager = new ConfigurationManager(this);
		playerRequestManager = new PlayerRequest(this);
		adminHonorManager = new AdminHonor(this);
		
		//Enables player listener class
		PlayerListener playerListener = new PlayerListener(this);
		getServer().getPluginManager().registerEvents(playerListener, this);
		
		//Load all players who are logged in already if someone reloads the server
		Player[] players = getServer().getOnlinePlayers();
		playerListener.loadLoggedinUsers(players);
	}
	
	@Override
	public void onDisable(){
		adminHonorManager.saveStats();
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String alias, String[] args){
		//No arguments
		if(cmd.getName().equalsIgnoreCase("helpme")){
			if(Perm.hasPermission((Player) sender, "admin360.request", true))
				playerRequestManager.addPlayerInQueue(sender.getName());
			return true;
		}
		
		//Single argument
		if(cmd.getName().equalsIgnoreCase("request") && args.length == 1){
			if(sender instanceof Player){
				if(args[0].equalsIgnoreCase("admin")){
					if(Perm.hasPermission((Player) sender, "admin360.request", true))
						playerRequestManager.addPlayerInQueue(sender.getName());
					return true;
				}else if(args[0].equalsIgnoreCase("cancel")){
					if(Perm.hasPermission((Player) sender, "admin360.request", true))
						playerRequestManager.cancelRequest(sender.getName());
				}else if(args[0].equalsIgnoreCase("aid")){
					if(Perm.hasPermission((Player) sender, "admin360.aid", true))
						playerRequestManager.fulfillNextRequest(sender.getName());
					return true;
				}else if(args[0].equalsIgnoreCase("status")){
					if(Perm.hasPermission((Player) sender, "admin360.status", true))
						playerRequestManager.informRequestStatus(sender.getName());
					return true;
				}else if(args[0].equalsIgnoreCase("count")){
					if(Perm.hasPermission((Player) sender, "admin360.count", true))
						playerRequestManager.informRequestsPending(sender.getName());
					return true;
				}else if(args[0].equalsIgnoreCase("stats")){
					if(Perm.hasPermission((Player) sender, "admin360.stats", true)){
						int honorCount = adminHonorManager.getStats(sender.getName());
						if(honorCount == 0){
							sender.sendMessage(ChatColor.RED + "You have no honors");
						}else{
							sender.sendMessage(ChatColor.GREEN + "You have a total of " + ChatColor.RED + honorCount + 
									ChatColor.GREEN + " honors");
						}
					}
					return true;	
				}
			}
			if(args[0].equalsIgnoreCase("purge")){
				if(Perm.hasPermission((Player) sender, "admin360.purge", true))
					playerRequestManager.purageRequests();
				return true;
			}
		}else if(cmd.getName().equalsIgnoreCase("request") && args.length == 2){
			if(args[0].equalsIgnoreCase("stats")){
				int honorCount = adminHonorManager.getStats(args[1]);
				String msg = "";
				if(honorCount == 0){
					msg += ChatColor.RED + args[1] + " has no honors";
				}else{
					msg += ChatColor.GREEN + args[1] + " has a total of " + ChatColor.RED + honorCount + 
							ChatColor.GREEN + " honors";
				}
				
				if(sender instanceof Player){
					if(Perm.hasPermission((Player) sender, "admin360.stats.others", true)){
						sender.sendMessage(msg);
					}
				}else{
					sender.sendMessage(msg);
				}
				return true;
			}
		}
		return false;
	}
	
}
