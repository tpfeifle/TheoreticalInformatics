package Task9.main.java.com.theo;

import java.util.HashSet;
import java.util.Set;

public final class Palindrome {
    private Palindrome() {
    }

    public static void main(String[] args) {
        printPalindromePDA();
    }

    // TODO: handle palindrom with length % 2 != 0
    public static void printPalindromePDA() {
        Set<State> states = new HashSet<>();
        State p = new State("p");
        State s = new State("s");
        State q = new State("q");
        State r = new State("r");
        states.add(p);
        states.add(s);
        states.add(q);
        states.add(r);


        Set<Character> alphabet = new HashSet<>();
        alphabet.add('0');
        alphabet.add('1');
        Set<Character> stackAlphabet = new HashSet<>();
        stackAlphabet.add('0');
        stackAlphabet.add('1');
        stackAlphabet.add('Z');
        Set<PDATransition> pdaTransitions = new HashSet<>();
        char Z_0 = 'Z';
        // Z=0,1,Z0
        // a = 0,1
        pdaTransitions.add(new PDATransition(p, '0', '0', p, "00"));
        pdaTransitions.add(new PDATransition(p, '0', '1', p, "01"));
        pdaTransitions.add(new PDATransition(p, '0', 'Z', p, "0Z"));
        pdaTransitions.add(new PDATransition(p, '1', '0', p, "10"));
        pdaTransitions.add(new PDATransition(p, '1', '1', p, "11"));
        pdaTransitions.add(new PDATransition(p, '1', 'Z', p, "1Z"));

        pdaTransitions.add(new PDATransition(p, Util.EPSILON, '0', q, "0"));
        pdaTransitions.add(new PDATransition(p, Util.EPSILON, '1', q, "1"));
        pdaTransitions.add(new PDATransition(p, Util.EPSILON, 'Z', q, "Z"));

        pdaTransitions.add(new PDATransition(q, '0', '0', q, ""));
        pdaTransitions.add(new PDATransition(q, '1', '1', q, ""));

        pdaTransitions.add(new PDATransition(q, Util.EPSILON, 'Z', r, ""));

        pdaTransitions.add(new PDATransition(p, '0', '1', s, "Z1"));
        pdaTransitions.add(new PDATransition(p, '0', '0', s, "Z0"));
        pdaTransitions.add(new PDATransition(p, '0', 'Z', s, "ZZ"));
        pdaTransitions.add(new PDATransition(p, '1', '1', s, "Z1"));
        pdaTransitions.add(new PDATransition(p, '1', '0', s, "Z0"));
        pdaTransitions.add(new PDATransition(p, '1', 'Z', s, "ZZ"));
        pdaTransitions.add(new PDATransition(s, Util.EPSILON, 'Z', q, ""));
        PDA palindromePDA = new PDA(states, pdaTransitions, alphabet, p, stackAlphabet, 'Z');
        System.out.println("Case #1:\n" +
                palindromePDA.toString());
    }
}
