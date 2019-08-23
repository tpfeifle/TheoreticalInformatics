package Task9.main.java.com.theo;

import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public final class CYK {
    private CYK() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grammar g = Grammar.parse(scanner);
        String word;
        while (!(word = scanner.nextLine()).equals("DONE")) {
            System.out.println(word + "\n" + cyk(g, word) + "\n");
        }
        scanner.close();
    }

    public static CYKTable cyk(Grammar cnf, String word) {
        int size = word.length();
        CYKTable table = new CYKTable(size);
        for (int k = 0; k<size; k++) { //k = j-i
            for(int i=1; i<=size; i++) {
                for(int j=1;j<=size;j++) {
                    if (j-i == k) {
                        table.set(i, j, getMatchingNonTerminal(cnf, table, word, i, j));
                    }
                }
            }
        }


        /*for (int i = size; i > 0; i--) {
            for (int j = 1; j <= size; j++) {
                if (i + j <= size + 1) { //only work on the triangle, the rest is not required
                    table.set(i, j, getMatchingNonTerminal(cnf));
                }
            }
        }*/

        return table;
    }

    public static Set<NonTerminal> getMatchingNonTerminal(Grammar cnf, CYKTable table, String word, int i, int j) {
        Set<NonTerminal> nonTerminals = new HashSet<>();
        if(i == j) {
            for(Production production: cnf.productions) {
                if(production.right.size() == 1 && production.right.get(0).isTerminal()) {
                    final Terminal ter = (Terminal) production.right.get(0);
                    if(!ter.isEpsilon() && ter.letter() == word.charAt(i-1)) {
                        nonTerminals.add(production.left.get(0)); //because grammar in cnf there's only one element
                    }
                }

            }
        } else {
            for(int k=i; k<j; k++) {
                final Set<NonTerminal> nonTerminalsB = table.get(i, k);
                final Set<NonTerminal> nonTerminalsC = table.get(k+1, j);
                for(Production production: cnf.productions) {
                    if(production.right.size() == 2) {
                        final Atom atom0 = production.right.get(0);
                        final Atom atom1 = production.right.get(1);
                        if(!atom0.isTerminal() && !atom1.isTerminal()
                                && nonTerminalsB.contains(new NonTerminal(atom0.name()))
                                && nonTerminalsC.contains(new NonTerminal(atom1.name()))) {
                            nonTerminals.add(production.left.get(0));
                        }
                    }
                }
            }
        }
        return nonTerminals;
    }
}
