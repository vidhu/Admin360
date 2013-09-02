package uk.co.vidhucraft.Admin360;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class ConfigurationManager {
	Admin360 plugin;
	
	//Config Files
	File configFile;
	FileConfiguration config;
	File honorFile;
	FileConfiguration honor;
	/**
	 * Creates a new instance of a Configuration manager and loads all the configuration files
	 * @param plugin The plugin this class belongs to
	 */
	public ConfigurationManager(Admin360 plugin){
		this.plugin = plugin;
		try {
			loadConfigurationFiles();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Loads all the configuration files
	 * <b>This is already called when in this class's constructor</b>
	 * @throws Exception
	 */
	public void loadConfigurationFiles() throws Exception{
		//Initiate data Files
		configFile = new File(plugin.getDataFolder(), "config.yml");
		honorFile = new File(plugin.getDataFolder(), "honor.yml");	
		
		//Check if data files exist
		if(!configFile.exists()){
			configFile.getParentFile().mkdirs();
			writeConfigFile(plugin.getResource("config.yml"), configFile);
		}
		if(!honorFile.exists()){
			honorFile.getParentFile().mkdirs();
			writeConfigFile(plugin.getResource("honor.yml"), honorFile);
		}
		
		
		//Load configuration Files form datafiles
		config = new YamlConfiguration();
		config.load(configFile);
		honor = new YamlConfiguration();
		honor.load(honorFile);
	}
	
	private void writeConfigFile(InputStream in, File file) {
	    try {
	        OutputStream out = new FileOutputStream(file);
	        byte[] buf = new byte[1024];
	        int len;
	        while((len=in.read(buf))>0){
	            out.write(buf,0,len);
	        }
	        out.close();
	        in.close();
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}

	/**
	 * Saves any modification done to any of the configuration files into its corresponding <i>yml</i> file
	 */
	public void saveAllConfig(){
		try {
			config.save(configFile);
			honor.save(honorFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
