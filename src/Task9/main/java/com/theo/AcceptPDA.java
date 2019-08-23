package Task9.main.java.com.theo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public final class AcceptPDA {
    private AcceptPDA() {
    }

    static ArrayList<State> visitedStates = new ArrayList<>();
    static ArrayList<String> visitedStacks = new ArrayList<>();
    static ArrayList<String> visitedWords = new ArrayList<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PDA m = PDAParser.parse(scanner);
        String word = scanner.nextLine();
        while (!word.equals("DONE")) {
            System.out.println(word + ": " + accept(m, word));
            word = scanner.nextLine();
        }
        scanner.close();
    }

    public static boolean accept(PDA m, String w) {
        for (char character : w.toCharArray()) {
            if (!m.getAlphabet().contains(character)) {
                return false;
            }
        }
        visitedStates = new ArrayList<>();
        visitedStacks = new ArrayList<>();
        visitedWords = new ArrayList<>();
        return acceptsWord(m, w, m.getStartSymbol() + "", m.getStartState());
    }

    public static boolean acceptsWord(PDA m, String wordRest, String stack, State state) {
        if(stack.length() > 100)
            return false;
        if (stack.equals("") && wordRest.equals("")) { //if stack and wordRest is empty --> accept it
            return true;
        }
        char inputCharacter = wordRest.length() > 0 ? wordRest.charAt(0) : Util.EPSILON;
        char topOfStack = stack.length() > 0 ? stack.charAt(0) : Util.EPSILON;


        for (PDATransition transition : m.getTransitions()) {
            if (transition.getStart().equals(state) && (transition.getLabel() == inputCharacter || transition.getLabel() == Util.EPSILON)) {
                String tepmWordRest = (transition.getLabel() == Util.EPSILON || inputCharacter == Util.EPSILON) ? wordRest : wordRest.substring(1, wordRest.length());
                if (topOfStack == transition.getPopSymbol()) {
                    String newStack = transition.getPushSymbols() + stack.substring(1, stack.length());
                    if (!isAlreadyVisited(transition.getEnd(), newStack, tepmWordRest) && acceptsWord(m, tepmWordRest, newStack, transition.getEnd())) {
                        return true;
                    }
                }
                if (transition.getPopSymbol() == Util.EPSILON) {
                    final String newStack = transition.getPushSymbols() + stack;
                    if (!isAlreadyVisited(transition.getEnd(), newStack, tepmWordRest) && acceptsWord(m, tepmWordRest, newStack, transition.getEnd())) {
                        return true;
                    }
                }

            }
        }
        return false;
    }

    public static boolean isAlreadyVisited(State state, String stack, String word) {
        for(int i=0; i<visitedStates.size(); i++) {
            if(visitedStates.get(i).equals(state) && visitedStacks.get(i).equals(stack) && visitedWords.get(i).equals(word)) {
                return true;
            }
        }
        visitedStates.add(state);
        visitedWords.add(word);
        visitedStacks.add(stack);
        return false;
    }
}
