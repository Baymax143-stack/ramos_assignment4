import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.nio.file.Paths;
import java.io.File;
import java.io.PrintWriter;

import java.util.*;
import java.util.HashSet;
import java.util.Scanner;

public class DuplicateRemover {
	//ArrayList to store contents of the file, if there is any
	private ArrayList<String> contents = new ArrayList<String>();

	//Set to store contents without duplicates of the file
	private HashSet<String> rewrite = new HashSet<String>();
	//Set to store words that are unique within the file
	private HashSet<String> uniqueWords = new HashSet<String>();
	
	/*
	 * Uses a set of strings to eliminate duplicate words 
	 * from the file referenced by dataFile, while storing
	 * unique words in the instance variable uniqueWords
	 */
	public void remove(String dataFile) throws IOException
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
			//Case 1: First instance of the word
			if(!(rewrite.contains(word) || uniqueWords.contains(word))) {
				rewrite.add(word); uniqueWords.add(word);
			}
			//Case 2: Word was considered unique, but we have found its duplicate(s)
			else if(uniqueWords.contains(word)) {
				uniqueWords.remove(word);
			}
		}
		
		//Overwrite old file to remove duplicates
		FileWriter writer = new FileWriter(dataFile, false);
		for(String word : rewrite) {
			writer.write(word);
			writer.write(" ");
		}
		writer.close();
	}

	
	/*
	 * Writes the words contained in uniqueWords to the file
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
				
				//Loop through uniqueWords to write to outputFile
				for(String word : uniqueWords) {
					writer1.write(word);
					writer1.write(" ");
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
				
				//Loop through uniqueWords to write to outputFile
				for(String word : uniqueWords) {
					writer2.write(word);
					writer2.write(" ");
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