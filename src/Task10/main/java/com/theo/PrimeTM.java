package Task10.main.java.com.theo;


import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class PrimeTM {
    private static final TuringMachine.Direction LEFT = TuringMachine.Direction.L;
    private static final TuringMachine.Direction RIGHT = TuringMachine.Direction.R;
    private static final char EMPTY = TuringMachine.EMPTY_LETTER;

    private PrimeTM() {
    }

    public static void main(String... args) {
        TuringMachine<?> tm = primeMachine();
        System.out.println(tm);
    }

    public static TuringMachine<?> primeMachine() {
        Set<Character> alphabet = new HashSet<>(Arrays.asList('a', 'S', 'T', '*', '#'));
        TuringMachine<String> tm = new TuringMachine<>(alphabet, "init");

        //add markers
        tm.addTransition("init", 'a', "jump", 'a', LEFT);
        tm.addTransition("jump", EMPTY, "num", '*', LEFT);
        tm.addTransition("num", EMPTY, "rightDirection", '#', RIGHT);

        //place markers
        tm.addScan("rightDirection", '*', RIGHT);
        tm.addScan("rightDirection", 'T', RIGHT);
        tm.addScan("leftDirection", 'T', LEFT);
        tm.addScan("leftDirection", '*', LEFT);

        tm.addTransition("rightDirection", 'a', "leftDirection", 'T', LEFT);
        tm.addTransition("rightDirection", EMPTY, "num", EMPTY, LEFT);
        tm.addTransition("leftDirection", 'S', "repeat", 'S', RIGHT);
        tm.addTransition("leftDirection", '#', "repeat2", '#', RIGHT);


        tm.addTransition("repeat", '*', "rightDirection", 'S', RIGHT);
        tm.addTransition("repeat", 'T', "test", 'T', RIGHT);
        tm.addScan("test", 'T', RIGHT);
        tm.addTransition("test", 'a', "reset", 'a', LEFT);

        tm.addTransition("repeat2", '*', "rightDirection", '#', RIGHT);
        tm.addTransition("repeat2", 'T', "test2", 'T', RIGHT);
        tm.addScan("test2", 'T', RIGHT);
        tm.addTransition("test2", 'a', "reset", 'a', LEFT);
        tm.addTransition("test2", EMPTY, "accept", EMPTY, RIGHT); //match for prime

        //resetting
        tm.addTransition("reset", EMPTY, "switchLeading", EMPTY, RIGHT);
        tm.addTransition("switchLeading", '*', "rightDirection", 'S', RIGHT);
        tm.addScan("reset", 'T', LEFT);
        tm.addTransition("reset", 'S', "reset", '*', LEFT);
        tm.addTransition("reset", '#', "reset", '*', LEFT);


        tm.addFinalState("accept");
        return addNumTransactions(tm);
    }

    public static TuringMachine<String> addNumTransactions(TuringMachine<String> tm) {
        //only need 7 numbers, because numbers till 50 can be represented by 7*something
        tm.addTransition("num", 'T', "num", 'a', LEFT);
        tm.addTransition("num", 'S', "num1", '*', LEFT);
        tm.addTransition("num", '*', "num1", '*', LEFT);

        for(int i=1; i<=6;i++) {
            tm.addTransition("num"+i, 'S', "num"+(i+1), '*', LEFT);
            tm.addTransition("num"+i, '*', "num"+(i+1), '*', LEFT);
            if(i%2 == 1) {
                tm.addTransition("num"+i, EMPTY, "num"+(i+1), '*', LEFT);
            } else {
                tm.addTransition("num"+i, EMPTY, "rightDirection", '#', RIGHT);
            }
        }

        tm.addTransition("num7", EMPTY, "accept", EMPTY, RIGHT);
        return tm;
    }
}




