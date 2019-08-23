package Task5.main.java.com.theo;
import java.util.*;

public class Intersection {


    public static DFA intersection(DFA d1, DFA d2) {
        HashMap<Set<State>, State> stateMapping = new HashMap<>();
        HashMap<Set<State>, State> stateMappingFinished = new HashMap<>(); //should actually be a simple list
        State frontierD1 = d1.getStartState();
        State frontierD2 = d2.getStartState();
        State newStartState = new State();
        Set<State> frontierSet = new HashSet<>();
        frontierSet.add(frontierD2);
        frontierSet.add(frontierD1);
        stateMapping.put(frontierSet, newStartState);
        Set<State> dfaStates = new HashSet<>();
        dfaStates.add(newStartState);
        Set<State> dfaFinalStates = new HashSet<>();
        Set<Transition> dfaTransitions = new HashSet<>();

        boolean finished = false;
        while(!finished) {
            //find unfinished state in hashmap
            boolean found = false;
            for (Set<State> potential : stateMapping.keySet()) {
                if(!stateMappingFinished.containsKey(potential)) {
                    final Iterator<State> iterator = potential.iterator();
                    State state1 = iterator.next();
                    State state2 = iterator.next();
                    if(d1.getStates().contains(state1)) {
                        frontierD1 = state1;
                        frontierD2 = state2;
                    } else {
                        frontierD1 = state2;
                        frontierD2 = state1;
                    }
                    found = true;
                }
            }
            if(!found) {
                finished = true;
                continue;
            }
            Set<State> hashSetFrontier = new HashSet<>();
            hashSetFrontier.add(frontierD1);
            hashSetFrontier.add(frontierD2);

            for (char character : d1.getAlphabet()) {
                Set<State> successors = getNewFrontier(d1, d2, frontierD1, frontierD2, character);

                if(!stateMapping.containsKey(successors)) {
                    final State newDfaState = new State();
                    stateMapping.put(successors, newDfaState);
                    dfaStates.add(newDfaState);
                }

                dfaTransitions.add(new Transition(stateMapping.get(hashSetFrontier), stateMapping.get(successors), character));

                if(d1.getFinalStates().contains(frontierD1) && d2.getFinalStates().contains(frontierD2)) {
                    dfaFinalStates.add(stateMapping.get(hashSetFrontier));
                }
            }
            stateMappingFinished.put(hashSetFrontier, new State());
        }
        return new DFA(dfaStates, dfaTransitions, d1.getAlphabet(), newStartState, dfaFinalStates);
    }

    public static Set<State> getNewFrontier(DFA d1, DFA d2, State frontierD1, State frontierD2, char character) {
        Set<State> successors = new HashSet<>();
        successors.add(d1.getSuccessor(frontierD1, character));
        successors.add(d2.getSuccessor(frontierD2, character));
        return successors;
    }

    public static void main(String[] args){

        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();//remove the number of lines in the beginning
        EpsilonNFA e = Parser.parse(scanner);
        DFA d1 = null;
        if(e instanceof DFA){
            d1 = (DFA) e;
        }
        else{
            System.out.println("First automaton is no DFA, aborting");
            System.exit(3);
        }

        e = Parser.parse(scanner);
        DFA d2 = null;
        if(e instanceof DFA){
            d2 = (DFA) e;
        }
        else{
            System.out.println("Second automaton is no DFA, aborting");
            System.exit(3);
        }

        System.out.println("Case #1:\n" + intersection(d1,d2));
        scanner.close();
    }


}
