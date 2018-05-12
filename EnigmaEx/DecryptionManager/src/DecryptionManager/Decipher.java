package DecryptionManager;

import EnigmaMachineFactory.*;
import java.util.*;
import AgentModule.*;

public class Decipher {
    private Map dictionary = new HashMap();
    private String excludeWords;
    private Integer agents;
    private EnigmaMachine machine; //copy of the machine
    private AgentFactory agentFactory;
    private List <Agent> agentsList;
    private DecipherMission mission;

    //c'tor gets dictionary and machine
    public Decipher(Map dictionary, EnigmaMachine machine){} //TODO:implement

    //get new mission from the mission factory
    public void getNewMission(){} //TODO:implement

    //Solves the task and return list of strings that are decoding candidacies
    public List<String> getCandidaciesForDecoding(String Code){} //TODO:implement

    //this function is responsible for crate new threads(for each agent),
    //and than ask for agent factory to create new agents
    public void createAgent(EnigmaMachine machine, String code){} //TODO:implement




}
