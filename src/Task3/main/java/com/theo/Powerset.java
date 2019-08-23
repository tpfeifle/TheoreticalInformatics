package Task3.main.java.com.theo;
import java.util.*;

public class Powerset {

    public static DFA powersetConstruction(NFA n){
        //TODO
        List<Set<State>> dfaStates = new ArrayList<>();
        Set<Transition> dfaTransitions = new HashSet<>();
        Set<State> dfaFinalStates = new HashSet<>();
        Set<State> stateCache = new HashSet<>();
        State emptyState = new State("empty");
        stateCache.add(emptyState);

        Set<State> startStateInSet = new HashSet<>();
        Set<State> emptyStateInSet = new HashSet<>();

        startStateInSet.add(n.getStartState());
        emptyStateInSet.add(emptyState);
        dfaStates.add(startStateInSet);
        dfaStates.add(emptyStateInSet);

        for(int i=0; i<dfaStates.size(); i++) {
            Set<State> dfaState = dfaStates.get(i);
            HashMap<Character, Set<State>> dfaStateTransitionsMapping = new HashMap<>();
            for (State selectedCurrentState : dfaState) {
                for (char character : n.getAlphabet()) {
                    Set<State> successors = new HashSet<>();
                    successors.addAll(n.getSuccessors(selectedCurrentState, character));
                    if(dfaStateTransitionsMapping.containsKey(character)) {
                        Set<State> currentMapping = dfaStateTransitionsMapping.get(character);
                        currentMapping.addAll(successors);
                        dfaStateTransitionsMapping.put(character, currentMapping);
                    } else {
                        dfaStateTransitionsMapping.put(character, successors);
                    }
                }
            }
            if(!dfaStateTransitionsMapping.isEmpty()) {
                final Iterator<Map.Entry<Character, Set<State>>> iterator = dfaStateTransitionsMapping.entrySet().iterator();
                while(iterator.hasNext()) {
                    Map.Entry<Character, Set<State>> pair = iterator.next();
                    if(!pair.getValue().isEmpty() && !dfaStates.contains(pair.getValue()))
                        dfaStates.add(pair.getValue());
                    dfaTransitions.add(new Transition(mergeStates(dfaState, stateCache), mergeStates(pair.getValue(), stateCache), pair.getKey()));

                }
            }
        }

        //merge for output
        Set<State> mergedDfaStates = new HashSet<>();
        Set<State> NFAFinalStates = n.getFinalStates();
        // dfaStates.remove(startStateInSet);
        for(Set<State> dfaState : dfaStates) {
            boolean isFinalState = false;
            for(State state : dfaState) {
                if(NFAFinalStates.contains(state))
                    isFinalState = true;
            }
            final State mergedState = mergeStates(dfaState, stateCache);
            mergedDfaStates.add(mergedState);
            if(isFinalState)
                dfaFinalStates.add(mergedState);
        }
        State startState = mergeStates(startStateInSet, stateCache);
        mergedDfaStates.add(startState);
        //if start-state is final state
        if(NFAFinalStates.contains(startState))
            dfaFinalStates.add(startState);
        return new DFA(mergedDfaStates, dfaTransitions, n.getAlphabet(), startState, dfaFinalStates);
    }

    private static State mergeStates(Set<State> states, Set<State> stateCache) {
        List<String> stateList = new ArrayList<>();
        for (State state : states) {
            stateList.add(state.getName());
        }
        Collections.sort(stateList);

        for (State cachedState : stateCache) {
            if (cachedState.getName().equals(String.join(",", stateList)))
                return cachedState;
        }

        State res;
        if(stateList.isEmpty()) {
            res = (State) stateCache.stream().filter((state) -> state.getName().equals("empty")).toArray()[0];
        } else {
            res = new State(String.join(",", stateList));
        }

        stateCache.add(res);
        return res;
    }

    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        scanner.nextLine();//remove the number of lines in the beginning
        EpsilonNFA e = Parser.parse(scanner);
        scanner.close();
        NFA n = null;
        if(e instanceof NFA){
            n = (NFA) e;
        }
        else{
            System.out.println("No NFA provided, aborting");
            System.exit(3);
        }
        System.out.println("Case #1:\n" + powersetConstruction(n));
    }


}
