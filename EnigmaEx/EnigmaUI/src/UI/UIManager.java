package UI;

import java.io.*;
import java.util.*;

import EnigmaMachineFactory.JAXBGenerated.Decipher;
import Logic.*;
import sun.rmi.runtime.Log;

public class UIManager {
    private EngineManager Logic;
    private StringBuilder m_menu;
    private StringBuilder m_subMenu;
    private UIEnigmaProfile EnigmaProf;
    private boolean gameIsRunning = true;
    private boolean isAutomaticDecoding = false;
    private boolean isMachineSet = false;
    private boolean isConfigSet = false;
    private int numOfOptions = 9;
    private int numOfOptions_SM = 3;
    private Decipher decipher;
    private boolean decipheringIsPause = false;

    public static void main(String[] args) throws FileNotFoundException {
        UIManager manager = new UIManager();
        try {
            manager.init();
        } catch (IOException e) {
            e.printStackTrace();
        }
        manager.run();
    }

    public UIManager()
    {
        EnigmaProf = new UIEnigmaProfile();
    }
    private void run() {
        while(gameIsRunning) {
            int userSelection = getValidUserSelection();
            switch (userSelection) {
                case 0:
                    displayMenu();
                    break;
                case 1:
                    createMachineFromXML();
                    DisplayMachineSpec();
                    break;
                case 2:
                    DisplayMachineSpec();
                    break;
                case 3:
                    getMachineConfigurationFromUser();
                    break;
                case 4:
                    randomChooseMachineConfiguration();
                    break;
                case 5:
                    processInput();
                    break;
                case 6:
                    resetSystem();
                    break;
                case 7:
                    DisplayStatisticsAndHistory();
                    break;
                case 8:
                    automaticDecoding();
                    displayMenu();
                    break;
                case 9:
                    exit();
                    break;
            }
            if(userSelection != 1 && userSelection != 8)
                System.out.println("Waiting for the next command (press 0 to display menu)");
        }
    }

    private void automaticDecoding() {
        Logic.initDecipher();
        if (!Logic.isDecipherAvailable())
            return; //TODO: change and show msg
        String code = getValidCodeFromUser();
        DifficultyLevel difficulty = getValidDifficultyLevel();
        DisplayNumOfOptions(difficulty);
        Integer numOfAgents = getValidNumOfAgents();
        Integer taskSize = getValidTaskSize(difficulty,numOfAgents);
        boolean needToStart = getValidStartSignal();
        //TODO:: check if machine is initialize
        if(needToStart) {
            String precessedCode = Logic.process(code);
            Logic.setDecipher(precessedCode, difficulty, taskSize, numOfAgents);
            Logic.startDecipher();
            try {
                initSubmenu();
                isAutomaticDecoding = needToStart;
                runSubMenu();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }//TODO:implement

    private void runSubMenu() {
        while (isAutomaticDecoding) {
            int userSelection = getValidUserSelection_SM();
            switch (userSelection) {
                case 0:
                    displaySubmenu();
                    break;
                case 1:
                    printDecipheringProgressMode();
                    break;
                case 2:
                    if (decipheringIsPause)
                        continueDeciphering();
                    else
                        pauseDeciphering();
                    break;
                case 3:
                    stopDeciphering();
                    isAutomaticDecoding = false;
                    break;
                case 4:
                    break;
            }
            if (!Logic.isDecipherManagerAlive()) {
                break;
            }
            System.out.println("Waiting for the next command (press 0 to display menu)");
        }
    }
    
    private int getValidUserSelection_SM() {
        Scanner in = new Scanner(System.in);
        String userInput;
        int userSelection = 0;

        userInput = in.next();
        if (!Logic.isDecipherManagerAlive())
            return 4;
        while (!isValidOptionNum(userInput, numOfOptions_SM)) {
            System.out.println("This option doesn't exist in the system, please try again:");
            userInput = in.next();
        }
        userSelection = Integer.parseInt(userInput);


        return userSelection;
    }

    private void pauseDeciphering(){
        Logic.pauseDeciphering();
        this.decipheringIsPause = true;
    }

    private void continueDeciphering(){
        Logic.continueDeciphering();
        this.decipheringIsPause = false;
    }

    private void stopDeciphering() {
        Logic.stopDeciphering();
    }

    private void printDecipheringProgressMode()
    {
        Logic.printDecipheringProgressMode();
    }

    private boolean getValidStartSignal() {
        Scanner in = new Scanner(System.in);
        String userInput;
        boolean toStart = false, valid = false;
        while (!valid)
        {
            System.out.println("Please enter 'S' to start the automatic decoding and 'Q' to get back to menu");
            userInput = in.next();
            userInput = userInput.toUpperCase();
            if (userInput.length() == 1 && (userInput.charAt(0) == 'S' || userInput.charAt(0) == 'Q')){
                valid = true;
                if(userInput.charAt(0) == 'S')
                    toStart = true;
            }
            else
            {
                System.out.println("INVALID INPUT, try again:");
            }
        }
        return toStart;
    }

    private Integer getValidTaskSize(DifficultyLevel difficulty, Integer numOfAgents) {
        //need to check task size is less than num of options
        Scanner in = new Scanner(System.in);
        boolean validInput=false, validType=true;
        Integer taskSize = 0;
        String userInput;

        System.out.print("please enter your wanted size of agent task:");
        while(!validInput) {
            validType = true;
            userInput = in.next();
            if (!tryParseInt(userInput)) {
                System.out.println("INVALID INPUT, please enter a NUMBER that represents size of agent task:");
                validType = false;
            }
            if (validType) {
                taskSize = Integer.parseInt(userInput);
                if (Logic.isValidTaskSize(taskSize,difficulty,numOfAgents)) {
                    validInput = true;
                }
                else {
                    System.out.println("INVALID INPUT, task size doesn't match the current number of options and agents, try again:");
                }
            }
        }
        return taskSize;
    }

    private Integer getValidNumOfAgents() {
        //need to check is less than man num of agent(gotten from XML file)
        Scanner in = new Scanner(System.in);
        boolean validInput=false, validType=true;
        Integer numOfAgents = 0;
        String userInput;

        System.out.print("please enter your wanted number of agents:");
        while(!validInput) {
            validType = true;
            userInput = in.next();
            if (!tryParseInt(userInput)) {
                System.out.println("INVALID INPUT, please enter a NUMBER that represents number of agents:");
                validType = false;
            }
            if (validType) {
                numOfAgents = Integer.parseInt(userInput);
                if (Logic.isValidNumOfAgents(numOfAgents)) {
                    validInput = true;
                }
                else {
                    System.out.println("INVALID INPUT, num of agents need to be between 2 and max num of agents, try again:");
                }
            }
        }
        return numOfAgents;
    }

    private void DisplayNumOfOptions(DifficultyLevel difficulty) {
        //need to calculate num of option according to difficulty level and the size and properties of the machine
        System.out.println("The number of possible options, considering the level of difficulty and size of the machine is:");
        System.out.println(Logic.getNumOfOptionsforDecoding(difficulty).intValue());
    }

    private DifficultyLevel getValidDifficultyLevel() {
        Scanner in = new Scanner(System.in);
        String userInput;
        DifficultyLevel difficulty = DifficultyLevel.Easy;
        boolean validInput = false,validType=false;

        System.out.println("Please enter difficulty level:");
        System.out.println("press 'E' for easy, 'M' for medium, 'H' for hard and 'I' for impossible");

        while (!validInput) {
            userInput = in.next().toUpperCase();

            if(DifficultyLevel.isDifficultyLevel(userInput)){
                difficulty = DifficultyLevel.getDifficultyLevelByCharVal(userInput.charAt(0));
                validInput = true;
            }
            else
                System.out.println("INVALID INPUT, please try again:");
        }
        return difficulty;
    }

    private String getValidCodeFromUser() {
        Scanner in = new Scanner(System.in);
        String userInput;

        System.out.println("Please enter your wanted message:");
        userInput = in.nextLine();
        userInput = userInput.toUpperCase();
        while (!Logic.isValidABC(userInput) || !Logic.isInDictionary(userInput))
        {
            System.out.println("INVALID INPUT. Please enter your wanted message:");
            userInput = in.nextLine();
            userInput = userInput.toUpperCase();
        }
        return userInput;
    }

    private int getValidUserSelection() {
        Scanner in = new Scanner(System.in);
        String userInput;
        int userSelection = 0;
        boolean isValid = false;

        while (!isValid) {
            userInput = in.next();
            while (!isValidOptionNum(userInput, numOfOptions)) {
                System.out.println("This option doesn't exist in the system, please try again:");
                userInput = in.next();
            }
            userSelection = Integer.parseInt(userInput);
            if (isValidOptionPriority(userSelection)) {
                isValid = true;
            }
        }
        return userSelection;
    }


    private boolean isValidOptionPriority(int userSelection) {
        boolean isValid = true;
        if(userSelection == 0 || userSelection == 9)
            return true;
        if(!isMachineSet && userSelection != 1)
        {
            System.out.println("Please load the machine first");
            isValid = false;
        }
        else if(!isConfigSet && (userSelection >= 5 && userSelection <= 7))
        {
            System.out.println("Please set the machine configuration first");
            isValid = false;
        }
        return isValid;
    }

    private boolean isValidOptionNum(String userInput, Integer wantedNumOfOptions) {
        int userSelection =0 ;
        if(!tryParseInt(userInput))
            return false; //invalid type
        else
        {
            userSelection = Integer.parseInt(userInput);
            if((userSelection>=0)&&(userSelection<=wantedNumOfOptions))
                return true;
        }
        return false;
    }

    private void initSubmenu() throws IOException {
        m_subMenu = new StringBuilder();
        String line;
        InputStream inputStream;
        inputStream = UIManager.class.getResourceAsStream("/resources/UIsubMenu.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        while((line = in.readLine()) != null) {
            m_subMenu.append(line).append("\n");
            System.out.println(line);
        }
    }

    private void init() throws IOException {
        m_menu = new StringBuilder();
        Logic = new EngineManager();
        String line;
        InputStream inputStream;
        inputStream = UIManager.class.getResourceAsStream("/resources/UItext.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        while((line = in.readLine()) != null) {
            m_menu.append(line).append("\n");
            System.out.println(line);
        }
    }

    private void displayMenu()
    {
        System.out.println(m_menu);
    }

    private void displaySubmenu()
    {
        System.out.println(m_subMenu);
    }

    //1.This function creates machine from XML-file
    public void createMachineFromXML() {
        boolean isValid = false;
        String path = getEnigmaMachinePath();
        isValid = Logic.createEnigmaMachineFromXMLFile(path);

        while (!isValid) {
            System.out.println("XML-file is Invalid\n" + Logic.getErrorInMachineBuilding());
            path = getEnigmaMachinePath();
            isValid = Logic.createEnigmaMachineFromXMLFile(path);
        }
        isMachineSet = true;
        if(isMachineSet) //not the first machine
        {
            isConfigSet = false;
            EnigmaProf.resetConfig();
        }
        isMachineSet = true;
        EnigmaProf.setMaxNumOfRotors(Logic.getMaxNumOfRotors());
        EnigmaProf.setActualNamOfRotors(Logic.getActualNumOfRotors());
        EnigmaProf.setRotorsNotch(Logic.getRotorsId_sorted(), Logic.getRotorsNotch_sorted()); //TODO:change to 1-Base
        EnigmaProf.setNumOfReflectors(Logic.getNumOfReflectors());
        EnigmaProf.setNumOfMessages(Logic.getNumOfMassages());
    }

    private String getEnigmaMachinePath() {
        Scanner in = new Scanner(System.in);
        String path;
        System.out.println("Please enter your Enigma Machine XML file Path:");

        path = in.nextLine();
        while (!isXml(path)){
            System.out.println("Please try again (file must end with .xml)...:");
            path = in.next();
        }
        return path;
    }

    public boolean isXml(String xmlPath)
    {
        return xmlPath.endsWith(".xml");
    }

    //2.this function display the machine specifications
    public void DisplayMachineSpec()
    {
        EnigmaProf.setNumOfMessages(Logic.getNumOfMassages());
        System.out.println(EnigmaProf.toString());
    }

    //3.this function gets the initial code configuration from user
    public void getMachineConfigurationFromUser()
    {
        isConfigSet = true;
        Scanner in = new Scanner(System.in);
        String userInput;
        List<Integer> chosenRotorsID = new ArrayList<>();
        Integer currRotorID;
        List<Character> chosenRotorsLoc = new ArrayList<>();
        RomanDigit chosenReflectorID;

        int actualNumOfRotors = Logic.getActualNumOfRotors();

        System.out.println("Please insert your wanted rotors-ID and their init-location:");
        for(int i=0;i < actualNumOfRotors; i++) {
            System.out.format("Rotor number %d\n",i+1);
            currRotorID = getValidRotorID();
            while(chosenRotorsID.contains(currRotorID)) {
                System.out.println("This rotor already selected, please try again:");
                currRotorID = getValidRotorID();
            }
            //the rotors is get from the left one to the right one
            chosenRotorsID.add(currRotorID);
            chosenRotorsLoc.add(getValidRotorLocation());
        }
        Collections.reverse(chosenRotorsID);
        Collections.reverse(chosenRotorsLoc);
        chosenReflectorID = getValidReflectorID();

        setInitialCodeConfiguration(chosenRotorsID,chosenRotorsLoc,chosenReflectorID);
        System.out.println("Your settings have been saved");
    }

    private void setInitialCodeConfiguration(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, RomanDigit chosenReflectorID) {
        Logic.setMachineConfig(chosenRotorsID,chosenRotorsLoc,chosenReflectorID.getIntValue());
        EnigmaProf.setAsInitial();
        EnigmaProf.setInitialCodeConfiguration(chosenRotorsID,chosenRotorsLoc,chosenReflectorID);
    }


    public Integer getValidRotorID()
    {
        Scanner in = new Scanner(System.in);
        boolean validInput=false, validType=true;
        Integer rotorID = 0;
        String userInput;

        while(!validInput) {
            validType = true;
            System.out.print("Rotor ID: ");
            userInput = in.next();
            if (!tryParseInt(userInput)) {
                System.out.println("INVALID INPUT, please enter a NUMBER that represents Rotor ID");
                validType = false;
            }
            if (validType) {
                rotorID = Integer.parseInt(userInput);
                if (Logic.isRotorID(rotorID)) {
                    validInput = true;
                }
                else {
                    System.out.println("INVALID INPUT, rotor ID doesn't exist in the system, try again:");
                }
            }
        }
        return rotorID;
    }


    public Character getValidRotorLocation()
    {
        Scanner in = new Scanner(System.in);
        String userInput;
        Character rotorLoc = ' ';
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Rotor location: ");
            userInput = in.next();
            userInput = userInput.toUpperCase();
            if (userInput.length() == 1 && Logic.isValidABC(userInput) ) {
                rotorLoc = userInput.charAt(0);
                validInput = true;
            }
            else
            {
                System.out.println("INVALID INPUT, rotor location should be legal letter:");
            }
        }
        return rotorLoc;
    }

    public RomanDigit getValidReflectorID() {
        Scanner in = new Scanner(System.in);
        String userInput;
        RomanDigit reflectorID = RomanDigit.I;
        boolean validInput = false,validType=false;

        System.out.println("Please enter your wanted reflector-ID (Roman digits):");
        while (!validInput) {
            System.out.print("Reflector ID: ");
            userInput = in.next();

            if(RomanDigit.isRomanDig(userInput)){
                reflectorID = RomanDigit.valueOf(userInput);
                validInput = true;
            }
            else
                System.out.println("INVALID INPUT, please try again:");
        }
        return reflectorID;
    }

    //4.this function randomly selects the initial code configuration and displays this to the user
    public void randomChooseMachineConfiguration()
    {
        isConfigSet = true;
        List<Integer> chosenRotorsID ;
        List<Character> chosenRotorsLoc ;
        RomanDigit chosenReflectorID;

        chosenRotorsID = randRotorsID();
        chosenRotorsLoc = randRotorsLoc();
        chosenReflectorID = randReflectorID();

        setInitialCodeConfiguration(chosenRotorsID,chosenRotorsLoc,chosenReflectorID);
        System.out.println("The settings have been updated");

    }

    public List<Integer> randRotorsID()
    {
        int actualNumOfRotors = Logic.getActualNumOfRotors();
        List<Integer> rotorsID  = Logic.getRotorsId_sorted();
        Collections.shuffle(rotorsID);

        List<Integer> chosenRotorsID  = new ArrayList<>(rotorsID.subList(0,actualNumOfRotors));
        return chosenRotorsID;
    }

    public List<Character> randRotorsLoc ()
    {
        int actualNumOfRotors = Logic.getActualNumOfRotors();
        List<Character> ABC = new ArrayList<>();
        for (Character ch:Logic.getABC().toCharArray())
        {
            ABC.add(ch);
        }
        List<Character> rotorsLoc = new ArrayList<>();
        Random rand = new Random();

        for(int i=0;i<actualNumOfRotors;i++)
        {
            int randomChoice = rand.nextInt(ABC.size());
            rotorsLoc.add((ABC.get(randomChoice)));
        }
        return rotorsLoc;
    }

    public RomanDigit randReflectorID() {
        Random rand = new Random();
        int val = rand.nextInt(Logic.getNumOfReflectors()) + 1;
        return RomanDigit.getRomanDigByIntVal(val);
    }

        //5.this function gets input from user and processes it (encrypts or decrypts)
    public void processInput()
    {
        String output;
        String userInput = getValidCodeFromUser();
        output = Logic.process(userInput);
        System.out.println("your processed message:");
        System.out.println(output);
    }

    //6.this function reset the system to it's init configuration
    public void resetSystem()
    {
        Logic.resetSystem();
    }

    //7.this function will display basic statistics and history of the system
    public void DisplayStatisticsAndHistory()
    {
        String text = Logic.getAllStats();
        System.out.println(text);
    }

    //8.exit
    public void exit()
    {
        gameIsRunning = false;
        System.out.println("Bye Bye :(");
    }


    public boolean tryParseInt(String value) {
        try {
            Integer.parseInt(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

