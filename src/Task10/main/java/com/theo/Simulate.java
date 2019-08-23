package Task10.main.java.com.theo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;

public final class Simulate {
    private Simulate() {
    }

    public static void main(String... args) throws IOException {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            TuringMachine<String> tm = TuringMachine.parse(reader);
            String line;
            while ((line = reader.readLine()) != null) {
                String[] split = line.split(";");
                System.out.println(simulate(tm, split[0], Integer.parseInt(split[1])));
            }
        }
    }

    public static <S> Result simulate(TuringMachine<S> tm, String word, int bound) {
        S currentState = tm.getInitialState();
        return simulatePart(tm, currentState, 0, word, bound);
    }

    public static <S> Result simulatePart(TuringMachine<S> tm, S currentState, int tapeHeadPos, String word, int bound) {
        if(bound < 0) {
            return Result.RUNNING;
        }
        if (tm.isFinal(currentState)) {
            return Result.ACCEPTED;
        }
        if(word.length() <= tapeHeadPos) {
            word = word + "█";
        }
        if (tapeHeadPos == 0) {
            tapeHeadPos += 1;
            word = "█" + word;
        }
        final char tapeChar = word.charAt(tapeHeadPos);
        final TuringMachine.Transition<S> transition = tm.getTransition(currentState, tapeChar);
        if(transition == null) {
            return Result.STUCK;
        }
        final char[] chars = word.toCharArray();
        chars[tapeHeadPos] = transition.letter;
        word = new String(chars);
        if (transition.direction.equals(TuringMachine.Direction.L)) {
            tapeHeadPos--;
        } else if(transition.direction.equals(TuringMachine.Direction.R)) {
            tapeHeadPos++;
        } else { //neutral
            word = transition.letter + word.substring(tapeHeadPos+1);
        }
        return simulatePart(tm, transition.successor, tapeHeadPos, word, bound - 1);
    }

    public enum Result {
        RUNNING, STUCK, ACCEPTED
    }
}
