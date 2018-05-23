package Logic;

import DecryptionManager.DecipherManager;
import EnigmaMachineFactory.*;
import EnigmaMachineFactory.Actual.Reflector;
import EnigmaMachineFactory.Actual.Rotor;


import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.*;

public class EngineManager {
    private EnigmaMachine machine;
    private EnigmaComponentFactoryImpl factory;
    private StatsManager statsManager;
    private CodeFormat currentCodeFormat;
    private String errorInMachineBuilding;
    private List<String> dictionary;
    private DecipherManager decipherManager = null;
    private boolean decipherAvailable = false;

    public DecipherManager getDecipherManager() {
        return decipherManager;
    }
    public boolean isDecipherAvailable() {
        return decipherAvailable;
    }

    public EngineManager() {
        this.machine = null;
        this.factory = new EnigmaComponentFactoryImpl();
        statsManager = new StatsManager();
    }

    public String getErrorInMachineBuilding() {
        return errorInMachineBuilding;
    }

    public void setDecipher(String i_encrypedCode, DifficultyLevel i_level, Integer i_taskSize, Integer i_numOfAgents){
        if (!decipherAvailable)
            return;
        DecryptionManager.DifficultyLevel newLevel = DecryptionManager.DifficultyLevel.valueOf(i_level.name());

        decipherManager.initFromUser(i_encrypedCode, newLevel, i_taskSize, i_numOfAgents);
    }

    public boolean createEnigmaMachineFromXMLFile(String path) {
        try {
            machine = factory.createEnigmaMachineFromXMLFile(path);
            if (machine.getDecipher() != null) {
                decipherManager = new DecipherManager(machine.deepCopy());
                dictionary = machine.getDecipher().getDictionary();
                decipherAvailable = true;
            }
        } catch (FileNotFoundException e){
            errorInMachineBuilding = "Could not find XML file.";
        }
        if (machine == null) {
            errorInMachineBuilding = "Could not load Machine";
            return false;
        }
        if (!isMachineABCEven()) {
            errorInMachineBuilding = "Machine ABC is not Even";
            return false;
        }
        if (!isMachineRotorsCountOK())
            return false;
        if (!isMachineRotorsOK())
            return false;
        if (!isMachineReflectorsOK())
            return false;

        statsManager.reset();
        return true;
    }

    private boolean isMachineReflectorsOK() {
        if (!isMachineReflectorsDoubleMapping()) {
            errorInMachineBuilding = "One or more of Machines reflectors contains double mapping";
            return false;
        }
        List<Reflector> reflectors = machine.getReflectors();
        reflectors.sort(Comparator.comparingInt(Reflector::getID));
        for (int i = 0; i< reflectors.size(); i++){
            Reflector reflect = reflectors.get(i);
            if (reflect.getID() != i+1) {
                errorInMachineBuilding = "Machine reflectors id's are not in sequential order";
                return false;
            }
            if (reflect.getReflectLength() != getABC().length()) {
                errorInMachineBuilding = "One of machine's reflectors mapping is not the same size as abc";
                return false;
            }
        }
        return true;
    }

    private boolean isMachineReflectorsDoubleMapping() {
        Reflector reflector = null;
        reflector = getMachine().getReflectors().stream().
                filter(Reflector::containsDoubleMapping).
                findAny().
                orElse(null);
        if (reflector == null)
            return true;
        else
            return false;
    }

    private boolean isMachineRotorsOK() {
        if (!isMachineRotorsNotchPositionOK()){
            errorInMachineBuilding = "One of machine's rotors has bad notch position";
            return false;
        }
        if (!isMachineRotorsMappingSizeOK()) {
            errorInMachineBuilding = "One of machine's rotors mapping is not the same size as abc";
            return false;
        }
        if(!isMachineRotorsMappingOK())
        {
            errorInMachineBuilding = "One of machine's rotors mapping letters doesn't contains in the ABC";
            return false;
        }
        if(!isMachineReflectorsMappingOK())
        {
            errorInMachineBuilding = "One of machine's reflector mapping in not valid";
            return false;
        }

        List<Rotor> rotors = machine.getRotors();
        rotors.sort(Comparator.comparingInt(Rotor::getID));
        for (int i = 0; i< rotors.size(); i++){
            if (rotors.get(i).getID() != i+1) {
                errorInMachineBuilding = "Machine rotors id's are not in sequential order";
                return false;
            }
        }
        return true;
    }

    private boolean isMachineRotorsMappingOK() {
        List<Rotor> rotors = getMachine().getRotors();
        for (Rotor rotor : rotors) {
            for(char mapCh : rotor.getMappingABC().toCharArray())
            {
                if(!machine.getABC().contains(String.valueOf(mapCh)))
                    return false;
            }
        }
        return true;
    }


    private boolean isMachineReflectorsMappingOK() {
        List<Reflector> reflectors = getMachine().getReflectors();
        for (Reflector reflector : reflectors) {
            for(int mapInt : reflector.getReflectorMapping())
            {
                if(!((mapInt >= 0) && (mapInt < machine.getABC().length() )))
                    return false;
            }
        }
        return true;
    }

    private boolean isMachineRotorsNotchPositionOK() {
        List<Rotor> rotors = getMachine().getRotors();
        for (Rotor rotor : rotors){
            if (rotor.getNotch() < 0 || rotor.getNotch() > getMachine().getABC().length())
                return false;
        }
        return true;
    }

    private boolean isMachineRotorsMappingSizeOK() {
        List<Rotor> rotors = getMachine().getRotors();
        for (Rotor rotor : rotors) {
            if (rotor.getMappingLength() != getABC().length())
                return false;
        }
        return true;
    }

    private boolean isMachineRotorsCountOK() {
        int numOfRotors = machine.getNumOfRotors();
        int rotorsCount = machine.getRotorsCount();
        if (rotorsCount < 2 || rotorsCount > numOfRotors) {
            errorInMachineBuilding = "Machine's 2 <= rotors-count < the total number of rotors";
            return false;
        }
        else
            return true;
    }



    private boolean isMachineABCEven() {
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
        return statsManager.getTotalNumOfCodedStrings();
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
        statsManager.addCodedString(currentCodeFormat,codedStrings);
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
        return machine.getWorkingReflectorRomanID();
    }

    public String getABC()
    {
        return machine.getABC();
    }

    public void setMachineConfig(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, Integer chosenReflectorID) {
        SecretBuilder secretBuilder = machine.createSecret();
        for(int i = 0; i < chosenRotorsID.size(); i++) {
            secretBuilder.selectRotor(chosenRotorsID.get(i), chosenRotorsLoc.get(i));
        }
        secretBuilder.selectReflector(chosenReflectorID);
        secretBuilder.create();
        currentCodeFormat = new CodeFormat(chosenRotorsID, chosenRotorsLoc, toRoman(chosenReflectorID));
    }

    private String toRoman(Integer numID) {
        String id;
        switch (numID) {
            case 1:
                id = "I";
                break;
            case 2:
                id = "II";
                break;
            case 3:
                id = "III";
                break;
            case 4:
                id = "IV";
                break;
            case 5:
                id = "V";
                break;
            default:
                id = "";
        }
        return id;
    }

    public String getAllStats() {
        StringBuilder sb = new StringBuilder();
        if(statsManager.getTotalNumOfCodedStrings()>0) {
            sb.append(statsManager.getDiconaryToString());
            sb.append("Average time taken to process: ");
            sb.append(statsManager.getAvarageTimeForCoding());
            return sb.toString();
        }
        else
            return "No messages received yet";
    }

    public boolean isInDictionary(String userInput) {
        if (!decipherAvailable)
            return true;
        boolean found;
        String[] words = userInput.split(" ");
        for (String word : words){
            found = false;
            for (String permittedWord : dictionary){
                if (permittedWord.equals(word)) {
                    found = true;
                    break;
                }
            }
            if (!found)
                return false;
        }
        return true;
    }

    public void startDecipher() {
        this.decipherManager.start();
    }

    public boolean isValidTaskSize(Integer taskSize, DifficultyLevel difficulty, Integer numOfAgents) {
        DecryptionManager.DifficultyLevel newLevel = DecryptionManager.DifficultyLevel.valueOf(difficulty.name());
        return decipherManager.isValidTaskSize(taskSize,newLevel,numOfAgents);
    }

    public boolean isValidNumOfAgents(Integer numOfAgents) {
        return decipherManager.isValidNumOfAgents(numOfAgents);
    }

    public Double getNumOfOptionsforDecoding(DifficultyLevel difficulty) {
        DecryptionManager.DifficultyLevel newLevel = DecryptionManager.DifficultyLevel.valueOf(difficulty.name());
        return decipherManager.getNumOfOptionsforDecoding(newLevel);
    }

    public void pauseDeciphering() {
        this.decipherManager.pauseDeciphering();
    }

    public void continueDeciphering() {
        this.decipherManager.continueDeciphering();
    }

    public void stopDeciphering() {
        this.decipherManager.stopDeciphering();
    }

    public void printDecipheringProgressMode() {
        this.decipherManager.printDecipheringProgressMode();
    }

    public void initDecipher() {
        if (machine.getDecipher() != null) {
            decipherManager = new DecipherManager(machine.deepCopy());
            dictionary = machine.getDecipher().getDictionary();
            decipherAvailable = true;
        }
    }
}

