package Task9.main.java.com.theo;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
public class PDAPrefix {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();//throw away number of lines
        PDA m = PDAParser.parse(scanner);
        System.out.println("Case #1:\n" + pdaPrefix(m));
        scanner.close();
    }

    public static PDA pdaPrefix(PDA m) {
        Set<PDATransition> prefixPDATransitions = new HashSet<>(m.getTransitions());
        Set<State> prefixPDAStates = new HashSet<>(m.getStates());
        Set<Character> prefixPDAAlphabet = new HashSet<>(m.getAlphabet());
        for (PDATransition transition : m.getTransitions()) {
            final State newStart = new State(transition.getStart().getName() + "_E");
            final State newEnd = new State(transition.getEnd().getName() + "_E");
            prefixPDAStates.add(newStart);
            prefixPDAStates.add(newEnd);
            final PDATransition epsilonTransition = new PDATransition(newStart, Util.EPSILON, transition.getPopSymbol(), newEnd, transition.getPushSymbols());
            prefixPDATransitions.add(epsilonTransition);
        }
        for (State state : m.getStates()) {
            for (State epsilonState : prefixPDAStates) {
                if(epsilonState.getName().equals(state.getName()+"_E")) {
                    for(char stackSymbol : m.getStackAlphabet()) {
                        prefixPDATransitions.add(new PDATransition(state, Util.EPSILON, stackSymbol, epsilonState, stackSymbol+""));
                    }
                }
            }
        }
        return new PDA(prefixPDAStates, prefixPDATransitions, prefixPDAAlphabet, m.getStartState(), m.getStackAlphabet(), m.getStartSymbol());
    }
}