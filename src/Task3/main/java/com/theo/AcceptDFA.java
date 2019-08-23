package Task3.main.java.com.theo;
import java.util.Scanner;
import java.util.Set;

public class AcceptDFA {

    public static boolean accept(DFA d, String w){
        final char[] wordCharacters = w.toCharArray();
        State currentState = d.getStartState();
        for (char character : wordCharacters) {
            if (!d.getAlphabet().contains(character)) {
                return false;
            }
            currentState = d.getSuccessor(currentState, character);
            if (currentState == null) {
                return false;
            }
        }
        return d.getFinalStates().contains(currentState);
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
        String word = scanner.nextLine();
        while(!word.equals("DONE")) {
            System.out.println(word + ": " + accept(d, word));
            word = scanner.nextLine();
        }
        scanner.close();
    }

}
