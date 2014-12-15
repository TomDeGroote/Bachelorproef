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
	 * TODO commentaar schrijven
	 */
	public Input() {
		inputList = new ArrayList<HashMap<String, Double>>();
		try {
			text = new File("inputExample.txt"); // TODO maak
			readTree();
			readFile1(text);
		} catch (IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TODO commentaar schrijven
	 * @param fin
	 * @throws IOException
	 */
	private void readFile1(File fin) throws IOException {
		FileInputStream fis = new FileInputStream(fin);
	 
		//Construct BufferedReader from InputStreamReader
		BufferedReader br = new BufferedReader(new InputStreamReader(fis));
	 
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
			doublesLine.put(Master.getNameOfGoalK(), Double.parseDouble(splitOnSpace[splitOnSpace.length-1]));
			inputList.add(doublesLine);
		}
		
		br.close();
	}
	
	/**
	 * Reads the tree from file (FILENAME given by tree.MAIN
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	private void readTree() throws IOException, ClassNotFoundException {
		FileInputStream fis = new FileInputStream(new File(Main.getFileNameTree()));
		ObjectInputStream ois = new ObjectInputStream(fis);
		
		// read object Tree
		tree = (Tree) ois.readObject();
		ois.close();
	}
	
	/**
	 * 
	 * @return
	 * 		TODO commentaar schrijven
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