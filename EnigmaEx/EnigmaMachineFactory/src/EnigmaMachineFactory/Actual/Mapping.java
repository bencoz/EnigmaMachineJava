package EnigmaMachineFactory.Actual;


import java.io.Serializable;

public class Mapping implements Serializable {
    protected char right;
    protected char left;

    public Mapping(String right, String left) {
        this.right = right.toUpperCase().charAt(0);
        this.left = left.toUpperCase().charAt(0);
    }

    public Mapping(char right, char left) {
        this.right = right;
        this.left = left;
    }

    public boolean isDoubleMapping() {return right == left; }

    public char getRight() {
        return right;
    }

    public char getLeft() {
        return left;
    }
}