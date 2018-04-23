package Logic;


import EnigmaMachineFactory.Actual.Reflector;
import EnigmaMachineFactory.SecretImpl;

import java.util.ArrayList;
import java.util.List;

public class CodeFormat {
    private List<Integer> rotors;
    private List<Character> positions;
    private String reflectorID;

    public CodeFormat(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, String chosenReflectorID){
        rotors = chosenRotorsID;
        positions = chosenRotorsLoc;
        reflectorID = chosenReflectorID;
    }

    public String toString() {
        StringBuilder Description = new StringBuilder();
        Description.append('<');
        //the loop start from the end to the beginning because the first rotor should be written on the RIGHT side, and so on..
        for (int i = rotors.size()-1; i >=0 ; i--) {
            Description.append(rotors.get(i));
            if (i != 0)
                Description.append(',');
            else
                Description.append('>');
        }
        Description.append('<');
        for (int i = positions.size()-1; i >=0 ; i--) {
            Description.append(positions.get(i));
            if (i != 0)
                Description.append(',');
            else
                Description.append('>');
        }
        Description.append('<').append(reflectorID).append('>');
        return Description.toString();
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
