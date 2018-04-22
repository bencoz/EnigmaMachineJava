package EnigmaMachineFactory;

import EnigmaMachineFactory.Actual.Reflector;
import EnigmaMachineFactory.Actual.Rotor;

public interface EnigmaComponentFactory {
    EnigmaComponentFactory INSTANCE = new EnigmaComponentFactoryImpl();

    /**
     * creates an Enigma machine builder object, used to construct a new machine instance
     * @param rotorsCount number of rotors the machine will use for it's processing
     * @param alphabet the set of valid characters the machine can accept as input and return as output
     * @return EnigmaMachineBuilder instance to be used to construct a new Enigma Machine
     */
    EnigmaMachineBuilder buildMachine(int rotorsCount, String alphabet);

    /**
     * static method used to create a new rotor instance.
     * the wiring of this rotor is determined by the mapping between each char in the input wiring
     * corresponding to the relevant char at the output wiring
     * The notch will of this rotor will be created randomly.
     * @param id rotor id
     * @param source source input string
     * @param target target output string
     * @return a new Rotor instance
     */
    Rotor createFromString(int id, String source, String target);

    /**
     * static method used to create a new rotor instance.
     * the wiring of this rotor is determined by the mapping between each char in the input wiring
     * corresponding to the relevant char at the output wiring
     * the notch is given in 1'based manner: 0 < notch <= source.length
     * @param id rotor id
     * @param source source input string
     * @param target target output string
     * @param notch the position of the notch in this rotor
     * @return a new Rotor instance
     */
    Rotor createFromString(int id, String source, String target, int notch);

    /**
     * static method to create a new reflector instance
     * reflector is defined by a set of two equal-by-size byte arrays, that defines the mapping (reflection)
     * by their corresponding indices (e.g. for each i, input[i] -> output [i])
     * the content of the array needs to be 1'based
     * the length of each array needs to be half of the size of the machine alphabet, where this reflector is about to serve it's duty.
     * the mapping is bi-directional so there is no special meaning for the terms 'input' and 'output' in this case
     * @param id the id of the reflector
     * @param input byte array serves as the input
     * @param output byte array serves as the output
     * @return a new reflector instance
     */
    Reflector createReflector(int id, byte[] input, byte[] output);
}
