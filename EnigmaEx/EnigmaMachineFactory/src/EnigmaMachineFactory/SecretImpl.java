package EnigmaMachineFactory;

import EnigmaMachineFactory.Actual.Rotor;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

public class SecretImpl implements Secret, Serializable {
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
    public Secret moveToNext(EnigmaMachine i_machine) {
        SecretBuilder secretBuilder = i_machine.createSecret();
        boolean isCarry = false;

        for(int i = 0; i < this.selectedRotors.size(); i++) {
            Integer position = this.selectedRotorsPositions.get(i);
            if (i == 0 || isCarry){
                position = (position + 1) % this.abcSize;
                isCarry = (position == 0 ? true:false);
            }
            secretBuilder.selectRotor(this.selectedRotors.get(i), position + 1);//method uses position as 1-based
        }
        secretBuilder.selectReflector(this.selectedReflector);
        return secretBuilder.create();
    }

    @Override
    public boolean hasNext(EnigmaMachine i_machine) {
        for (int i = 0; i < this.selectedRotorsPositions.size(); i++){
            if (this.selectedRotorsPositions.get(i) != (this.abcSize - 1)){
                return true;
            }
        }
        return false;
    }

    @Override
    public Secret toZero() {
        Secret newSecret = new SecretImpl();
        newSecret.setABCSize(this.abcSize);
        newSecret.setSelectedReflector(this.selectedReflector);
        for (int i = 0; i < this.selectedRotors.size(); i++)
            newSecret.addRotor(this.selectedRotors.get(i),0);
        return newSecret;
    }

    @Override
    public Secret advanceBy(Integer taskSize) {
        Secret newSecret = new SecretImpl();
        newSecret.setABCSize(this.abcSize);
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
                    carry = newPosition - abcSize + 1;
                    newPosition = newPosition % abcSize;
                    isCarry = true;
                }
            } else if (isCarry){
                newPosition = lastPosition + carry;
                if (newPosition >= abcSize) {
                    carry = newPosition - abcSize + 1;
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
    public void setRotorsIDs(List<Integer> rotorsIDs) {
        selectedRotors = rotorsIDs;
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
        newSecret.setAbcSize(this.abcSize);
        List<Integer> rotorIDs = this.getSelectedRotorsInOrder();
        for (int i = 0; i < rotorIDs.size(); i++)
            newSecret.addRotor(rotorIDs.get(i).intValue(), newPosition.get(i).intValue()-1 );//Postion is 1-based

        return (Secret)newSecret;
    }

    @Override
    public void setABCSize(int abcSize) {
        this.abcSize = abcSize;
    }
}
