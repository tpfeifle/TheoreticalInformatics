package Task10.main.java.com.theo;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class Rectangle {
  private static final TuringMachine.Direction LEFT = TuringMachine.Direction.L;
  private static final TuringMachine.Direction RIGHT = TuringMachine.Direction.R;
  private static final char EMPTY = TuringMachine.EMPTY_LETTER;

  private Rectangle() {}

  public static void main(String[] args) {
    TuringMachine<?> tm = rectangleMachine();
    System.out.println(tm);
  }

  public static TuringMachine<?> rectangleMachine() {
    Set<Character> alphabet = new HashSet<>(Arrays.asList('^', 'v', '>', '<', 'D')); //this is the tape-alphabet, so you may add more
    // 'D' for done with this letter
    String stateWalkRight = "State_>";
    String stateWalkRight2 = "asdfState";
    String stateWalkRight3 = "fooState";
    String stateLeft = "State_<";
    String stateDown = "State_v";
    String stateFinal = "State_final";
    String stateCheckIfDone = "State_checkIfDone";
    TuringMachine<String> turingMachine = new TuringMachine<>(alphabet, stateWalkRight);
    turingMachine.addFinalState(stateFinal);
    // Add transitions, final states, etc. here

    //simply walk over the right, and left arrows
    turingMachine.addTransition(stateWalkRight, '>', stateWalkRight, '>', RIGHT);
    turingMachine.addTransition(stateWalkRight, '^', stateWalkRight, '^', RIGHT);
    turingMachine.addTransition(stateWalkRight, 'D', stateWalkRight, 'D', RIGHT);

    turingMachine.addTransition(stateWalkRight2, '>', stateWalkRight2, '>', RIGHT);
    turingMachine.addTransition(stateWalkRight2, '^', stateWalkRight2, '^', RIGHT);
    turingMachine.addTransition(stateWalkRight2, 'D', stateWalkRight2, 'D', RIGHT);

    turingMachine.addTransition(stateWalkRight3, '>', stateWalkRight3, '>', RIGHT);
    turingMachine.addTransition(stateWalkRight3, '^', stateWalkRight3, '^', RIGHT);
    turingMachine.addTransition(stateWalkRight3, 'D', stateWalkRight3, 'D', RIGHT);

    turingMachine.addTransition(stateWalkRight, '<', stateLeft, 'D', LEFT); //mark character as done
    turingMachine.addTransition(stateWalkRight2, '<', stateLeft, 'D', LEFT); //mark character as done
    turingMachine.addTransition(stateLeft, 'D', stateLeft, 'D', LEFT);
    turingMachine.addTransition(stateLeft, '^', stateLeft, '^', LEFT); //walk to opposite character
    turingMachine.addTransition(stateLeft, '>', stateWalkRight2, 'D', RIGHT);


    turingMachine.addTransition(stateWalkRight2, 'v', stateDown, 'D', LEFT); //mark character as done
    turingMachine.addTransition(stateWalkRight3, 'v', stateDown, 'D', LEFT); //mark character as done
    turingMachine.addTransition(stateDown, 'D', stateDown, 'D', LEFT);
    turingMachine.addTransition(stateDown, '^', stateWalkRight3, 'D', RIGHT); //walk to opposite character

    turingMachine.addTransition(stateWalkRight3, EMPTY, stateCheckIfDone, EMPTY, LEFT);
    turingMachine.addTransition(stateCheckIfDone, 'D', stateCheckIfDone, 'D', LEFT);
    turingMachine.addTransition(stateCheckIfDone, EMPTY, stateFinal, EMPTY, LEFT);

    return turingMachine;
  }
}
