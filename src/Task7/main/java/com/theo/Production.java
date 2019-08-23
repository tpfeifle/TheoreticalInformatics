package Task7.main.java.com.theo;

public class Production {
    final String left;
    final String right;

    Production(String left, String right) {
        this.left = left;
        this.right = right;
    }

    @Override
    public String toString() {
        if (right.isEmpty()) {
            return left + " \u2192 " + "\u03B5"; //print the greek letter epsilon for empty right side
        } else {
            return left + " \u2192 " + right;
        }
    }

    public int getHash() {
        int hash = 7;
        for (int i = 0; i < left.length()+right.length(); i++) {
            hash = hash*31;
            if(left.length() >= i+1)
                hash += left.charAt(i);
            if(right.length() >= i+1)
                hash += right.charAt(i);
        }
        return hash;
    }

    public boolean eqaully(Production p) {
        return p.getHash() == this.getHash();
    }
}
