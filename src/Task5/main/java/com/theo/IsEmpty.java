package Task5.main.java.com.theo;
import java.util.Scanner;
import java.util.Set;

public class IsEmpty {

    public static boolean isEmpty(DFA n) {
        final Set<State> states = n.computeReachableStates(n.getStartState());
        for (State state : states) {
            if (n.getFinalStates().contains(state)) {
                return false;
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
        System.out.println(isEmpty(d));
        scanner.close();
    }
}
