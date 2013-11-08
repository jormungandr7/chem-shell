package net.modd.chemistry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import Jama.Matrix;

public class CmdBalance implements Cmd{
	
	public void updateLine(ArrayList<String> line){
		if(line.get(0).compareToIgnoreCase("balance")==0){
			String eq = "";
			for(int i = 1; i < line.size(); i++)
				eq+=" "+line.get(i);
			eq = eq.substring(1,eq.length());
			eq = execute(eq,true);
			line.clear();
			line.add(eq);
		}
	}
	
	public String execute(String reaction, boolean refined){
		
		String[] compounds = split(reaction);
		reaction = CmdConvert.fixParens(reaction);
		
		double[][] d = getMatrix(reaction);
		double[][] coefs = new double[d.length][d[0].length-1];
		double[][] sols = new double[d.length][1];
		for(int j = 0; j < d.length; j++){
			for(int i = 0; i < d[j].length-1; i++){
				coefs[j][i]=d[j][i];
			}
			sols[j][0]=d[j][d[j].length-1];
		}
		String balanced = "";
		Matrix m = new Matrix(coefs);
		Matrix out = m.solve(new Matrix(sols));
		double[] solution = new double[compounds.length-1];
		solution[0]=1;
		for(int i = 0; i < out.getArray().length; i++)
			solution[i+1]=out.getArray()[i][0];
		fix(solution);
		if(!refined)
			return Arrays.toString(solution);
		balanced+=(((int)solution[0]==1?"":(int)solution[0])+compounds[0]);
		int s;
		for(s = 1; s < compounds.length; s++){
			if(compounds[s]==null){
				balanced+=(" = ");
				break;
			}
			else
				balanced+=(" + ");
			balanced+=(((int)solution[s]==1?"":(int)solution[s])+compounds[s]);
		}
		s++;
		for(s=s+0; s < compounds.length; s++){
			balanced+=(((int)solution[s-1]==1?"":(int)solution[s-1])+compounds[s]);
			if(s!=compounds.length-1)
				balanced+=(" + ");
		}
		return balanced;
	}
	
	private static int atomsIn(String ele, String comp){
		
		int count = 0;
		/*
		Error is in here!!!!!!
		*/
		
		for(int i = 0; i < comp.length(); i++){
			if(comp.charAt(i)==ele.charAt(0)){
				if(ele.length()==1&&i+1<comp.length()&&Character.isLowerCase(comp.charAt(i+1))){
					continue;
				}
				if(ele.length()==2&&(i+1<comp.length()&&Character.isUpperCase(comp.charAt(i+1)))){
					continue;
				}
				if(ele.length()==2&&i+1==comp.length())
					continue;
				if(ele.length()==2&&i+1<comp.length()&&comp.charAt(i+1)!=ele.charAt(1)){
					i++;
					continue;
				}
				if(ele.length()==2&&i+1<comp.length()&&comp.charAt(i+1)==ele.charAt(1)){
					i++;
				}
				
				int add = 1;
				String num = "";
				i++;
				while(i<comp.length()&&Character.isDigit(comp.charAt(i))){
					num+=comp.charAt(i);
					i++;
				}
				if(num.length()>0)
					add = Integer.parseInt(num);
				i--;
				count+=add;
			}
		}
		
		return count;
	}
	
	private static String[] elements(String str){
		if(str.charAt(0)==' ')
			str = str.substring(1,str.length());
		str += " ";
		ArrayList<String> elements = new ArrayList<String>();
		String recent = "";
		for(int n = 0; n < str.length(); n++){
			if(Character.isDigit(str.charAt(n))||Character.isWhitespace(str.charAt(n))){
				if(!elements.contains(recent)&&(recent.compareTo("+")*recent.compareTo("=")*recent.compareTo(" ")!=0))
					elements.add(recent);
				recent = "";
			} else if (Character.isUpperCase(str.charAt(n))){
				if(!elements.contains(recent)&&(recent.compareTo("+")*recent.compareTo("=")*recent.compareTo(" ")!=0))
					elements.add(recent);
				recent = "";
				recent+=str.charAt(n);
			} else {
				recent += str.charAt(n);
			}
		}
		
		elements.remove("");
		String[] out = new String[elements.size()];
		for(int n = 0; n < out.length; n++)
			out[n] = elements.get(n);
		return out;
		
	}
	
	
	private static String[] split(String str){
				Scanner input = new Scanner(str);
				ArrayList<String> reagents = new ArrayList<String>();
				ArrayList<String> products = new ArrayList<String>();
				boolean flip = false;
				while(input.hasNext()){
					String s = input.next();
					if(s.compareTo("+")==0)
						continue;
					if(s.compareTo("=")==0){
						flip = true;
						continue;
					}
					if(flip)
						products.add(s);
					else
						reagents.add(s);
				}
				while(input.hasNext()){
					products.add(input.next());
				}
				String[] out = new String[products.size()+reagents.size()+1];
				int n;
				for(n = 0; n < reagents.size(); n++){
					out[n] = reagents.get(n);
				}
				out[n] = null;
				n++;
				int m;
				for(m = 0; m < products.size(); m++){
					out[n]=products.get(m);
					n++;
				}
				return out;	
	}
	
	
	private static double[][] getMatrix(String reaction){
		String[] compounds = split(reaction);
		String[] elements = elements(reaction);
		double[][] out = new double[elements.length][compounds.length-1];
		for(int e = 0; e < elements.length; e++){
			int offset = 0;
			for(int c = 1; c < compounds.length; c++){
				if(compounds[c]==null){
					offset = 1;
				}else{
					out[e][c-(1+offset)]=(offset==1?-1:1)*atomsIn(elements[e],compounds[c]);
				}
			}
			out[e][compounds.length-2]=-atomsIn(elements[e],compounds[0]);
		}
		return out;
	}
	
	private static void fix(double[] d){
		for(int n = 1; n < 1000; n++){
			boolean check = true;
			for(double k : d){
				if((k*n+0.01)%1.0>0.02)
					check = false;
			}
			if(check){
				for(int i = 0; i < d.length; i++){
					d[i]*=n;
					d[i]+=0.0000001;
				}
				return;
			}
		}
	}
}
