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
}
