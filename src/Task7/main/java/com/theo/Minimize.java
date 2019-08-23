package Task7.main.java.com.theo;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Minimize {


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();//remove the number of lines in the beginning
        EpsilonNFA e = Parser.parse(scanner);
        DFA d = null;
        if (e instanceof DFA) {
            d = (DFA) e;
        } else {
            System.out.println("No DFA provided, aborting");
            System.exit(3);
        }

        System.out.println("Case #1:\n" + minimize(d));
        scanner.close();
    }

    protected static DFA minimize(DFA d) {
        final Set<State> reachableStates = d.computeReachableStates(d.getStartState());
        HashMap<Set<State>, State> differentStates = new HashMap<>();

        //collect all different s
        boolean finished = false;
        while (!finished) {
            finished = true;
            for (State state : reachableStates) {
                for (State otherState : reachableStates) {
                    //mark final-notFinal-state pairs as different
                    Set<State> wrapperSet = new HashSet<>();
                    wrapperSet.add(state);
                    wrapperSet.add(otherState);
                    if (d.getFinalStates().contains(state) != d.getFinalStates().contains(otherState)) {
                        differentStates.put(wrapperSet, new State());
                        continue;
                    }

                    for (char character : d.getAlphabet()) {
                        final State successorState = (State) d.getSuccessors(state, character).toArray()[0];
                        final State successorOtherState = (State) d.getSuccessors(otherState, character).toArray()[0];
                        Set<State> wrapperSet2 = new HashSet<>();
                        wrapperSet2.add(successorState);
                        wrapperSet2.add(successorOtherState);

                        if (differentStates.containsKey(wrapperSet2) && !differentStates.containsKey(wrapperSet)) {
                            finished = false;
                            differentStates.put(wrapperSet, new State());
                        }
                    }
                }
            }
        }

        //from different to equals
        HashMap<Set<State>, State> equalStates = new HashMap<>();
        for (State state : reachableStates) {
            for (State otherState : reachableStates) {
                Set<State> temp = new HashSet<>();
                temp.add(state);
                temp.add(otherState);
                if (!state.equals(otherState))
                    equalStates.put(temp, new State());
            }
        }
        for (Set<State> keySet : differentStates.keySet()) {
            equalStates.remove(keySet);
        }

        //merge multiple equals into bigger equals
        HashMap<Set<State>, State> compressedEqualStates = new HashMap<>();
        HashMap<Set<State>, State> compressedEqualStatesCopy = new HashMap<>();
        for (Set<State> mappings : equalStates.keySet()) {
            boolean added = false;
            for (State stateMappings : mappings) {
                for (Set<State> mappingsCompressed : compressedEqualStates.keySet()) {
                    if (mappingsCompressed.contains(stateMappings)) {
                        //remove old mapping and add mapping which is now stronger compressed
                        final State state = compressedEqualStatesCopy.get(mappingsCompressed);
                        compressedEqualStatesCopy.remove(mappingsCompressed);
                        mappingsCompressed.addAll(mappings);
                        compressedEqualStatesCopy.put(mappingsCompressed, state);
                        added = true;
                    }
                }
            }
            if (!added) {
                compressedEqualStates.put(mappings, equalStates.get(mappings));
                compressedEqualStatesCopy.put(mappings, equalStates.get(mappings));
            }
        }

        //create new dfa
        Set<State> dfaStates = new HashSet<>();
        Set<State> dfaFinalStates = new HashSet<>();
        Set<Transition> dfaTransitions = new HashSet<>();
        State dfaStartState = new State();
        for (State state : reachableStates) {
            State stateToAdd = null;
            for (Set<State> mappings : compressedEqualStatesCopy.keySet()) {
                if (mappings.contains(state)) {
                    stateToAdd = compressedEqualStatesCopy.get(mappings);
                }
            }
            if (stateToAdd == null) {
                stateToAdd = state;
            }

            if (d.getFinalStates().contains(state)) {
                dfaFinalStates.add(stateToAdd);
            }
            if (d.getStartState().equals(state)) {
                dfaStartState = stateToAdd;
            }
            dfaStates.add(stateToAdd);
        }

        for (State state : reachableStates) {
            //edit transitions
            for (Transition transition : d.getTransitionsFromState(state)) {
                State transitionEnd = transition.getEnd();
                State transitionStart = transition.getStart();
                for (Set<State> mappings : compressedEqualStatesCopy.keySet()) {
                    if (mappings.contains(transitionEnd)) {
                        transitionEnd = compressedEqualStatesCopy.get(mappings);
                    }
                    if (mappings.contains(transitionStart)) {
                        transitionStart = compressedEqualStatesCopy.get(mappings);
                    }
                }
                if (dfaStates.contains(transitionEnd))
                    dfaTransitions.add(new Transition(transitionStart, transitionEnd, transition.getLabel()));
            }
        }

        final DFA dfa = new DFA(dfaStates, dfaTransitions, d.getAlphabet(), dfaStartState, dfaFinalStates);
        State currentState = dfa.getStartState();
        HashMap<State, String> aquivalenceClassStrings = new HashMap<>();
        aquivalenceClassStrings.put(currentState, "");
        Set<State> finishedStates = new HashSet<>();
        Set<State> potentialNextStates = new HashSet<>();
        potentialNextStates.add(currentState);
        Set<State> aquivalenceClassStates = new HashSet<>();
        boolean finished2 = false;
        //name states appropriate to aquivalence-classes
        while(!finished2) {
            finished2 = true;
            //find not finished state
            for(State state : dfa.getStates()) {
                if(!finishedStates.contains(state) && potentialNextStates.contains(state)) {
                    currentState = state;
                    finished2 = false;
                    break;
                }
            }
            //name successor by their path
            for(char character : dfa.getAlphabet()) {
                if(!aquivalenceClassStrings.containsKey(dfa.getSuccessor(currentState, character))) {
                    String pre = aquivalenceClassStrings.get(currentState);
                    aquivalenceClassStrings.put(dfa.getSuccessor(currentState, character), pre + character);
                }
                potentialNextStates.add(dfa.getSuccessor(currentState, character));
            }
            finishedStates.add(currentState);
        }


        State aquivalenceClassStartState = dfa.getStartState();
        Set<State> aquivalenceClassFinalState = new HashSet<>();
        for (State state : dfa.getStates()) {
            State copiedState = state;
            if(aquivalenceClassStrings.get(state).equals(""))
                aquivalenceClassStrings.put(state, "eps");
            copiedState.setName(aquivalenceClassStrings.get(state));
            aquivalenceClassStates.add(copiedState);
            if(dfa.getFinalStates().contains(state)) {
                aquivalenceClassFinalState.add(copiedState);
            }
            if(dfa.getStartState().equals(state)) {
                aquivalenceClassStartState = copiedState;
            }
        }

        return new DFA(aquivalenceClassStates, dfa.getTransitions(), dfa.getAlphabet(), aquivalenceClassStartState, aquivalenceClassFinalState);
    }

}
