package EnigmaMachineFactory;

import EnigmaMachineFactory.Actual.Machine;
import EnigmaMachineFactory.JAXBGenerated.Decipher;
import EnigmaMachineFactory.JAXBGenerated.Enigma;
import EnigmaMachineFactory.JAXBGenerated.Reflector;
import EnigmaMachineFactory.JAXBGenerated.Rotor;

import java.util.List;
import java.util.Map;

public class JAXBToActual {
    private static JAXBToActual ourInstance = new JAXBToActual();

    public static JAXBToActual getInstance() {
        return ourInstance;
    }

    private JAXBToActual() {
    }

    public EnigmaMachineFactory.Actual.Enigma change(Enigma enigma) {
        List<Reflector> JAXBReflectors;
        List<Rotor> JAXBRotors;
        Decipher decipher = enigma.getDecipher();
        EnigmaMachineFactory.Actual.Enigma res = new EnigmaMachineFactory.Actual.Enigma();
        Machine machine = new Machine();

        machine.setAbc(enigma.getMachine().getABC().trim());
        machine.setRotorsCount(enigma.getMachine().getRotorsCount());

        JAXBRotors = enigma.getMachine().getRotors().getRotor();
        JAXBReflectors = enigma.getMachine().getReflectors().getReflector();

        for (Rotor rotor : JAXBRotors){
            EnigmaMachineFactory.Actual.Rotor ActualRotor = new EnigmaMachineFactory.Actual.Rotor();
            ActualRotor.setID(rotor.getId());
            ActualRotor.setNotch(rotor.getNotch()-1);
            ActualRotor.addMappingList(rotor.getMapping());
            machine.addRotor(ActualRotor);
        }

        for (Reflector reflector : JAXBReflectors) {
            EnigmaMachineFactory.Actual.Reflector ActualReflector = new EnigmaMachineFactory.Actual.Reflector();
            ActualReflector.setID(reflector.getId());
            ActualReflector.addReflectList(reflector.getReflect());
            machine.addReflector(ActualReflector);
        }

        if (decipher != null){
            EnigmaMachineFactory.Actual.Decipher ActualDecipher = new EnigmaMachineFactory.Actual.Decipher(decipher.getAgents());
            String words = decipher.getDictionary().getWords().trim();
            ActualDecipher.appedToDictionary(words);
            ActualDecipher.setExcludeChars(decipher.getDictionary().getExcludeChars());
            res.setDecipher(ActualDecipher);
            //TODO: process dictinary words that only exclude chars inside dictionary words
        }

        res.setMachine(machine);
        return res;
    }
}
