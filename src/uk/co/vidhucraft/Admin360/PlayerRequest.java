package uk.co.vidhucraft.Admin360;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerRequest {
	Admin360 plugin;

	public PlayerRequest(JavaPlugin plugin){
		this.plugin = (Admin360) plugin;
	}
	
	LinkedList<String> playerRequestQueue = new LinkedList<String>();
	
	/**
	 * Adds a new player in the request queue for an admin to attend
	 * @param name The player's name to add in the queue
	 */
	public void addPlayerInQueue(String name){
		if(isAdminAvailable()){
			if(isPlayerRequestExists(name)){
				msgPlayer(name, ChatColor.RED + "You've already created a request");
				return;
			}
			
			playerRequestQueue.add(name);
			msgPlayer(name, "A request has been sent to an admin");
			msgPlayer(name, "Please wait for your turn.");
			informRequestStatus(name);
			msgAdmins("A new request as been created by " + ChatColor.RED + name);
		}else{
			msgPlayer(name, "Sorry! There arent any admins available at the moment");
		}
	}
	
	/**
	 * Checks if an administrator is online to help a user
	 * @return returns true if an administrator is online
	 */
	public static boolean isAdminAvailable(){
		return Admin360.adminsOnline.getItemCount() > 0;
	}

	/**
	 * Checks if a player has already made a request
	 * @param name The player to check
	 * @return If TRUE a request in the name of this player already exists
	 */
	public boolean isPlayerRequestExists(String name){
		for(String playerName : playerRequestQueue){
			if(playerName.equalsIgnoreCase(name)){
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Informs a player about his request status
	 * @param name
	 * @return
	 */
	public void informRequestStatus(String name){
		int i = 0;
		boolean found = false;
		for(String requestname : playerRequestQueue){
			i++;
			if(requestname.equalsIgnoreCase(name)){
				found = true;
				break;
			}
		}
		if(found){
			msgPlayer(name, "You are currently number "+ ChatColor.RED + i + ChatColor.GREEN +" in the request queue");
		}else{
			msgPlayer(name, "You havent created a request yet.");
		}
	}
	
	/**
	 * Informs number of requests in still pending
	 */
	public void informRequestsPending(String name){
		msgPlayer(name, "There are a total of " + ChatColor.RED + playerRequestQueue.size() + ChatColor.GREEN + " requests still pending");
	}
	
	/**
	 * Send a message to a player
	 * @param name The players name who will recieve the message
	 * @param message The message to send
	 */
	public static void msgPlayer(String name, String message){
		Bukkit.getPlayerExact(name).sendMessage(ChatColor.GREEN + message);
	}
	
	/**
	 * Purge all the requests in the playerRequestQueue
	 */
	public void purageRequests(){
		playerRequestQueue.clear();
		Bukkit.getServer().broadcastMessage(ChatColor.RED + "All admin requests have been purged");
	}

	/**
	 * Removes the next request in queue and teleports 
	 * the specified admin to the player who created the request
	 * @param admin The admin to teleport
	 */
	public void fulfillNextRequest(String name){
		//Sanity check
		if(playerRequestQueue.size() < 1){
			msgPlayer(name, "There arent any request");
			return;
		}
		Player admin = Bukkit.getPlayerExact(name);
		Player player = Bukkit.getPlayerExact(playerRequestQueue.pop());
		admin.teleport(player);
		
		//Honor admin if(admin != admin) incase of Trolls
		if(!admin.getName().equalsIgnoreCase(player.getName()))
			plugin.adminHonorManager.honorAdmin(name);
		
		//Notify
		Bukkit.broadcastMessage(ChatColor.RED + name + ChatColor.GREEN + " is now attending " + ChatColor.RED + player.getName());
		Bukkit.broadcastMessage(ChatColor.GREEN + "There are now " + ChatColor.RED + playerRequestQueue.size() + 
				ChatColor.GREEN + " requests remaining");
	}
	
	/**
	 * Sends a message to all online admins
	 */
	public static void msgAdmins(String msg){
		for(int i=0;i<Admin360.adminsOnline.getItemCount();i++){
			Bukkit.getPlayerExact(Admin360.adminsOnline.getItem(i))
			.sendMessage(ChatColor.GREEN + msg);
		}
	}

	public void removePlayerFromRequestQueue(String name){
		for(int i=0;i<playerRequestQueue.size();i++){
			if(playerRequestQueue.get(i).equalsIgnoreCase(name)){
				playerRequestQueue.remove(i);
				break;
			}
		}
	}

	public void cancelRequest(String name){
		msgPlayer(name, "Your request has been canceled");
		removePlayerFromRequestQueue(name);
	}
}
