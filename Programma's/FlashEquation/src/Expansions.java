import java.util.ArrayList;

/**
 * This class defines the possible expansions for a given value
 * 
 * Current possibilities
 * E -> E*E
 * E -> E+E
 * E -> E-E
 * E -> E/E
 * E -> 1
 * E -> 2
 * E -> 3
 *
 */
public class Expansions {	
	
	public static ArrayList<ArrayList<Symbol>> getExpansions(String value) {
		ArrayList<ArrayList<Symbol>> expansions = new ArrayList<>();
		
		if(value.equals("E")) {
			// Definition multiplication
			ArrayList<Symbol> multiply = new ArrayList<>();
			multiply.add(new NonTerminal(value));
			multiply.add(new Operand("*"));
			multiply.add(new NonTerminal(value));
			expansions.add(multiply);
			
			// Definition sum
			ArrayList<Symbol> sum = new ArrayList<>();
			sum.add(new NonTerminal(value));
			sum.add(new Operand("+"));
			sum.add(new NonTerminal(value));
			expansions.add(sum);
			
			// Definition subtraction
			ArrayList<Symbol> sub = new ArrayList<>();
			sub.add(new NonTerminal(value));
			sub.add(new Operand("-"));
			sub.add(new NonTerminal(value));
			expansions.add(sub);
			
			// Definition division
			ArrayList<Symbol> div = new ArrayList<>();
			div.add(new NonTerminal(value));
			div.add(new Operand("/"));
			div.add(new NonTerminal(value));
			expansions.add(div);
//			
//			// Definition constant values
//			for(int i = 1; i <= 3; i++) {
//				ArrayList<Symbol> value1 = new ArrayList<>();
//				value1.add(new Terminal(i));
//				expansions.add(value1);
//			}
		}
		
		return expansions;
	}
}
