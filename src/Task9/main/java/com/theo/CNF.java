package Task9.main.java.com.theo;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public final class CNF {
    private CNF() {
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grammar g = Grammar.parse(scanner);
        scanner.close();

        Grammar cnf = cnf(g);
        System.out.println(cnf);
    }

    /*
     * 1. Ersetze Nichtterminale, falls length >= 2
     * 2. Substituire Terminale, falls length >= 2
     * 3. Eliminiere e-Produktionen
     * 4. Eliminiere Kettenproduktionen
     */
    public static Grammar cnf(Grammar g) {
        Grammar temp = replaceNonTerminals(g);
        temp = substituteTerminals(temp);
        temp = eliminateEpsilonProductions(temp);
        temp = eliminateChainProductions(temp);
        return temp;
    }

    public static Grammar replaceNonTerminals(Grammar g) {
        Set<NonTerminal> nonTerminals = new HashSet<>(g.nonTerminals);
        Set<Production> productions = new HashSet<>();
        for (Production production : g.productions) {
            if (production.right.size() >= 2) {
                List productionRightTemp = new ArrayList(production.right);
                for (Atom atom : production.right) {
                    if (atom.isTerminal()) {
                        final NonTerminal nonTerminal = new NonTerminal("Y_" + atom.letter());
                        nonTerminals.add(nonTerminal);

                        for (int i = 0; i < productionRightTemp.size(); i++) {
                            if (productionRightTemp.get(i).equals(atom)) {
                                productionRightTemp.set(i, nonTerminal);
                            }
                        }
                        productions.add(new Production(nonTerminal, atom));
                    }
                }
                productions.add(new Production(production.left, productionRightTemp));
            } else {
                productions.add(production);
            }
        }

        return new Grammar(g.alphabet, nonTerminals, productions, g.startSymbol);
    }

    public static Grammar substituteTerminals(Grammar g) {
        Set<NonTerminal> nonTerminals = new HashSet<>(g.nonTerminals);
        Set<Production> productions = new HashSet<>();
        for(Production production : g.productions) {
            if(production.right.size() > 2) {
                List<Atom> newRight = new ArrayList<>();
                newRight.add(production.right.get(0));
                NonTerminal newNonTerminal = new NonTerminal("X_["+production.right.get(1).name()+";"+production.right.get(2).name()+"]");
                newRight.add(newNonTerminal);
                Production split1 = new Production(production.left, newRight);
                Production split2 = new Production(newNonTerminal, production.right.subList(1, 3));
                productions.add(split1);
                productions.add(split2);
                nonTerminals.add(newNonTerminal);
                // TODO : for chains longer than 3 (ex. S -> A B C D)
                /*for(int i=production.right.size();i>2; i++) {

                    production.right.get(i+1)
                }*/
            } else {
                productions.add(production);
            }
        }
        return new Grammar(g.alphabet, nonTerminals, productions, g.startSymbol);
    }

    public static Grammar eliminateEpsilonProductions(Grammar g) {
        Set<Production> epsilonProductions = new HashSet<>();
        Set<Production> productionsCopy = new HashSet<>(g.productions);
        for(Production production: g.productions) {
            if(production.isEpsilonProducing()) {
                epsilonProductions.add(production);
            }
        }

        for(Production epsilonProduction: epsilonProductions) {
            for(Production production: g.productions) {
                if(production.right.contains(epsilonProduction.left.get(0))) {
                    productionsCopy.remove(production);
                    List<Atom> right = new ArrayList<>();
                    for(Atom atom : production.right) {
                        if(!atom.equals(epsilonProduction.left.get(0))) {
                            right.add(atom);
                        }
                    }
                    productionsCopy.add(new Production(production.left, right));
                }
            }
        }
        productionsCopy.removeAll(epsilonProductions);
        return new Grammar(g.alphabet, g.nonTerminals, productionsCopy, g.startSymbol);
    }

    public static Grammar eliminateChainProductions(Grammar g) {
        return g;
    }
}
