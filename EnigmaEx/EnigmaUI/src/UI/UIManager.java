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

    private void run() {
        Logic.createEnigmaMachineFromXMLFile(null);
        Logic.getMachine().createSecret()
                .selectRotor(3,'X')
                .selectRotor(2,'D')
                .selectRotor(1,'O')
                .selectReflector(1)
                .create();
        System.out.println(Logic.getMachine().process("WOWCANTBELIEVEITACTUALLYWORKS"));
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

    //1.This function get a path of XML-file and do a basic checking (is xml)
    public boolean isXml(String xmlPath)
    {
        return xmlPath.endsWith(".xml");
    }

    //2.this function display the machine specifications
    public void DisplayMachineSpec()
    {
        EnigmaProf = new UIEnigmaProfile();
        EnigmaProf.setMaxNumOfRotors(Logic.getMaxNumOfRotors());
        EnigmaProf.setActualNamOfRotors(Logic.getActualNumOfRotors());
        EnigmaProf.setRotorsNotch(Logic.getRotorsId_sorted(),Logic.getRotorsNotch_sorted());
        EnigmaProf.setNumOfReflectors(Logic.getNumOfReflectors());
        EnigmaProf.setNumOfMessages(Logic.getNumOfMassages());
        if(Logic.isCodeInitialized()) {
            EnigmaProf.setAsInitial();
            EnigmaProf.setInitialCodeConfiguration(Logic.getChosenRotorsID_sorted(), Logic.getChosenRotorsLocationInit_Sorted(),
                    Logic.getChosenReflectorId());
        }
        System.out.println(EnigmaProf.toString());
    }

    //3.this function gets the initial code configuration from user
    public void getMachineConfigurationFromUser()
    {
        Scanner in = new Scanner(System.in);
        String userInput;
        List<Integer> chosenRotorsID = new ArrayList<>();
        List<Character> chosenRotorsLoc = new ArrayList<>();
        String chosenReflectorID;

        int actualNumOfRotors = Logic.getActualNumOfRotors();

        System.out.println("Please insert your wanted rotors-ID and their init-location:");
        for(int i=0;i<actualNumOfRotors;i++) {
            System.out.format("rotor number %d\n",i);
            chosenRotorsID.add(getValidRotorID());
            chosenRotorsLoc.add(getValidRotorLocation());
        }

        chosenReflectorID = getValidReflectorID();

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
            if (userInput.length() == 1) {
                if(Logic.isValidABC(userInput))
                {
                    rotorLoc=userInput.charAt(0);
                    validInput = true;
                }
            }
            else
            {
                System.out.println("INVALID INPUT, rotor location should be legal letter:");
            }
        }
        return rotorLoc;
    }

    public String getValidReflectorID() {
        Scanner in = new Scanner(System.in);
        String userInput;
        String reflectorID = null;
        boolean validInput = false,validType=false;

        System.out.println("Please enter your wanted reflector-ID:");
        while (!validInput) {
            System.out.print("Reflector ID: ");
            userInput = in.next();

            if(tryParseInt(userInput) && Logic.isReflectorID(Integer.parseInt(userInput)))
                reflectorID = userInput;
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


        Logic.setMachineConfig(chosenRotorsID,chosenRotorsLoc,chosenReflectorID);
        EnigmaProf.setAsInitial();
        EnigmaProf.setInitialCodeConfiguration(chosenRotorsID,chosenRotorsLoc,chosenReflectorID);

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

    public String randReflectorID()
    {
        Random rand = new Random();
        int reflectorNum = rand.nextInt(Logic.getNumOfReflectors()) +1;
        return Logic.getReflectorRomanID(reflectorNum);
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
    public void displayStatisticsAndHistory()
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

