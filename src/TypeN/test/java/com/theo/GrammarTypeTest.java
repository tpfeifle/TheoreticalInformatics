package TypeN.test.java.com.theo;

import TypeN.main.java.com.theo.Grammar;
import TypeN.main.java.com.theo.GrammarType;
import TypeN.main.java.com.theo.Production;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class GrammarTypeTest {

    @Test
    public void testType2() {
        Set<Character> alphabet = new HashSet<>();
        alphabet.add('b');
        alphabet.add('a');
        Set<Character> nonTerminals = new HashSet<>();
        nonTerminals.add('A');
        nonTerminals.add('S');
        nonTerminals.add('B');
        Set<Production> productions = new HashSet<>();
        productions.add(new Production("S", "AB"));
        productions.add(new Production("S", ""));
        productions.add(new Production("S", "ASB"));
        productions.add(new Production("A", "a"));
        productions.add(new Production("B", "b"));
        Grammar g = new Grammar(alphabet, nonTerminals, productions, 'S');
        assert(GrammarType.isType0(g));
        assert(GrammarType.isType1(g));
        assert(GrammarType.isType2(g));
        assertEquals(GrammarType.isType3(g), false);
    }

    @Test
    public void testType3() {
        Set<Character> alphabet = new HashSet<>();
        alphabet.add('b');
        alphabet.add('a');
        Set<Character> nonTerminals = new HashSet<>();
        nonTerminals.add('X');
        nonTerminals.add('Y');
        Set<Production> productions = new HashSet<>();
        productions.add(new Production("X", ""));
        productions.add(new Production("X", "a"));
        productions.add(new Production("X", "aY"));
        productions.add(new Production("Y", "b"));
        Grammar g = new Grammar(alphabet, nonTerminals, productions, 'X');
        assert(GrammarType.isType0(g));
        assert(GrammarType.isType1(g));
        assert(GrammarType.isType2(g));
        assert(GrammarType.isType3(g));
    }

    @Test
    public void testType0() {
        Set<Character> alphabet = new HashSet<>();
        alphabet.add('b');
        alphabet.add('a');
        alphabet.add('c');
        Set<Character> nonTerminals = new HashSet<>();
        nonTerminals.add('A');
        nonTerminals.add('B');
        nonTerminals.add('C');
        nonTerminals.add('S');
        nonTerminals.add('D');
        Set<Production> productions = new HashSet<>();
        productions.add(new Production("S", "ACaB"));
        productions.add(new Production("Bc", "acB"));
        productions.add(new Production("CB", "DB"));
        productions.add(new Production("aD", "Db"));
        Grammar g = new Grammar(alphabet, nonTerminals, productions, 'S');
        assert(GrammarType.isType0(g));
        assert(GrammarType.isType1(g));
        assertEquals(GrammarType.isType2(g), false);
        assertEquals(GrammarType.isType3(g), false);
    }

    @Test
    public void testCornerCases() {
        Set<Character> alphabet = new HashSet<>();
        alphabet.add('b');
        alphabet.add('a');
        Set<Character> nonTerminals = new HashSet<>();
        nonTerminals.add('S');
        nonTerminals.add('X');
        Set<Production> productions = new HashSet<>();
        productions.add(new Production("S", "X"));
        //productions.add(new Production("Xa", "a"));
        productions.add(new Production("S", ""));
        Grammar g = new Grammar(alphabet, nonTerminals, productions, 'S');
        assert(GrammarType.isType0(g));
        assert(GrammarType.isType1(g));
    }

}