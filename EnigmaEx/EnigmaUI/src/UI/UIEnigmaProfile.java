package UI;

import java.lang.reflect.Array;
import java.util.*;

public class UIEnigmaProfile {

    private class RotorNotch{
        private Integer rotorId;
        private Integer notchPosition;
    }

    private int maxNumOfRotors;
    private int actualNumOfRotors;
    private List<RotorNotch> rotorsNotch;
    private int numOfReflectors;
    private int numOfMessages;
    private boolean isInitial;
    private InitialCodeConfiguration initialCodeConfig;

    public void setMaxNumOfRotors(int i_maxNumOfRotors)
    {
        maxNumOfRotors=i_maxNumOfRotors;
    }

    public void setActualNamOfRotors(int i_actualNamOfRotors)
    {
        actualNumOfRotors = i_actualNamOfRotors;
    }

    public void setRotorsNotch(List<Integer> i_rotorsID_sorted, List<Integer> i_rotorsNotch_sorted)
    {
        rotorsNotch = new ArrayList<RotorNotch>();
        RotorNotch rotorNotch = new RotorNotch();
        for(int i=0;i<i_rotorsID_sorted.size();i++) //same as i_rotorsNotch_sorted.size()
        {
            rotorNotch.rotorId = i_rotorsID_sorted.get(i);
            rotorNotch.notchPosition = i_rotorsNotch_sorted.get(i);
            rotorsNotch.add(rotorNotch);
        }
    }

    public void setNumOfReflectors(int i_numOfReflectors)
    {
        numOfReflectors = i_numOfReflectors;
    }

    public void setNumOfMessages(int i_numOfMessages)
    {
        numOfMessages = i_numOfMessages;
    }

    public void setAsInitial()
    {
        isInitial = true;
    }



    public void setInitialCodeConfiguration(List<Integer> chosenRotors_sorted, List<Character> rotorsLocationInit_Sorted,
                                             Integer chosenReflectorId)
    {
        initialCodeConfig = new InitialCodeConfiguration(chosenRotors_sorted, rotorsLocationInit_Sorted, chosenReflectorId);
    }

    public String toString()
    {
        StringBuilder Description = new StringBuilder();
        Description.append("This is the Enigma machine specifications:\n");
        Description.append("Number of rotors: ").append(actualNumOfRotors).append('/').append(maxNumOfRotors).append('\n');
        Description.append("Rotors Notch:\n");
        for(RotorNotch rotorNotch : rotorsNotch)
        {
            Description.append("Rotor Number: ").append(rotorNotch.rotorId).append("notch position: ").append(rotorNotch.notchPosition).append('\n');
        }
        Description.append("Number of reflectors: ").append(numOfReflectors).append('\n');
        Description.append("Number of messages (until now): ").append(numOfMessages).append('\n');
        if(isInitial) {
            Description.append(initialCodeConfig).append('\n');
        }
        return Description.toString();
    }

}
