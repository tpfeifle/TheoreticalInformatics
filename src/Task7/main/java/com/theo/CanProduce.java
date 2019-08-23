package Task7.main.java.com.theo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class CanProduce {
    static ArrayList<String> newAlreadyTried = new ArrayList<>();
    public static boolean canProduce(Grammar g, String w){
        char startingSymbol = g.startingSymbol;
        newAlreadyTried = new ArrayList<>();
        return findeAbleitungen(startingSymbol, "","", withoutEpsilon(g), w);
    }

    public static Grammar withoutEpsilon(Grammar g) {
        Set<String> nonTerminalToEpsilon = new HashSet<>();
        Set<Production> epsilonProductions = new HashSet<>();
        Set<Production> productionsCopy = new HashSet<>(g.productions);
        for(Production production: g.productions) {
            if(production.right.length() == 0 && !(production.left.equals(g.startingSymbol+""))) {
                nonTerminalToEpsilon.add(production.left);
                epsilonProductions.add(production);
            }
        }

        for(Production epsilonProduction: epsilonProductions) {
            for(Production production: g.productions) {
                if(production.right.contains(epsilonProduction.left)) {
                    productionsCopy.add(new Production(production.left, production.right.replace(epsilonProduction.left, "")));
                }
            }
        }
        productionsCopy.removeAll(epsilonProductions);
        return new Grammar(g.alphabet, g.nonTerminals, productionsCopy, g.startingSymbol);
    }


    public static boolean findeAbleitungen(char nonTerminal, String before, String after, Grammar g, String w) {
        if(w.length()+2 < before.length()+after.length())
            return false;
        for(Production production: g.productions) {
            String ganzeAbleitung = before + production.right + after;
            if(production.left.equals(nonTerminal+"") && !newAlreadyTried.contains(ganzeAbleitung)) {
                newAlreadyTried.add(ganzeAbleitung);
                if(w.equals(ganzeAbleitung)) {
                    return true;
                }
                for(int i=0;i<ganzeAbleitung.length(); i++) {
                    if(g.nonTerminals.contains(ganzeAbleitung.charAt(i))) {
                        if(findeAbleitungen(ganzeAbleitung.charAt(i), ganzeAbleitung.substring(0, i), ganzeAbleitung.substring(i+1, ganzeAbleitung.length()), g, w)) {
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }








































    /*public static boolean findeAbleitungen(char nonTerminal, String before, String after, Grammar g, String w) {
        int epsilon = (int) before.chars().filter(num -> num == g.startingSymbol).count() + (int) after.chars().filter(num -> num == g.startingSymbol).count();
        if(w.length()+2 < (before.length()+after.length())) {
            return false;
        }

        for(Production production: g.productions) {
            if(production.left.equals(nonTerminal+"")) {
                String ganzeAbleitung = before+production.right+after;
                if(newAlreadyTried.contains(ganzeAbleitung)) {
                    continue;
                }
                if (production.right.length() == 1 && g.alphabet.contains(production.right.charAt(0)) && w.equals(ganzeAbleitung)){
                    return true;
                }
                //epsilon-case
                if(production.right.length() == 0 && w.equals(ganzeAbleitung)) {
                    return true;
                }

                for(int i=0; i<ganzeAbleitung.length(); i++) {
                    if(g.nonTerminals.contains(ganzeAbleitung.charAt(i))) {
                        newAlreadyTried.add(ganzeAbleitung);
                        if(findeAbleitungen(ganzeAbleitung.charAt(i), ganzeAbleitung.substring(0, i), ganzeAbleitung.substring(i+1, ganzeAbleitung.length()), g, w)) {
                            return true;
                        }
                        break;
                    }
                }
            }
        }
        return false;
    }*/

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Grammar g = Grammar.parse(scanner);
        String word = scanner.nextLine();
        while(!word.equals("DONE")) {
            System.out.println(word + ": " + canProduce(g,word));
            word = scanner.nextLine();
        }
        scanner.close();
    }
}
