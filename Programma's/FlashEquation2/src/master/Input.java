package master;

import java.io.*;
import java.util.*;

import tree.Main;
import tree.Tree;

public class Input {
	
	private File text;
	private Tree tree;
	private List<List<Double>> inputList;
	
	public Input() {
		inputList = new ArrayList<List<Double>>();
		try {
			text = new File("src/inputExample.txt");
			readFile1(text);
			readTree();
		} catch (IOException e) {
			e.printStackTrace();
		} catch(ClassNotFoundException e) {
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
	 */
	public List<List<Double>> getList() {
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