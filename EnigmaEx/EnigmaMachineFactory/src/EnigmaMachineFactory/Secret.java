package EnigmaMachineFactory;

import java.util.List;

public interface Secret {

    /**
     * gets list of rotors ids that are in use in this current secret.
     * The order of appearance in the list determines the order of appearance of the rotors in the machine
     * @return list of rotor id's
     */
    List<Integer> getSelectedRotorsInOrder();

    /**
     * gets list of the positions of each rotor.
     * The position at index i is the position of the rotor with the id that appears in index i as returned by the method List<Integer> getSelectedRotorsInOrder()
     * the positions are given in 1'based manner (that is for each position 1 <= position <= alphabet size)
     * @return list of positions of each rotor
     */
    List<Integer> getSelectedRotorsPositions();

    /**
     * gets the id of the selected reflector
     * @return id of selected reflector by this secret object
     */
    int getSelectedReflector();

    /**
     * sets new initial positions for the rotor, as determined and by the order produced by List<Integer> getSelectedRotorsInOrder() invocation
     * Secret is immutable object, so it cannot be changed. this method creates a new instance of secret with the new positioning and the old
     * rotors and reflector selection
     * the positions are given in 1'based manner (that is for each position 1 <= position <= alphabet size)
     * @param newPosition list of new positions to update
     * @return new secret object holding the new positions
     */
    Secret setInitialPosition(List<Integer> newPosition);

    void addRotor(int rotorID, int rotorPostion);

    void setSelectedReflector(int reflectorID);

    void moveToNext(EnigmaMachine enigmaMachine); //TODO:implement

    boolean hasNext(EnigmaMachine enigmaMachine); //TODO:implement

    Secret toZero();

    Secret advanceBy(Integer taskSize);

    void setRotorsIDs(List<Integer> rotorsIDs);
}
