package TypeN.main.java.com.theo;

public class Production {

    private final String leftSide;
    private final String rightSide;

    public Production(String leftSide, String rightSide) {
        this.leftSide = leftSide;
        this.rightSide = rightSide;
    }

    public String getLeftSide() {
        return leftSide;
    }

    public String getRightSide() {
        return rightSide;
    }

    @Override
    public String toString() {
        if(rightSide.length()>0)
            return leftSide + " \u2192 " + rightSide;
        else
            return leftSide + " \u2192 " + "\u03B5"; //print the greek letter epsilon for empty right side
    }
}
