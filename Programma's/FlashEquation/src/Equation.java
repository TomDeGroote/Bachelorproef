import java.util.ArrayList;


/**
 * This class represents an equation (i.e. E*E+E)
 *
 */
public class Equation {
	int numberOfNonTerminals = 0; // represents the number of nonterminals in this equation
	ArrayList<Symbol> elements;   // Contains the elements of this equation
	
	/**
	 * 
	 * @param elements
	 * 			The elements for this equation
	 * @param numberOfNonTerminals
	 * 			The number of nonterminals in the equation
	 */
	public Equation(ArrayList<Symbol> elements, int numberOfNonTerminals) {
		this.numberOfNonTerminals = numberOfNonTerminals;
		if(elements == null) {
			// creates the first equation if non was given
			this.elements = new ArrayList<Symbol>();
			this.elements.add(new NonTerminal("E"));
			numberOfNonTerminals++;
		} else {
			this.elements = elements;
		}
	}
	
	/**
	 * Checks if the equation contains nonterminals
	 * @return
	 * 		True if the equation contains nonterminals
	 * 		False if the equation does not contain any nonterminals
	 */
	public boolean containsNonTerminal() {
		if(numberOfNonTerminals == 0)
			return false;
		else 
			return true;
	}
	
	/**
	 * @return
	 * 		The elements of this equation
	 */
	public ArrayList<Symbol> getElements() {
		return elements;
	}
	
	/**
	 * This function expands the current equation to the next level in the three,
	 * 		Thus replacing nonTerminals by the possible replacements
	 * @return
	 * 		The list of equations which are correct expansions of the this equation
	 */
	public ArrayList<Equation> Expand() {
		ArrayList<Equation> expansion = new ArrayList<>();	
		for(int i = 0; i < elements.size(); i++) {
			if(elements.get(i).isNonTerminal()) {
				for(ArrayList<Symbol> uitbreiding : Expansions.getExpansions(((NonTerminal) elements.get(i)).getValue())) {
					ArrayList<Symbol> newElements = new ArrayList<>();
					int nonTerminalCount = 0; // counts the nonterminals in the new expanded equation
					
					// Adds all elements before the replaced element 
					// i.e. equation 1*E*2
					// the second element (E) will be replaced thus this adds 1*
					for(int j = 0; j < i; j++) {
						newElements.add(elements.get(j));
					}
					
					// Adds all elements for one possible expansion
					// i.e. equation 1*E*2
					// the second element (E) will be replaced thus this adds E*E
					// combined with the previous forloop this gives 1*E*E
					for(Symbol s : uitbreiding) {
						newElements.add(s);
						if(s.isNonTerminal()) {
							nonTerminalCount++;
						}
					}
					// Adds all elements for one possible expansion
					// i.e. equation 1*E*2
					// the second element (E) will be replaced thus this adds *2
					// combined with the previous forloop this gives 1*E*E*2
					for(int j = i + 1; j < elements.size(); j++) {
						newElements.add(elements.get(j));
						if(elements.get(j).isNonTerminal()) {
							nonTerminalCount++;
						}
					}
					expansion.add(new Equation(newElements, nonTerminalCount));
				}
				// If there has been a replacement don't search for a new one
				i = elements.size();
			}
		}
		return expansion;
	}
}
