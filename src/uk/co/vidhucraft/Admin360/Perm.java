package uk.co.vidhucraft.Admin360;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public enum Perm {
	RespondToRequest ("Admin360.aid");
	
	private String permissionNode;
	private Perm(String permissionNode){
		this.permissionNode = permissionNode;
	}
	
	public String getNode(){
		return this.permissionNode;
	}
	
	public static boolean hasPermission(Player player, String permission, Boolean sendmsg){
		if(!player.hasPermission(permission)){
			if(sendmsg)
				player.sendMessage(ChatColor.RED + "Sorry, you need the permission: " + ChatColor.BLUE + permission);
			return false;
		}
		return true;
	}
}
