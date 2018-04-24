package EnigmaMachineFactory.Actual;

public class Reflect {
    protected int input;
    protected int output;

    public Reflect(int input, int output) {
        this.input = input;
        this.output = output;
    }

    public int compareToByInput(Reflect r2) {
        return this.input - r2.input;
    }

    public boolean isDoubleMapping(){
        return input == output;
    }

    public int getInput()
    {
        return input;
    }

    public int getOutput()
    {
        return output;
    }
}