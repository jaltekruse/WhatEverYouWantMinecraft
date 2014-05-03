package emd24.rpgmod.quest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.apache.commons.lang3.StringUtils;

import sun.org.mozilla.javascript.internal.Context;
import sun.org.mozilla.javascript.internal.ScriptableObject;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

public class ScriptManagerServer {
	//Script Name -> Script Content
	static Map<String, String> scripts = new HashMap<String, String>();
	//A map of Entity active scripts
	static Map<Integer, String> active_scripts = new HashMap<Integer, String>();
	//Name of scripts file
	static String filename = "scripts.txt";
	//Delimiter to use for storing scripts
	static String delimiter = StringUtils.repeat("/", 40);
	//Scripting Engine Stuff
	static ScriptEngineManager factory;
	static ScriptEngine engine;
	static String scriptError;
	
	static boolean initialized = false;
	
	public static void init()
	{
		if(!initialized)
		{
			load();
			initialized = true;
		}
	}
	
	/**
	 * Set's a target script to execute on entityID every tick.
	 * @param entityID entityID of Entity to attach script
	 * @param scriptName the name of the script to be attached
	 */
	public static void setActiveScript(Integer entityID, String scriptName)
	{
		active_scripts.put(entityID, scriptName);
	}
	
	/**
	 * Run entityID's active script, if any.  Called every tick.
	 * @param entity entity to update.
	 */
	public static void runScript(Entity entity)
	{
		//entity.getEntityId()
		Integer entityID = entity.getEntityId();
		init();
		String script_name = active_scripts.get(entityID);
		if(script_name != null)
		{
			ScriptManagerServer.load(); //TODO: Remove
			String script = ScriptManagerServer.scripts.get(script_name);
			if(script != null)
			{
				//Run script
				Object result;
				try {
					//Minecraft.getMinecraft().theWorld.getEntityByID(par1)
					//MinecraftServer.getServer().getEntityWorld().getEntityByID(entityID).posX
					//entity.worldObj
					engine.eval("id = " + entityID);
					result = engine.eval(script);
					if(result != null)
					{
						scriptError = result.toString();
						if(scriptError.equals("false"))
						{
							active_scripts.remove(entityID);
						}
					}
				} catch(ScriptException e) {
					scriptError = e.getLocalizedMessage();
					System.err.println("Error in script '" + script_name + "': " + scriptError);
				}
			}
			else
			{
				System.err.println(" Script name: '" + script_name + "' not found.");
				active_scripts.remove(entityID);
			}
		}
	}
	
	public static String getScript(String name) {
		init();
		return scripts.get(name);
	}
	
	public static void storeScript(String name, String content) {
		init();
		scripts.put(name, content);
	}
		
	/**
	 * Load the scripts from a file
	 */
	public static void load() {
		File file = new File(DimensionManager.getCurrentSaveRootDirectory(), filename);
		Scanner scanner;
		try {
			scanner = new Scanner(file);
		} catch (FileNotFoundException e) {
			return;
		}
		//read in scripts
		scanner.useDelimiter(delimiter);
		while(scanner.hasNext()) {
			String name = scanner.next().trim();
			if(!scanner.hasNext()) {
				System.err.println("Error reading script (missing content) " + name);
				break;
			}
			String content = scanner.next().trim();
			scripts.put(name, content);
		}
		
		//cleanup
		scanner.close();
		
		//Setup JavaScript Engine
		//setup script manager
		factory = new ScriptEngineManager();
		engine = factory.getEngineByName("JavaScript");
	}
	
	/**
	 * Store the scripts into a file.
	 */
	public static void store() {
		PrintWriter writer = null;
		try {
			File file = new File(DimensionManager.getCurrentSaveRootDirectory(), filename);
			writer = new PrintWriter(file);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return;
		}
		//write scripts
		for(Map.Entry<String, String> entry : scripts.entrySet()) {
			String name = entry.getKey();
			String content = entry.getValue();
			writer.print(delimiter);
			writer.print(name);
			writer.println(delimiter);
			writer.println(content);
		}
		
		

		//cleanup
		writer.close();
		
	}
}
