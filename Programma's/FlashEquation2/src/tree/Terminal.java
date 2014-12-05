package tree;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the terminal symbols.
 * @author Jeroen & Tom
 *
 */
public class Terminal extends Symbol {

	private static List<Double> columnValues = new  ArrayList<Double>();
	
	public Terminal(String terminal) {
		super.representation = terminal;
		super.terminal = true;
	}
	
	/**
	 * @param columnValue
	 * 		The column values
	 */
	public static void setColumnValues(List<Double> columnValue) {
		Terminal.columnValues = columnValue;
	}
	
	/**
	 * @return
	 * 		{K1, K2, ..., KN}
	 */
	public static List<Double> getColumnValue() {
		return columnValues;
	}
	
	/**
	 * @return
	 * 		{1, 2, ..., 10}
	 */
	public List<Double> getConstants() {
		List<Double> constants = new ArrayList<Double>();
		for(double i = 1; i < 11; i++) {
			constants.add(i);
		}
		return constants;
	}
	
	
	
	
}
