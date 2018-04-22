package EnigmaMachineFactory;

import EnigmaMachineFactory.Actual.Reflector;
import EnigmaMachineFactory.Actual.Rotor;

import java.util.List;
import java.util.function.Consumer;


public interface EnigmaMachine {

    /**
     * creates a Secret object, that will hold all relevant configuration for the machine to encrypt\decrypt data
     * Secret object is immutable, and constructed using a builder
     * @return a SecretBuilder object, using it to build and construct the actual Secret objec
     */
    SecretBuilder createSecret();

    /**
     * gets the current Secret object used by this machine instance
     * @return the Secret object
     */
    Secret getSecret();

    /**
     * configure this machine instance to use the current Secret
     * @param secret secret object to configure the machine with
     */
    void initFromSecret(Secret secret);

    /**
     * resets the rotors to their initial position, as determined by the current secret
     */
    void resetToInitialPosition();

    /**
     * sets the rotors position as given by a string of chars, each should match to the rotors by order of appearance (first char -> first rotor etc.)
     * the characters in the given string are expected each to be part of the input wiring of the rotor
     * this method also updates the current Secret object used inside the machine.
     * @param position String that holds all the a single char to configure each rotor within.
     */
    void setInitialPosition(String position);

    /**
     * process String of characters in the machine, using the current configured Secret
     * calls within for char process(char character) for each of the chars in the input string and by the same order
     * @param plainText string of characters to process
     * @return String of characters after processing
     */
    String process(String plainText);

    /**
     * processes a signle character in the machine, using the current configured Secret
     * @param character The current char to process
     * @return the result of the char processing by the machine
     */
    char process(char character);

    /**
     * sets the debug mode of the machine.
     * default debug mode is false. once debug is set to true, upon each character processing (as performed in the method char process(char character))
     * additional information will be logged to the console that will put some light on the machine internal processing
     * @param debug when true the machine will enter debug mode.
     */
    void setDebug(boolean debug);

    /**
     * prints the current status of the machine, as determined by it's secret:
     * 1. selected rotors and their position, along with notch positioning
     * 2. selected reflector id
     * @param stateConsumer consumer that will accept the string holding the information
     */
    void consumeState(Consumer<String> stateConsumer);

    //get the max num of rotors
    int getNumOfRotors();

    //get the actual num of rotors
    int getRotorsCount();

    List<Integer> getRotorsId_sorted();

    List<Integer> getRotorsNotch_sorted();

    int getNumOfReflectors();

    int getNumOfMassages();

    boolean isCodeInitialized();

    List<Integer> getChosenRotorsID_sorted();

    List<Character> getChosenRotorsLocationInit_Sorted();

    Integer getChosenReflectorId();

    String getReflectorRomanID(int reflectorNum);

    List<Integer> getReflectorsId();

    String getABC();

    List<Rotor> getRotors();

    List<Reflector> getReflectors();
}
