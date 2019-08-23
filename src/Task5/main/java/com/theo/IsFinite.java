package Task5.main.java.com.theo;
import java.util.Scanner;
import java.util.Set;

public class IsFinite {

    public static boolean isFinite(DFA d){
        for(State state : d.computeReachableStates(d.getStartState())) {
            final Set<State> reachableStates = d.computeReachableStates(state);
            if (!d.getAllPossibleSuccessors(state).contains(state)) { //remove onself from reachable, if not reflexiv
                reachableStates.remove(state);
            }
            if (reachableStates.contains(state)) {
                for (State selectedReachableState : reachableStates) {
                    if (d.getFinalStates().contains(selectedReachableState)) {
                       return false;
                    }
                }
            }

        }
        return true;

    }


    public static void main (String[] args){
        Scanner scanner = new Scanner(System.in);
        EpsilonNFA e = Parser.parse(scanner);
        DFA d = null;
        if(e instanceof DFA){
            d = (DFA) e;
        }
        else{
            System.out.println("No DFA provided, aborting");
            System.exit(3);
        }
        System.out.println(isFinite(d));
        scanner.close();
    }

}

