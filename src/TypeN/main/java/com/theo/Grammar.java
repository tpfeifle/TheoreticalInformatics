package TypeN.main.java.com.theo;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Grammar {
    private final Set<Character> alphabet;
    private final Set<Character> nonTerminals;
    private final Set<Production> productions;
    private final Character startingSymbol;

    public Grammar(Set<Character> alphabet, Set<Character> nonTerminals, Set<Production> productions, Character startingSymbol) {
        this.alphabet = alphabet;
        this.nonTerminals = nonTerminals;
        this.productions = productions;
        this.startingSymbol = startingSymbol;
        checkValidGrammar();
    }

    private void checkValidGrammar() {
        if (alphabet==null){throw new IllegalArgumentException("Grammar constructor: alphabet cannot be null.");}
        if (nonTerminals==null){throw new IllegalArgumentException("Grammar constructor: nonTerminals cannot be null.");}
        if (productions==null){throw new IllegalArgumentException("Grammar constructor: productions cannot be null.");}
        if (startingSymbol==null){throw new IllegalArgumentException("Grammar constructor: startingSymbol cannot be null.");}
        for (Character t : alphabet){
            if (nonTerminals.contains(t)){
                throw new IllegalArgumentException("Terminals and nonterminals must be disjoint! " + t + " is part of both.");
            }
        }
        if(!nonTerminals.contains(startingSymbol)){
            throw new IllegalArgumentException("Starting symbol " + startingSymbol + " must be part of the nonterminals");
        }
        for (Production p : productions){//no need to check that p is not null, because productions is a valid set, and hence only valid productions can be in it
            if(p.getLeftSide().length()==0){
                throw new IllegalArgumentException("Production left side must be set for production " + p);
            }//if right side has length 0, then it is epsilon, which is fine
            char[] letters = new char[p.getLeftSide().length()+p.getRightSide().length()];
            p.getLeftSide().getChars(0,p.getLeftSide().length(),letters,0);
            p.getRightSide().getChars(0,p.getRightSide().length(),letters,p.getLeftSide().length());
            for (char c : letters){
                if (!alphabet.contains(c) && !nonTerminals.contains(c)){
                    throw new IllegalArgumentException("Letter " + c + " in production " + p + " is neither a terminal nor a nonterminal");
                }
            }
        }
    }

    public Set<Character> getAlphabet() {
        return alphabet;
    }

    public Set<Character> getNonTerminals() {
        return nonTerminals;
    }

    public Set<Production> getProductions() {
        return productions;
    }

    public Character getStartingSymbol() {
        return startingSymbol;
    }

    @Override
    public String toString() {
        return "Grammar{" +
                "alphabet=" + alphabet +
                ", nonTerminals=" + nonTerminals +
                ", productions=" + productions +
                ", startingSymbol=" + startingSymbol +
                '}';
    }





    //Die folgende Methode müssen Sie nicht betrachten oder verstehen.
    /**
     * Parses the grammar that is defined in the file f.
     * A file may look like this
     *
     * Grammar
     * Nonterminals: A,B,C
     * Alphabet: a,b,c
     * Startsymbol: A
     * Productions:
     * A -> Aa
     * A -> AB
     *
     * Needs to start with "Grammar"
     * Second line: "Nonterminals: " and then all nonterminals, which can only be chars, in a comma separated list without spaces.
     * Third line: "Alphabet: " and then all terminals, see above.
     * Fourth line: "Startsymbol: " and then the startsymbol
     * Fifth line: "Productions:"
     * Then each production takes a line; left and right side are separated by " -> "; using multiple right sides with "|" is currently not supported.
     *
     */
    public static Grammar parseGrammar(Scanner s) {

        //First line
        String line = s.nextLine();
        if (!line.equals("Grammar")){
            throw new IllegalArgumentException("Parsed grammar does not start with 'Grammar'.");
        }

        //Second line; nonterminals
        line = s.nextLine();
        if (!line.startsWith("Nonterminals: ")){
            throw new IllegalArgumentException("Parsed grammar does not declare Nonterminals first.");
        }
        HashSet<Character> nonterminals = new HashSet<>();
        line = line.split(": ")[1];
        for (String nonterminal : line.split(",")){
            if(nonterminal.length()==1){
                nonterminals.add(nonterminal.charAt(0));
            }
            else{
                throw new IllegalArgumentException("Nonterminals have to be input as a comma separated list without spaces. Nonterminals may only be chars.");
            }
        }

        //Third line; Alphabet
        line = s.nextLine();
        if (!line.startsWith("Alphabet: ")){
            throw new IllegalArgumentException("Parsed grammar does not declare Alphabet second.");
        }
        HashSet<Character> alphabet = new HashSet<>();
        line = line.split(": ")[1];
        for (String terminal : line.split(",")){
            if(terminal.length()==1){
                alphabet.add(terminal.charAt(0));
            }
            else{
                throw new IllegalArgumentException("Alphabet has to be input as a comma separated list without spaces. Terminals may only be chars.");
            }
        }

        //Fourth line; Startsymbol
        line = s.nextLine();
        if (!line.startsWith("Startsymbol: ")){
            throw new IllegalArgumentException("Parsed grammar does not declare startsymbol third.");
        }
        Character startsymbol;
        if(line.split(": ")[1].length()==1){
            startsymbol=line.split(": ")[1].charAt(0);
        }
        else{
            throw new IllegalArgumentException("Startsymbol must be a single char.");
        }

        //Fifth line; rules
        line = s.nextLine();
        if (!line.equals("Productions:")){
            throw new IllegalArgumentException("Parsed grammar does not contain the String 'Productions' in the fifth line.");
        }
        HashSet<Production> productions = new HashSet<Production>();
        while(true){
            line = s.nextLine();
            if(line.equals(("END"))){
                break;
            }
            if(line.contains(" -> ")){
                String left = line.split(" -> ")[0];
                String[] right = line.split(" -> ")[1].split("\\|");
                for (String r : right) {
                    productions.add(new Production(left,r));
                }
            }
        }

        s.close();

        return new Grammar(alphabet,nonterminals,productions,startsymbol);
    }
}