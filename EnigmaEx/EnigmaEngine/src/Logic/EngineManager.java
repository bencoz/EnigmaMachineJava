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
        machine = factory.createEnigmaMachineFromXMLFile(path);//TODO:change names
/*
        if (!checkMachineABC())
            return false;
        if (!checkMachineRotorsCount())
            return false;
        if (!checkMachineRotors())
            return false;
        if (!checkMachineReflectors())
            return false;
*/
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

    private boolean checkMachineReflectorsMapping() {
        //TODO : implement
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
        rotors.sort(Comparator.comparingInt(Rotor::getID));
        for (int i = 0; i< rotors.size(); i++){
            if (rotors.get(i).getID() != i+1)
                return false;
        }
        return true;
    }

    private boolean checkMachineRotorsNotchPosition() {
        //TODO : implement
        return true;
    }

    private boolean checkMachineRotorsMapping() {
        //TODO : implement
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
        return (machine.getABC().length() % 2 == 0);
    }

    public EnigmaMachine getMachine() {
        return machine;
    }

    public int getMaxNumOfRotors()
    {
        return machine.getNumOfRotors();
    }
    public int getActualNumOfRotors()
    {
        return machine.getRotorsCount();
    }
    public List<Integer> getRotorsId_sorted()
    {
        return machine.getRotorsId_sorted();
    }
    public List<Integer> getRotorsNotch_sorted()
    {
        return machine.getRotorsNotch_sorted();
    }
    public int getNumOfReflectors()
    {
        return machine.getNumOfReflectors();
    }
    public int getNumOfMassages()
    {
        return machine.getNumOfMassages();
    }
    public boolean isCodeInitialized()
    {
        return machine.isCodeInitialized();
    }

    public List<Integer> getChosenRotorsID_sorted()
    {
        return machine.getChosenRotorsID_sorted();
    }

    public List<Character> getChosenRotorsLocationInit_Sorted()
    {
        return machine.getChosenRotorsLocationInit_Sorted();
    }

    public Integer getChosenReflectorId()
    {
        return machine.getChosenReflectorId();
    }

    public boolean isValidABC(String code)
    {
        for (int i =0; i< code.length(); ++i){
            String ch = String.valueOf(code.charAt(i));
            if (!getABC().contains(ch))
                return false;
        }
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
        //statsManager.addCodedString(currentCodeFormat,codedStrings);
        return result;
    }

    public void resetSystem()
    {
        machine.resetToInitialPosition();
    }

    public boolean isRotorID(Integer rotorID)
    {
        boolean isRotorId=false;
        List<Integer> RotorsId = machine.getRotorsId_sorted();
        for(Integer id : RotorsId)
        {
            if(id == rotorID)
            {
                isRotorId = true;
                break;
            }
        }
        return isRotorId;
    }

    public boolean isReflectorID(Integer reflectorID)
    {
        boolean isReflectorId=false;
        List<Integer> ReflectorsId = machine.getReflectorsId();
        for(Integer id : ReflectorsId)
        {
            if(id == reflectorID)
            {
                isReflectorId = true;
                break;
            }
        }
        return isReflectorId;
    }

    public String getReflectorRomanID(int reflectorNum)
    {
        return machine.getReflectorRomanID(reflectorNum);
    }

    public String getABC()
    {
        return machine.getABC();
    }

    //TODO: need to add to statsManager?
    public void setMachineConfig(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, Integer chosenReflectorID) {
        SecretBuilder secretBuilder = machine.createSecret();
        for(int i=0;i<chosenRotorsID.size();i++) {
                    secretBuilder.selectRotor(chosenRotorsID.get(i), chosenRotorsLoc.get(i));
        }
        secretBuilder.selectReflector(chosenReflectorID);
        secretBuilder.create();
        currentCodeFormat = new CodeFormat(chosenRotorsID, chosenRotorsLoc, chosenReflectorID);
    }
}

