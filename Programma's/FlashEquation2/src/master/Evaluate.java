package master;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import tree.Equation;

public abstract class Evaluate {

	public List<Equation> bufferSolutions = new ArrayList<Equation>();
	
	public abstract Collection<? extends Equation> evaluate(Master master);
	
	/**
	 * Adds an example to the current example list
	 * @param Ks
	 * 		The example to be added
	 */
	public abstract void addExample(Double[] Ks);
	
}
