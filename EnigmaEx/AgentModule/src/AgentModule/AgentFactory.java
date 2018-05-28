package AgentModule;


import EnigmaMachineFactory.*;

import java.util.List;
import java.util.concurrent.BlockingQueue;

public class AgentFactory {
    private static int nextAgentID = 0;
    //this function create and return new agent, (maybe give him task?)
    public static Agent createAgent(EnigmaMachine _machine, String _code, BlockingQueue<AgentResponse> _answersToDM_Queue,
                                    DecipheringStatus _DMstatus, Integer blockSize, List<String> _dictionary){
        Agent resAgent = new Agent(_machine, _code, nextAgentID, _answersToDM_Queue, _DMstatus, blockSize, _dictionary);
        nextAgentID ++;
        return resAgent;
    }

    public static void resetFactory(){
        nextAgentID = 0;
    }

}
