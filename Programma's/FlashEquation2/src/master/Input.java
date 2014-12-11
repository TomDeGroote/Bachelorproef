package master;

import java.io.*;
import java.util.*;

public class Input {
	
	private File text;
	private List<List<Double>> inputList;
	
	public Input() {
		inputList = new ArrayList<List<Double>>();
		try {
			text = new File("src/inputExample.txt");
			readFile1(text);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void readFile1(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
	 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
		String line = null;
		ArrayList<Double> doublesLine;
		while ((line = br.readLine()) != null) {
			String[] test = line.split(" ");
			doublesLine = new ArrayList<Double>();
			for(String t : test){
				doublesLine.add(Double.parseDouble(t));
			}
			inputList.add(doublesLine);
		}
		
	 
		br.close();
	}
	
	public List<List<Double>> getList() {
		return inputList;
	}
	
}