package EnigmaMachineFactory.Actual;


public class Mapping {
    protected char from;
    protected char to;

    public Mapping(String from, String to) {
        this.from = from.toUpperCase().charAt(0);
        this.to = to.toUpperCase().charAt(0);
    }

    public Mapping(char from, char to) {
        this.from = from;
        this.to = to;
    }

    public boolean isDoubleMapping() {return from == to; }

    public char getFrom() {
        return from;
    }

    public char getTo() {
        return to;
    }
}