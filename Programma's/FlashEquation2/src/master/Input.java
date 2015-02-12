package master;

import java.io.*;
import java.util.*;

import tree.Main;
import tree.Tree;

public class Input {
	
	private File text;
	private Tree tree;
	private List<HashMap<String, Double>> inputList;
	
	/**
	 * Constructor of class input
	 * This constructor will read the inputExample.txt file and the tree file.
	 */
	public Input() {
		inputList = new ArrayList<HashMap<String, Double>>();
		try {
			text = new File("inputExample.txt");
			readTree();
			readFile1(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Reads a file. This will convert a text file with column values to a list of HashMap<String, Double>
	 * e.g. text file: 3 6 8 -> {(K0, 3), (K1, 6), (Goal, 8)}
	 * @param
	 * 		The file to be read
	 * 
	 */
	private void readFile1(File fin) throws IOException {
		InputStream in = getClass().getResourceAsStream("/inputExample.txt");
		//FileInputStream fis = new FileInputStream(fin);
		
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(in));//fis));
		
		String line = null;
		HashMap<String, Double> doublesLine;
		while ((line = br.readLine()) != null) {
			String[] splitOnSpace = line.split(" ");
			doublesLine = new HashMap<String, Double>();
			
			// add all K elements to hashmap
			for(int i = 0; i < splitOnSpace.length-1; i++) {
				doublesLine.put("K" + i, Double.parseDouble(splitOnSpace[i]));
			}
			// add last element, the goal to hashmap
			doublesLine.put(StringMaster.getNameOfGoalK(), Double.parseDouble(splitOnSpace[splitOnSpace.length-1]));
			inputList.add(doublesLine);
		}
		
		br.close();
	}
	
	/**
	 * Converts a two dimensional ArrayList to a readable HashMap format
	 * @param list
	 * 			The two dimensional list to be converted
	 * @return
	 * 			The resulting HashMap
	 */
	public List<HashMap<String, Double>> convertArrayList(List<List<Double>> list) {
		List<HashMap<String, Double>> result = new ArrayList<HashMap<String, Double>>();
		for(List<Double> inputRow: list) {
			// This HashMap will contain this input values
			HashMap<String, Double> doubleLine = new HashMap<String, Double>();
			// adds all the column values to the HashMap
			for(int i = 0; i < inputRow.size()-1; i++) {
				doubleLine.put("K" + i, inputRow.get(i));
			}
			// adds the goal to the HashMap
			doubleLine.put(StringMaster.getNameOfGoalK(), inputRow.get(inputRow.size()-1));
			// adds this complete input line to the result
			result.add(doubleLine);
		}
		return result;
	}
	
	/**
	 * Reads the tree from file (FILENAME given by tree.MAIN
	 */
	private void readTree() {
		ObjectInputStream ois = null;
		try {
			InputStream in = getClass().getResourceAsStream("/" + Main.getFileNameTree());
			//FileInputStream fis = new FileInputStream(new File(Main.getFileNameTree()));
			ois = new ObjectInputStream(in);//fis);
			
			// read object Tree
			tree = (Tree) ois.readObject();
		} catch (Exception e){
			Main.main(null); // Generates the tree
			e.printStackTrace();
			//readTree(); // try to read the tree again
		} finally {
			try {
				ois.close();
			} catch (Exception e) {
				
			}
		}
	}
	
	/**
	 * 
	 * @return
	 * 		A list containing all input lines
	 */
	public List<HashMap<String, Double>> getList() {
		return inputList;
	}

	/**
	 * @return
	 * 			The tree object
	 */
	public Tree getTree() {
		return tree;
	}
	
}