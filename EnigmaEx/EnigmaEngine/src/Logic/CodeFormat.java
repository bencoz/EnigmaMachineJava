package Logic;


import EnigmaMachineFactory.Actual.Reflector;
import EnigmaMachineFactory.SecretImpl;

import java.util.ArrayList;
import java.util.List;

public class CodeFormat {
    private List<Integer> rotors;
    private List<Character> positions;
    private Integer reflectorID; //TODO: check with Ben that its ok

    public CodeFormat(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, Integer chosenReflectorID){
        rotors = chosenRotorsID;
        positions = chosenRotorsLoc;
        reflectorID = chosenReflectorID;

    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CodeFormat that = (CodeFormat) o;

        if (rotors != null ? !rotors.equals(that.rotors) : that.rotors != null) return false;
        if (positions != null ? !positions.equals(that.positions) : that.positions != null) return false;
        return reflectorID != null ? reflectorID.equals(that.reflectorID) : that.reflectorID == null;
    }

    @Override
    public int hashCode() {
        int result = rotors != null ? rotors.hashCode() : 0;
        result = 31 * result + (positions != null ? positions.hashCode() : 0);
        result = 31 * result + (reflectorID != null ? reflectorID.hashCode() : 0);
        return result;
    }
}
