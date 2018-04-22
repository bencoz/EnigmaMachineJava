package EnigmaMachineFactory;

import EnigmaMachineFactory.Actual.Rotor;

import java.util.LinkedList;
import java.util.List;

public class SecretImpl implements Secret {
    private List<Integer> selectedRotors;
    private List<Integer> selectedRotorsPositions;
    private int selectedReflector;

    public SecretImpl() {
        selectedRotorsPositions = new LinkedList<>();
        selectedRotors = new LinkedList<>();
    }

    public void addRotor(int rotorID, int rotorPosition){
        selectedRotors.add(rotorID);
        selectedRotorsPositions.add(rotorPosition);
    }

    public void setSelectedReflector(int reflectorID){
        selectedReflector = reflectorID;
    }

    @Override
    public List<Integer> getSelectedRotorsInOrder() {
        return selectedRotors;
    }//TODO:: add lambda function to increase position by one.

    @Override
    public List<Integer> getSelectedRotorsPositions() {
        return selectedRotorsPositions;
    }//TODO:: add lambda function to increase position by one.

    @Override
    public int getSelectedReflector() {
        return selectedReflector;
    }

    @Override
    public Secret setInitialPosition(List<Integer> newPosition) {
        SecretImpl newSecret = new SecretImpl();
        List<Integer> rotorIDs = this.getSelectedRotorsInOrder();
        for (int i = 0; i < rotorIDs.size(); i++)
            newSecret.addRotor(rotorIDs.get(i), newPosition.get(i)-1);//Position is 1-based

        return (Secret)newSecret;
    }
}
