package Logic;

import EnigmaMachineFactory.*;

import java.util.*;

public class EngineManager {
    EnigmaMachine machine;
    EnigmaComponentFactoryImpl factory;

    public EngineManager() {
        this.machine = null;
        this.factory = new EnigmaComponentFactoryImpl();
    }

    public void createEnigmaMachineFromXMLFile(String path) {
        machine = factory.createEnigmaMachineFromXMLFile(path);
    }

    public EnigmaMachine getMachine() {
        return machine;
    }

    public int getMaxNumOfRotors()
    {
        return machine.getMaxNumOfRotors();
    }
    public int getActualNumOfRotors()
    {
        return machine.getActualNumOfRotors();
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
        for (char ch: code.toCharArray()) {
            if(!isValidChar(ch))
                return false;
        }
        return true;
    }

    private boolean isValidChar(char ch)
    {
        for(char legCh : getABC().toCharArray())
        {
            if(legCh == ch)
                return true;
        }
        return false;
    }


    public String process(String code)
    {
        return machine.process(code);
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

    public void setMachineConfig(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, Integer chosenReflectorID) {
        SecretBuilder secretBuilder = machine.createSecret();
        for(int i=0;i<chosenRotorsID.size();i++) {
                    secretBuilder.selectRotor(chosenRotorsID.get(i), chosenRotorsLoc.get(i));
        }
        secretBuilder.selectReflector(chosenReflectorID);
        secretBuilder.create();
    }
}

