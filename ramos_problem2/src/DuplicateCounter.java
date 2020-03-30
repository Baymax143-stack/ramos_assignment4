import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Paths;
import java.io.File;
import java.io.PrintWriter;

import java.util.*;
import java.util.Map.Entry;

public class DuplicateCounter {
	//ArrayList to store contents of the file, if there is any, and results of contents after modification
	private ArrayList<String> contents = new ArrayList<String>();
	
	//Map to store words that are unique within the file
	private HashMap<String, Integer> wordCounter = new HashMap<String, Integer>();
		
	/*
	 * Uses a set of strings to eliminate duplicate words 
	 * from the file referenced by dataFile, while storing
	 * unique words in the instance variable uniqueWords
	 */
	public void count(String dataFile) throws IOException
	{
		//Create file to handle dataFile
		File f = new File(dataFile);
			
		//Call method to check if file exists
		if(f.exists()) {
			//If exists and can be read, try creating scanner object 
			//and read in data, transferring into content variables
			Scanner in = null;
				
			if(f.exists() && f.canRead()) {
				try {
					in = new Scanner(Paths.get(dataFile));
						
					//Store all contents in separate ArrayList for analyzing
					while(in.hasNext()) {
						String text = in.next();
						text = text.replaceAll("\\W", "").toLowerCase();
							
						contents.add(text);
					}
				}
				//Otherwise, if it can't read file, throw FileNotFoundException
				catch(FileNotFoundException fe) {
					System.out.println("This file, unfortunately, cannot be read.");
					System.exit(1);
				}
				finally {
					if(in != null) {in.close();}
				}
			}
			//Check to see if it is a directory
			else if (f.isDirectory()){
				try {
					//Get path of file
					System.out.println("File " + f.getCanonicalPath() + " is a directory.");
				}
				//If this fails, throw IOEException
				catch (IOException ioe) {
					System.out.println("Because this file is a directory, the task cannot be completed.");
					System.exit(1);
				}
			}
		}
		else {
			System.out.println("The file you have asked for does not exist.");
			return;
		}
			
		//Separate words accordingly in contents
		for(String word : contents) {
		//Case 1: New Word
			if(!(wordCounter.containsKey(word))) {
				wordCounter.put(word, 1);
			}
			//Case 2: Word already exists in the map
			else if(wordCounter.containsKey(word)) {
				int count = wordCounter.get(word);
				count++;
				wordCounter.replace(word, count);
			}
		}
	}

		
	/*
	 * Writes the count of each word to the file
	 * pointed to by outputFile; if it already exists, it will
	 * be overwritten - otherwise, created if not
	 */
	public void write(String outputFile) throws IOException
	{
		File f = new File(outputFile);
		FileWriter writer1 = null;
		PrintWriter writer2 = null;
			
		//Check to see if outputFile exists
		if(f.exists()) {
			try {
				writer1 = new FileWriter(outputFile, false);
					
				//Loop through wordCounter to write to outputFile
				Iterator<Entry<String, Integer>> it = wordCounter.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry m = (Map.Entry)it.next();
			        String key = (String)m.getKey();
			        int value = (int) m.getValue();
			        
			        String space = " ";
			        writer1.write(key + space + value);
			        writer1.write("\n");
				    }
				}
			catch (IOException ioe) {
				System.out.println("The output file could not be overwritten.");
				System.exit(1);
			}
			finally {
				if(writer1 != null) {writer1.close();}
			}
		}
		//If not, create file
		else {
			try {
				writer2 = new PrintWriter(outputFile);
					
				//Loop through wordCounter to write to outputFile
				Iterator<Entry<String, Integer>> it = wordCounter.entrySet().iterator();
				while(it.hasNext()) {
					Map.Entry m = (Map.Entry)it.next();
			        String key = (String)m.getKey();
			        int value = (int)m.getValue();
			        
			        String space = " ";
			        writer2.write(key + space + value);
			        writer2.write("\n");

				}
				writer2.flush();
			}
			catch (FileNotFoundException fe) {
				System.out.println("The output file could not be created.");
				System.exit(1);
			}
			finally {
				if(writer2 != null) {writer2.close();}
			}
		}	
	}
}
