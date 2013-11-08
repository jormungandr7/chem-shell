package net.modd.chemistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class CmdConvert implements Cmd{
	
	public void updateLine(ArrayList<String> line){
		for(int i = 0; i < line.size(); i++){
			String s = line.get(i);
			if(s.compareToIgnoreCase("gramspermole")==0){
				double gpm = gramsPerMole(line.get(1));
				line.clear();
				line.add(Double.toString(gpm));
				line.add("grams per mole");
			}
			if(s.compareToIgnoreCase("moles")==0){
				try{
					String query = "";
					query += Double.toString(Double.parseDouble(line.get(i+1)))+" ";
					query += line.get(i+2)+" ";
					query += line.get(i+3)+" ";
					line.set(i,Double.toString(toMoles(new Scanner(query))));
					line.set(i+1,"moles");
					line.remove(i+2);
					line.remove(i+2);
				} catch(Exception e){}
			}
			try{
				String query = "";
				query += line.get(i+1)+" "+line.get(i+2)+" "+line.get(i+3);
				String unitOut = line.get(i);
				double out = convert(new Scanner(query),line.get(i));
				if(!Double.isNaN(out)){
					line.set(i,Double.toString(out));
					line.set(i+1,unitOut);
					line.remove(i+2);
					line.remove(i+2);
				}
			} catch(Exception e){}
		}
	}
	
	//Parse Chemical Formula from name
	public static String chemFormula(String name){
		
		return name;
		
	}
	
	public static String fixParens(String formula){
		
		/*
		 * NOTE:
		 * THIS HAS TO WORK FOR A WHOLE EQUATION TOO
		 */
		
		String newFormula = "";
		for(int i = 0; i < formula.length(); i++){
			if(formula.charAt(i)=='('){
				int k = i;
				while(formula.charAt(i)!=')')
					i++;
				String parens = formula.substring(k+1,i);
				String multiply = "";
				i++;
				while(i<formula.length()&&Character.isDigit(formula.charAt(i))){
					multiply += formula.charAt(i);
					i++;
				}
				for(int j = 0; j < parens.length(); j++){
					newFormula+=parens.charAt(j);
					if(j+1<parens.length()&&Character.isLowerCase(parens.charAt(j+1))){
						j++;
						newFormula+=parens.charAt(j);
					}
					if(j+1<parens.length()&&Character.isDigit(parens.charAt(j+1))){
						j++;
						String number = "";
						while(j<parens.length()&&Character.isDigit(parens.charAt(j))){
							number+=parens.charAt(j);
							j++;
						}
						newFormula+=Integer.toString((Integer.parseInt(multiply)*Integer.parseInt(number)));
						j--;
					} else {
						newFormula+=multiply;
					}
				}
				i--;
			}
			else
				newFormula+=formula.charAt(i);
		}
		//System.out.println(newFormula);
		return newFormula;
	}
	
	//Calculate molecular/formula unit weight
	public static double gramsPerMole(String formula){
		
		formula = fixParens(formula);
		
		double total = 0;
		String abbrev = "";
		int coefficient = 1;
		int multiplier = 1;
		int i = 0;
		String number = "";
		if(formula.charAt(0)==' ')
			formula = formula.substring(1,formula.length());
		while(Character.isDigit(formula.charAt(i))){//Handle leading coefficients
			number+=formula.charAt(i);
			i++;
		}
		if(number.length()>0)
			coefficient = Integer.parseInt(number);
		formula+="X";//Nonexistent element to terminate.
		for(i=i+0;i < formula.length(); i++){
			char c = formula.charAt(i);
			if(Character.isUpperCase(c)){
				total+=atomicMass(abbrev)*multiplier;
				multiplier = 1;
				abbrev = ""+c;
			}
			else if(Character.isDigit(c)){
				number = "";
				while(Character.isDigit(formula.charAt(i))){
						number+=formula.charAt(i);
						i++;
				}
				i--;
				multiplier = Integer.parseInt(number);
			}else{
				abbrev+=c;
			}
		}
		return total*coefficient;
	}
	
	//Get mass of an element
	public static double atomicMass(String abbreviation){
		if(abbreviation.length() == 0)
			return 0;
		if(abbreviation.charAt(0)==' ')
			abbreviation = abbreviation.substring(1,abbreviation.length());
		int index = ChemFiles.EleAbbrevs.indexOf((abbreviation));
		if(index != -1){
			return ChemFiles.EleMasses.get(index);
		}
		return 0;
	}
	
	//Units for conversions
	public static final String[] units = {//unit, abbrev., grams per 1 unit
		"kilograms","kg","1000",
		"milligrams","mg","0.001",
		"grams", "g", "1"
	};
	
	//Unit Conversion: Mass
	public static double convertMass(String unitIn, String unitOut, double numberIn){
		try{
			unitOut = unitOut.toLowerCase();
			unitIn = unitIn.toLowerCase();
			if(unitIn.charAt(unitIn.length()-1)!='s'&&unitIn.length()>3)
				unitIn+='s';
			if(unitOut.charAt(unitOut.length()-1)!='s'&&unitOut.length()>3)
				unitOut+='s';
			if(!Arrays.asList(units).contains(unitIn))
				throw new Exception();
			if(!Arrays.asList(units).contains(unitOut))
				throw new Exception();
			double out;
			int index ;
			index = ((Arrays.asList(units).indexOf(unitIn))/3)*3+2;
			out = numberIn*(Double.parseDouble(units[index]));
			index = ((Arrays.asList(units).indexOf(unitOut))/3)*3+2;
			out = out/(Double.parseDouble(units[index]));
			return out;
		} catch(Exception e){
			return Double.NaN;
		}
	}
	
	//To moles
	public static double toMoles(Scanner input){
		try{
			double amount = input.nextDouble();
			String units = input.next();
			String material = input.next();
			if(units.compareToIgnoreCase("g")*units.compareToIgnoreCase("grams")*units.compareToIgnoreCase("gram")!=0)
				amount = convertMass(units,"grams",amount);//Ensure units are grams
			double moles = amount/(gramsPerMole(chemFormula(material)));
			return moles;
		} catch(Exception e){}
		return Double.NaN;
	}

	
	//To grams
	public static double convert(Scanner input, String outunits){
			double amount = input.nextDouble();
			String inunits = input.next();
			String material = input.next();
			if(inunits.compareToIgnoreCase("mole")*inunits.compareToIgnoreCase("moles")!=0){
				return(convertMass(inunits,outunits,amount));
			}
			double moles = amount*(gramsPerMole(chemFormula(material)));
			moles = convertMass("grams",outunits,moles);
			return moles;
	}
}
