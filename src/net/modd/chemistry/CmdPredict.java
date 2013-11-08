package net.modd.chemistry;

import java.util.ArrayList;
import java.util.Scanner;

public class CmdPredict implements Cmd{

	public void updateLine(ArrayList<String> line){
		for(int i = 0; i < line.size(); i++){
			if(line.get(i).compareToIgnoreCase("excess")==0){
				line.add(i+1,"");
			}
		}
		if(line.get(0).compareToIgnoreCase("predict")!=0)
			return;
		int equals = 0;
		for(int i = 1; i < line.size(); i++){
			if(line.get(i).compareToIgnoreCase("=")==0)
				equals = i;
		}
		int reactants = 0;
		String reaction = "";
		for(int i = 3; i < equals; i+=4){
			reaction += (" + "+line.get(i));
			reactants++;
		}
		reaction = reaction.substring(3,reaction.length());
		reaction += " = ";
		for(int i = equals+1; i < line.size(); i+=2){
			reaction += line.get(i)+" + ";
			reactants++;
		}
		reaction = reaction.substring(0,reaction.length()-3);
		String c = (new CmdBalance()).execute(reaction,false);
		c = c.substring(1,c.length()-1);
		Scanner s = new Scanner(c);
		double[] coefs = new double[reactants];
		s.useDelimiter(", ");
		for(int i = 0; i < reactants; i++)
			coefs[i] = s.nextDouble();
		double lowestValue = Double.MAX_VALUE;
		Scanner sr = new Scanner(reaction);
		int count = -1;
		for(int i = 1; i < equals; i+=4){
			try{
			String r = line.get(i)+" "+line.get(i+1)+" "+line.get(i+2);
			double amount = CmdConvert.toMoles(new Scanner(r));
			count++;
			sr.next();
			sr.next();
			amount = amount/coefs[count];
			if (amount < lowestValue)
				lowestValue = amount;
			}catch(Exception e){}
		}
		while(line.size()>equals)
			line.remove(equals);
		line.add("yields");
		while(count<reactants){
			count++;
			if(!sr.hasNext())
				break;
			String n = sr.next();
			if(sr.hasNext())
				sr.next();
			double out = (CmdConvert.gramsPerMole(n)*(coefs[count]*lowestValue));
			out = (Math.round(1000.0*out))/1000.0;
			line.add(out+" grams "+n+" + ");
		}
		line.remove(0);
		line.set(line.size()-1,line.get(line.size()-1).substring(0,line.get(line.size()-1).length()-3));
	}
	
}
