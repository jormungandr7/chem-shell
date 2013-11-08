package net.modd.chemistry;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

//Thanks to www.sunjay.ca for element files

public class ChemShell{
	
	public static Cmd[] commandObjects = {
		new CmdHelp(),
		new CmdBalance(),
		new CmdConvert(),
		new CmdSolution(),
		new CmdPredict()
		};
	
	public static void processLine(Scanner input, PrintWriter print){
		ArrayList<String> line = new ArrayList<String>();
		while(input.hasNext()){
			String n = input.next();
			if(n.compareToIgnoreCase("in")*n.compareToIgnoreCase("of")!=0)
				line.add(n);
		}
		int before,after;
		do {
			before = line.size();
			updateLine(line);
			after = line.size();
		} while(after<before);
		String out = "";
		for(String s : line)
			out+=s+" ";
		print.println(out);
	}
	
	private static void updateLine(ArrayList<String> line){
		for(Cmd c : commandObjects){
			c.updateLine(line);
		}
	}
}
