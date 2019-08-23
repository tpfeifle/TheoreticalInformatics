package Task3.main.java.com.theo;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

public class AcceptNFA {

    public static Set<State> getTransitiveSuccessors(Set<State> successors, EpsilonNFA n, State state) {
        Set<State> newSuccessors = n.getSuccessors(state, Transition.EPSILON);
        for (State successor : newSuccessors) {
            if (!successors.contains(successor)) {
                successors.add(successor);
                successors.addAll(getTransitiveSuccessors(successors, n, successor));
            }
        }
        return successors;
    }
    public static boolean accept(EpsilonNFA n, String w){
        final char[] wordCharacters = w.toCharArray();
        Set<State> currentStateFront = new HashSet<>();
        currentStateFront.add(n.getStartState());
        currentStateFront.addAll(getTransitiveSuccessors(new HashSet<>(), n, n.getStartState()));
        if (w.length() == 0) {
            for (State resultingState : n.getSuccessors(n.getStartState(), Transition.EPSILON)) {
                if (n.getFinalStates().contains(resultingState)) {
                    return true;
                }
            }
        }
        for (char character : wordCharacters) {
            if (!n.getAlphabet().contains(character)) {
                return false;
            }

            Set<State> nextStateFront = new HashSet<>();
            for(State selectedState : currentStateFront) {

                // copied from EpsilonNFA
                // because n.getSuccessors(selectedState, character) includes epsilon as successors and I don't want that here
                Set<State> result = new HashSet<>();

                for (Transition t : n.getTransitions()){
                    if(t.getStart().equals(selectedState) && (t.getLabel()==character)) {
                        result.add(t.getEnd());
                    }
                }
                //end copied

                nextStateFront.addAll(result);
                if(result.size() > 0) {
                    nextStateFront.addAll(getTransitiveSuccessors(new HashSet<>(), n, selectedState));
                }
            }
            currentStateFront = nextStateFront;
        }

        //all resulting with transitive
        for (State currentState : currentStateFront) {
            currentStateFront.addAll(getTransitiveSuccessors(new HashSet<>(), n, currentState));
        }
        for (State resultingState : currentStateFront) {
            if (n.getFinalStates().contains(resultingState)) {
                return true;
            }
        }
        return false;
    }
    

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        EpsilonNFA e = Parser.parse(scanner);
        String word = scanner.nextLine();
        while(!word.equals("DONE")) {
            System.out.println(word + ": " + accept(e, word));
            word = scanner.nextLine();
        }
        scanner.close();
    }
}
