package net.modd.chemistry;

import java.util.ArrayList;

public class CmdHelp implements Cmd{

	public void updateLine(ArrayList<String> line){
		if(line.get(0).compareToIgnoreCase("help")==0)
			execute(line);
	}
	
	private void execute(ArrayList<String> line){
		line.clear();
		String out = "ChemShell Help <BR> Primary Commands: <BR>"
			+ "&nbsp	Moles [X] units [Y] <BR>"
			+ "&nbsp	Grams [X] moles [Y] <BR>"
			+ "&nbsp	GramsPerMole [X] <BR>"
			+ "&nbsp	Balance [equation] <BR>"
			+ "&nbsp	Predict [reagents and amounts] = [products]"
			+ "For instance: <BR> &nbsp \"Moles 2 grams H2SO4\" <BR>"
			+ "&nbsp\"Balance H2 + N2 = NH3\" <BR>"
			+ "&nbsp\"Predict 10 g CH4 + 10 g O2 = H2O + CO2\" <BR>"
			+ "Note that ChemShell currently supports only chemical formulas.<BR>"
			;
		line.add(out);
	}
	
}
