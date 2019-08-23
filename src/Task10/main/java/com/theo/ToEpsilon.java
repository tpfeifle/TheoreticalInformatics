package Task10.main.java.com.theo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public final class ToEpsilon {
    private ToEpsilon() {
    }

    public static void main(String... args) throws IOException {
        try (BufferedReader reader =
                     new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            System.out.println(toEpsilonNFA(TuringMachine.parse(reader)));
        }
    }

    public static <S> EpsilonNFA<?> toEpsilonNFA(TuringMachine<S> tm) {
        final Set<S> states = tm.getStates();
        Set<S> epsilonFinales = new HashSet<>();
        final EpsilonNFA<S> epsilonNFA = new EpsilonNFA<>(tm.getAlphabet(), tm.getInitialState());

        boolean finished = false;
        while (!finished) {
            finished = true;
            for (S state : states) {
                final TuringMachine.Transition<S> transition = tm.getTransition(state, TuringMachine.EMPTY_LETTER);
                if (transition != null && (epsilonFinales.contains(transition.successor) || tm.getFinalStates().contains(transition.successor))
                        && !(epsilonFinales.contains(state) || tm.getFinalStates().contains(state))) {
                    epsilonFinales.add(state);
                    epsilonNFA.addFinalState(state);
                    finished = false;
                }
            }
        }

        for (S state : states) {
            for (char character : tm.getAlphabet()) {
                TuringMachine.Transition<S> transition = tm.getTransition(state, character);
                if (transition != null) {
                    epsilonNFA.addTransition(state, character, transition.successor);
                }

                if (tm.isFinal(state) && !epsilonFinales.contains(state)) {
                    epsilonNFA.addTransition(state, character, state);
                }
            }

            epsilonNFA.addState(state);

            if (tm.isFinal(state)) {
                epsilonNFA.addFinalState(state);
            }
        }

        return epsilonNFA;
    }
}
