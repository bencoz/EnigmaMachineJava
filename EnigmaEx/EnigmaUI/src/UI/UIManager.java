package UI;

import java.io.*;
import java.util.*;
import Logic.*;

public class UIManager {
    private EngineManager Logic;
    private StringBuilder m_menu;
    private int m_requestedChoise;
    private UIEnigmaProfile EnigmaProf;

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
        while(true) {
            int userSelection = getValidUserSelection();
            switch (userSelection) {
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
                    ;
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
                    exit();
                    break;
            }
        }

        /*
        Logic.createEnigmaMachineFromXMLFile(null);
        Logic.getMachine().createSecret()
                .selectRotor(3,'X')
                .selectRotor(2,'D')
                .selectRotor(1,'O')
                .selectReflector(1)
                .create();
        System.out.println(Logic.getMachine().process("WOWCANTBELIEVEITACTUALLYWORKS"));*/
    }

    private int getValidUserSelection() {
        Scanner in = new Scanner(System.in);
        String userInput;
        int userSelection ;

        userInput = in.next();
        while (!tryParseInt(userInput) || !isValidOptionNum(userSelection = Integer.parseInt(userInput)))
        {
            System.out.println("Invalid selection, please try again:");
            userInput = in.next();
        }
        return userSelection;
    }

    private boolean isValidOptionNum(int i) { //TODO : implement
        return true;
    }

    private void init() throws IOException {
        m_menu = new StringBuilder();
        Logic = new EngineManager();
        String line;
        InputStream inputStream;
        inputStream = UIManager.class.getResourceAsStream("/resources/UItext.txt");
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        while((line = in.readLine()) != null) {
            m_menu.append(line);
            System.out.println(line);
        }
    }

    //1.This function creates machine from XML-file
    public boolean createMachineFromXML()
    {
        boolean isValid = Logic.createEnigmaMachineFromXMLFile(null);
        if(isValid) {
            EnigmaProf.setMaxNumOfRotors(Logic.getMaxNumOfRotors());
            EnigmaProf.setActualNamOfRotors(Logic.getActualNumOfRotors());
            EnigmaProf.setRotorsNotch(Logic.getRotorsId_sorted(), Logic.getRotorsNotch_sorted()); //TODO:change to 1-Base
            EnigmaProf.setNumOfReflectors(Logic.getNumOfReflectors());
            EnigmaProf.setNumOfMessages(Logic.getNumOfMassages());
        }
        else
        {

        }
        return isValid;

    }

    public boolean isXml(String xmlPath)
    {
        return xmlPath.endsWith(".xml");
    }

    //2.this function display the machine specifications
    public void DisplayMachineSpec()
    {
        System.out.println(EnigmaProf.toString());
    }

    //3.this function gets the initial code configuration from user
    public void getMachineConfigurationFromUser()
    {
        Scanner in = new Scanner(System.in);
        String userInput;
        List<Integer> chosenRotorsID = new ArrayList<>();
        List<Character> chosenRotorsLoc = new ArrayList<>();
        Integer chosenReflectorID;

        int actualNumOfRotors = Logic.getActualNumOfRotors();

        System.out.println("Please insert your wanted rotors-ID and their init-location:");
        for(int i=0;i<actualNumOfRotors;i++) {
            System.out.format("Rotor number %d\n",i+1);
            chosenRotorsID.add(getValidRotorID());
            chosenRotorsLoc.add(getValidRotorLocation());
        }

        chosenReflectorID = getValidReflectorID();

        setInitialCodeConfiguration(chosenRotorsID,chosenRotorsLoc,chosenReflectorID);
        System.out.println("Your settings have been saved");
    }

    private void setInitialCodeConfiguration(List<Integer> chosenRotorsID, List<Character> chosenRotorsLoc, Integer chosenReflectorID) {
        Logic.setMachineConfig(chosenRotorsID,chosenRotorsLoc,chosenReflectorID);
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
            while (!tryParseInt(userInput)) {
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
            if (userInput.length() == 1 && Logic.isValidABC(userInput) ) {
                rotorLoc=userInput.charAt(0);
                validInput = true;
            }
            else
            {
                System.out.println("INVALID INPUT, rotor location should be legal letter:");
            }
        }
        return rotorLoc;
    }

    public Integer getValidReflectorID() {
        Scanner in = new Scanner(System.in);
        String userInput;
        Integer reflectorID =0;
        boolean validInput = false,validType=false;

        System.out.println("Please enter your wanted reflector-ID:");
        while (!validInput) {
            System.out.print("Reflector ID: ");
            userInput = in.next();

            if(tryParseInt(userInput) && Logic.isReflectorID(Integer.parseInt(userInput))) {
                reflectorID = Integer.parseInt(userInput);
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
        List<Integer> chosenRotorsID ;
        List<Character> chosenRotorsLoc ;
        Integer chosenReflectorID;

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

    public Integer randReflectorID()
    {
        Random rand = new Random();
        return rand.nextInt(Logic.getNumOfReflectors()) +1;
    }

    //5.this function gets input from user and processes it (encrypts or decrypts)
    public void processInput()
    {
        Scanner in = new Scanner(System.in);
        String userInput, output;

        System.out.println("Please enter your wanted message:");
        userInput = in.nextLine();
        while (!Logic.isValidABC(userInput))
        {
            System.out.println("INVALID INPUT. Please enter your wanted message:");
            userInput = in.nextLine();
        }
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

    }

    //8.exit
    public void exit()
    {

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

