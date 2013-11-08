package net.modd.chemistry;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class ChemFiles{
	//File Information
	private static String elementAddress = "static/periodic_table.csv";
	
	//Element Information
	public static ArrayList<Integer> EleNumbers = new ArrayList<Integer>();
	public static ArrayList<Double> EleMasses = new ArrayList<Double>();
	public static ArrayList<String> EleNames = new ArrayList<String>();
	public static ArrayList<String> EleAbbrevs = new ArrayList<String>();
	
	//Load Information
	public static void loadFiles() throws FileNotFoundException{
		//Elements
		Scanner scan = new Scanner(new File(elementAddress));
		int count = 0;//Keep track of which arraylist to add to
		while(scan.hasNextLine()){
			Scanner line = new Scanner(scan.nextLine());
			line.useDelimiter(",");
			while(line.hasNext()){
				switch(count){
				case 0:
					EleNumbers.add(Integer.parseInt(line.next()));
					break;
				case 1:
					EleMasses.add(Double.parseDouble(line.next()));
					break;
				case 2:
					EleNames.add(line.next());
					break;
				case 3:
					EleAbbrevs.add(line.next());
					break;
				}
				count++;
				count%=4;
			}
		}
	}
}