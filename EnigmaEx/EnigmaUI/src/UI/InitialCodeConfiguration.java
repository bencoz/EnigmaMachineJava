package UI;

import java.util.*;

public class InitialCodeConfiguration{
    private class RotorLocation{
        private Integer chosenRotor;
        private Character InitialChosenRotorLocation;
    }
    private List<RotorLocation> rotorsLocation;
    private String reflectorID;

    public InitialCodeConfiguration(List<Integer> chosenRotors_sorted, List<Character> rotorsLocationInit_Sorted,
                                    String chosenReflectorId)
    {
        rotorsLocation = new ArrayList<>();
        RotorLocation rotorLoc = new RotorLocation();
        for(int i=0;i<chosenRotors_sorted.size();i++) //same as rotorsLoctionInit_sorted.size()
        {
            rotorLoc.chosenRotor = chosenRotors_sorted.get(i);
            rotorLoc.InitialChosenRotorLocation = rotorsLocationInit_Sorted.get(i);
            rotorsLocation.add(rotorLoc);
        }
        reflectorID = chosenReflectorId;
    }

    public String toString() {
        StringBuilder Description = new StringBuilder();
        Description.append("Current code Description: ").append('<');
        //the loop start from the end to the beginning because the first rotor should be written on the RIGHT side, and so on..
        for (int i = rotorsLocation.size()-1; i >=0 ; i--) {
            Description.append(rotorsLocation.get(i).chosenRotor);
            if (i != 0)
                Description.append(',');
            else
                Description.append('>');
        }
        Description.append('<');
        for (int i = rotorsLocation.size()-1; i >=0 ; i--) {
            Description.append(rotorsLocation.get(i).InitialChosenRotorLocation);
            if (i != 0)
                Description.append(',');
            else
                Description.append('>');
        }
        Description.append('<').append(reflectorID).append('>');
        return Description.toString();
    }

}
