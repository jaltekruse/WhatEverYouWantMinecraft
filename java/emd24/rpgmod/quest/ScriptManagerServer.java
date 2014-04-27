package emd24.rpgmod.quest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.apache.commons.lang3.StringUtils;

import net.minecraftforge.common.DimensionManager;

public class ScriptManagerServer {
	static Map<String, String> scripts = new HashMap<String, String>();
	static String filename = "scripts.txt";
	static String delimiter = StringUtils.repeat("!", 80);
	
	public static String getScript(String name) {
		return scripts.get(name);
	}
	
	public static void storeScript(String name, String content) {
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
			writer.println(name);
			writer.println(delimiter);
			writer.print(content);
			writer.println(delimiter);
		}
		
		

		//cleanup
		writer.close();
		
	}
}
