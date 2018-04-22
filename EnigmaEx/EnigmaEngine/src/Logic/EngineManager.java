package Logic;

import EnigmaMachineFactory.*;
import EnigmaMachineFactory.Actual.Reflector;
import EnigmaMachineFactory.Actual.Rotor;

import java.time.Instant;
import java.util.*;

public class EngineManager {
    EnigmaMachine machine;
    EnigmaComponentFactoryImpl factory;
    StatsManager statsManager;
    CodeFormat currentCodeFormat;

    public EngineManager() {
        this.machine = null;
        this.factory = new EnigmaComponentFactoryImpl();
        statsManager = new StatsManager();
    }

    public boolean createEnigmaMachineFromXMLFile(String path) {
        machine = factory.createEnigmaMachineFromXMLFile(path);
        if (checkMachineABC())
            return false;
        if (checkMachineRotorsCount())
            return false;
        if (checkMachineRotors())
            return false;
        if (checkMachineReflectors())
            return false;
        return true;
    }

    private boolean checkMachineReflectors() {
        if (machine == null)
            return false;
        if (checkMachineReflectorsMapping())
            return false;
        List<Reflector> reflectors = machine.getReflectors();
        reflectors.sort((r1,r2) -> r1.getID() - r2.getID());
        for (int i = 0; i< reflectors.size(); i++){
            if (reflectors.get(i).getID() != i+1)
                return false;
        }
        return true;
    }

    private boolean checkMachineRotors() {
        if (machine == null)
            return false;
        if (checkMachineRotorsNotchPosition())
            return false;
        if (checkMachineRotorsMapping())
            return false;

        List<Rotor> rotors = machine.getRotors();
        rotors.sort((r1,r2) -> r1.getID() - r2.getID());
        for (int i = 0; i< rotors.size(); i++){
            if (rotors.get(i).getID() != i+1)
                return false;
        }
        return true;
    }

    private boolean checkMachineRotorsCount() {
        if (machine == null)
            return false;
        int numOfRotors = machine.getNumOfRotors();
        int rotorsCount = machine.getRotorsCount();
        if (rotorsCount < 2 || rotorsCount > numOfRotors)
            return false;
        else
            return true;
    }

    private boolean checkMachineABC() {
        if (machine == null)
            return false;
        return (machine.getABCLength() % 2 == 0);
    }

    public EnigmaMachine getMachine() {
        return machine;
    }

    public int getMaxNumOfRotors()
    {
        return 0;
    }
    public int getActualNumOfRotors()
    {
        return 0;
    }
    public List<Integer> getRotorsId_sorted()
    {
        return null;
    }
    public List<Integer> getRotorsNotch_sorted()
    {
        return null;
    }
    public int getNumOfReflectors()
    {
        return 0;
    }
    public int getNumOfMassages()
    {
        return 0;
    }
    public boolean isCodeInitialized()
    {
        return false;
    }

    public List<Integer> getChosenRotorsID_sorted()
    {
        return null;
    }

    public List<Character> getChosenRotorsLocationInit_Sorted()
    {
        return null;
    }

    public String getChosenReflectorId()
    {
        return null;
    }

    public boolean isValidABC(String code)
    {
        return true;
    }

    public String process(String code)
    {
        CodedStrings codedStrings = new CodedStrings();
        codedStrings.setInput(code);
        Instant start = Instant.now();
        String result = machine.process(code);
        Instant end = Instant.now();
        codedStrings.setOutput(result);
        codedStrings.setDuration(start,end);
        statsManager.addCodedString(currentCodeFormat,codedStrings);
        return result;
    }

    public void resetSystem()
    {

    }

    public boolean isRotorID(Integer rotorID)
    {
        return false;
    }

    public boolean isReflectorID()
    {
        return false;
    }

    public String getReflectorRomanID(int reflectorNum)
    {
        return null;
    }

    public List<Character> getABC() {

        return null;
    }

    public void setMachineConfig(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, String chosenReflectorID) {
    }
}

