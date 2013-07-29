package uk.co.vidhucraft.Admin360;

import java.util.HashMap;
import java.util.Set;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class AdminHonor extends BukkitRunnable{
	Admin360 plugin;
	HashMap<String, Integer> honorStats = new HashMap<>();
	
	/**
	 * Class constructor Loads any existing stats into memory form honor.yml
	 * @param plugin The plugin this class is part of
	 */
	public AdminHonor(JavaPlugin plugin){
		this.plugin = (Admin360) plugin;
		
		//load honor count when class initiated
		FileConfiguration honors = this.plugin.configurationManager.honor;
		if(honors.isSet("honor")){
			Set<String> admins = honors.getConfigurationSection("honor").getValues(false).keySet();
			
			for(String admin : admins){
				honorStats.put(admin, (Integer) honors.get("honor." + admin + ".total"));
			}
		}

		//Schedule adminstats save event
		this.runTaskTimer(plugin, 12000, 12000);
	}
	
	/**
	 * Honors an admin by adding points to his honor collection
	 * @param adminName Admin's name to honor
	 */
	public void honorAdmin(String adminName){
		if(honorStats.containsKey(adminName)){
			honorStats.put(adminName, honorStats.get(adminName)+1);
		}else{
			honorStats.put(adminName, 1);
		}
	}
	
	/**
	 * Save all the admin honors that have been updated during runtime into honor.yml file
	 */
	public void saveStats(){
		FileConfiguration tmp = plugin.configurationManager.honor;
		
		System.out.println("[Admin360] Saving admin honors");
		for(String adminName : honorStats.keySet()){
			tmp.set("honor." + adminName + ".total", honorStats.get(adminName));
		}
		plugin.configurationManager.saveAllConfig();
		System.out.println("[Admin360] Saving successful");
	}

	/**
	 * Returns the number of honors at the moment of the specified admin
	 * @param adminName The admin's name to query
	 * @return Total count of honors
	 */
	public int getStats(String adminName){
		if(honorStats.containsKey(adminName)){
			return honorStats.get(adminName);
		}else{
			return 0;
		}
	}
	
	/**
	 * A scheduler to save the adminstats periodically
	 */
	@Override
	public void run() {
		this.saveStats();
	}
}
