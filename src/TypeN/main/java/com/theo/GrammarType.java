package TypeN.main.java.com.theo;

import java.util.Scanner;

public class GrammarType {


    public static boolean isType0(Grammar g){
        return true;
    }

    public static boolean isType1(Grammar g){
        if(!GrammarType.isType0(g)) {
            return false;
        }
        for(Production production : g.getProductions()) {
            //check if start -> epsilon
            if(production.getLeftSide().length() == 1 && production.getLeftSide().charAt(0) == g.getStartingSymbol()
                    && production.getRightSide().length() == 0) {
                continue;
            }
            if(production.getLeftSide().length() > production.getRightSide().length()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isType2(Grammar g){
        if(!GrammarType.isType1(g)) {
            return false;
        }
        for(Production production : g.getProductions()) {
            char alpha = production.getLeftSide().charAt(0);
            if (!g.getNonTerminals().contains(alpha) || production.getLeftSide().length() != 1) {
                return false;
            }
        }
        return true;
    }

    public static boolean isType3(Grammar g){
        if(!GrammarType.isType2(g)) {
            return false;
        }
        for(Production production : g.getProductions()) {
            //check if start -> epsilon
            if(production.getLeftSide().length() == 1 && production.getLeftSide().charAt(0) == g.getStartingSymbol()
                    && production.getRightSide().length() == 0) {
                continue;
            }
            char[] charactersBeta = production.getRightSide().toCharArray();
            if (charactersBeta.length == 1 && !g.getAlphabet().contains(charactersBeta[0])) {
                return false;
            }
            if (charactersBeta.length == 2 && (!g.getAlphabet().contains(charactersBeta[0]) || !g.getNonTerminals().contains(charactersBeta[1]))) {
                return false;
            }
            if (charactersBeta.length > 2) {
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args){
        Scanner s = new Scanner(System.in);
        Grammar g = Grammar.parseGrammar(s);
        System.out.println(isType0(g));
        System.out.println(isType1(g));
        System.out.println(isType2(g));
        System.out.println(isType3(g));
    }

}
