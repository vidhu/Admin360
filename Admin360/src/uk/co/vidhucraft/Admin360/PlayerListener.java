package uk.co.vidhucraft.Admin360;

import java.util.HashMap;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerListener implements Listener {
	Admin360 plugin;
	
	public PlayerListener(JavaPlugin plugin){
		this.plugin = (Admin360) plugin;
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerJoin(PlayerJoinEvent playerJoinEvent){
		Player player = playerJoinEvent.getPlayer();
		loadLoggedinUsers(new Player[]{player});
	}
	
	@EventHandler(priority=EventPriority.MONITOR)
	public void onPlayerQuit(PlayerQuitEvent playerQuitEvent){
		Player player = playerQuitEvent.getPlayer();
		if(player.hasPermission(Perm.RespondToRequest.getNode())){
			try{
			Admin360.adminsOnline.remove(playerQuitEvent.getPlayer().getName());
			}catch(Exception ex){ }
		}

		plugin.playerRequestManager.removePlayerFromRequestQueue(player.getName());
	}

	public void loadLoggedinUsers(Player[] playerarray){
		for (Player player : playerarray) {
			if(player.hasPermission(Perm.RespondToRequest.getNode())){
				Admin360.adminsOnline.add(player.getName());
			}
		}
	}
	
}
