package master;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import master.normal.ObjectMaster;
import tree.Main;
import tree.Tree;

public class Input {
	
	private File text;
	private Tree tree;
	private List<HashMap<String, Double>> inputList;
	private List<Double[]> primitiveInput;
	
	/**
	 * Constructor of class input
	 * This constructor will read the inputExample.txt file and the tree file.
	 */
	public Input() {
		inputList = new ArrayList<HashMap<String, Double>>();
		primitiveInput = new ArrayList<Double[]>();
		try {
			text = new File("inputExample.txt");
			readTree();
			readFile1(text);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Convert a HashMap to Double[]
	 * @param input
	 * 		The hashMap
	 * @return
	 * 		The Double
	 */
	public static Double[] covertToHashMap(HashMap<String, Double> input) {
		Double[] result = new Double[input.size()];
		for(String K : input.keySet()) {
			if(K.equals(Master.getNameOfGoalK())) {
				result[result.length-1] = input.get(K);
			} else {
				result[(int) Double.parseDouble(K.substring(1))] = input.get(K);
			}
		}
		return result;
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
			Double[] primLine = new Double[splitOnSpace.length];
			// add all K elements to hashmap
			for(int i = 0; i < splitOnSpace.length-1; i++) {
				double value = Double.parseDouble(splitOnSpace[i]);
				doublesLine.put("K" + i, value);
				primLine[i] = value;
			}
			// add last element, the goal to hashmap
			double value = Double.parseDouble(splitOnSpace[splitOnSpace.length-1]);
			doublesLine.put(ObjectMaster.getNameOfGoalK(), value);
			inputList.add(doublesLine);
			
			primLine[primLine.length-1] = value;
			primitiveInput.add(primLine);
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
			doubleLine.put(ObjectMaster.getNameOfGoalK(), inputRow.get(inputRow.size()-1));
			// adds this complete input line to the result
			result.add(doubleLine);
		}
		return result;
	}
	
	/**
	 * Converts a two dimensional ArrayList to a readable primitive Doulbe[] format
	 * @param list
	 * 			The two dimensional list to be converted
	 * @return
	 * 			The resulting Double []
	 */
	public List<Double[]> convertArrayListToPrim(List<List<Double>> list) {
		List<Double[]> result = new ArrayList<Double[]>();
		for(List<Double> inputRow: list) {
			// This will contain this input values
			Double[] row = new Double[inputRow.size()];
			// adds all the column values to the HashMap
			for(int i = 0; i < inputRow.size(); i++) {
				row[i] = inputRow.get(i);
			}
			result.add(row);
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
	 * 
	 * @return
	 * 		A list containing all input lines
	 */
	public List<Double[]> getPrimitiveList() {
		return primitiveInput;
	}

	/**
	 * @return
	 * 			The tree object
	 */
	public Tree getTree() {
		return tree;
	}
	
}