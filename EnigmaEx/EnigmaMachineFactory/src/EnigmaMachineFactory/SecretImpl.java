package EnigmaMachineFactory;

import EnigmaMachineFactory.Actual.Rotor;

import java.util.LinkedList;
import java.util.List;

public class SecretImpl implements Secret {
    private List<Integer> selectedRotors;
    private List<Integer> selectedRotorsPositions;
    private int selectedReflector;
	private int abcSize;
	
    public SecretImpl() {
        selectedRotorsPositions = new LinkedList<>();
        selectedRotors = new LinkedList<>();
    }
	
	public void setAbcSize(int abcSize) {
        this.abcSize = abcSize;
    }
	
    @Override
    public void addRotor(int rotorID, int rotorPostion){
        selectedRotors.add(rotorID);
        selectedRotorsPositions.add(rotorPostion);
    }

    @Override
    public void setSelectedReflector(int reflectorID){
        selectedReflector = reflectorID;
    }

    @Override
    public void moveToNext(EnigmaMachine i_machine) {
        List<Integer> chosenRotorsIDs = i_machine.getRotorsId_sorted();
        List<Integer> chosenRotorsPositions = i_machine.getChosenRotorsPositions();
        Integer chosenReflectorID = i_machine.getChosenReflectorId();
        Integer abcSize = i_machine.getABC().length();
        SecretBuilder secretBuilder = i_machine.createSecret();
        boolean isCarry = false;

        for(int i = 0; i < chosenRotorsIDs.size(); i++) {
            Integer position = chosenRotorsPositions.get(i);
            if (i == 0 || isCarry){
                position = (position + 1) % abcSize;
                isCarry = (position == 0 ? true:false);
            }
            secretBuilder.selectRotor(chosenRotorsIDs.get(i), position);
        }
        secretBuilder.selectReflector(chosenReflectorID);
        secretBuilder.create();
    }

    @Override
    public boolean hasNext(EnigmaMachine i_machine) {
        List<Integer> chosenRotorsPositions = i_machine.getChosenRotorsPositions();
        Integer abcSize = i_machine.getABC().length();
        for (int i = 0; i < chosenRotorsPositions.size(); i++){
            if (chosenRotorsPositions.get(i) != (abcSize - 1)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Secret toZero() {
        Secret newSecret = new SecretImpl();
        newSecret.setSelectedReflector(this.selectedReflector);
        for (int i = 0; i < this.selectedRotors.size(); i++)
            newSecret.addRotor(this.selectedRotors.get(i),0);
        return newSecret;
    }

    @Override
    public Secret advanceBy(Integer taskSize) {
        Secret newSecret = new SecretImpl();
        newSecret.setSelectedReflector(this.selectedReflector);
        boolean isCarry, finished;
        isCarry = finished = false;
        int carry = 0;
        for (int i = 0; i < this.selectedRotors.size(); i++) {
            Integer lastPosition = this.getSelectedRotorsPositions().get(i);
            Integer newPosition = lastPosition;
            if (i == 0) {
                newPosition = lastPosition + taskSize;
                if (newPosition >= abcSize) {
                    carry = newPosition - abcSize;
                    newPosition = newPosition % abcSize;
                    isCarry = true;
                }
            } else if (isCarry){
                newPosition = lastPosition + carry;
                if (newPosition >= abcSize) {
                    carry = newPosition - abcSize;
                    newPosition = newPosition % abcSize;
                    isCarry = true;
                } else
                    isCarry = false;
            }
            newSecret.addRotor(this.selectedRotors.get(i), newPosition);
        }
        return newSecret;
    }

    @Override
    public List<Integer> getSelectedRotorsInOrder() {
        return selectedRotors;
    }

    @Override
    public List<Integer> getSelectedRotorsPositions() {
        return selectedRotorsPositions;
    }

    @Override
    public int getSelectedReflector() {
        return selectedReflector;
    }

    @Override
    public Secret setInitialPosition(List<Integer> newPosition) {
        SecretImpl newSecret = new SecretImpl();
        List<Integer> rotorIDs = this.getSelectedRotorsInOrder();
        for (int i = 0; i < rotorIDs.size(); i++)
            newSecret.addRotor(rotorIDs.get(i).intValue(), newPosition.get(i).intValue()-1 );//Postion is 1-based

        return (Secret)newSecret;
    }

}
