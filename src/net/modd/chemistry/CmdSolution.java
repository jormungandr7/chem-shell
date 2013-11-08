package net.modd.chemistry;

import java.util.ArrayList;

public class CmdSolution implements Cmd{

	public void updateLine(ArrayList<String> line){
		for(int i = 0; i < line.size(); i++){
			String s = line.get(i).toLowerCase();
			if(s.contains("liter")||s.compareTo("l")*s.compareTo("ml")==0){
				double volume = Double.parseDouble(line.get(i-1));
				if(s.contains("milliliter")||s.compareTo("ml")==0)
					volume = volume/1000.0;
				Double molarity = Double.parseDouble(line.get(i+1));
				double moles = volume*molarity;
				line.set(i-1,Double.toString(moles));
				line.set(i,"moles");
				line.remove(i+1);
				line.remove(i+1);
			}
		}
	}
}
