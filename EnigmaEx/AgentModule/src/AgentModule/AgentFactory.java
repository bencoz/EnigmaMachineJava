package AgentModule;


import EnigmaMachineFactory.*;

import java.util.concurrent.BlockingQueue;

public class AgentFactory {
    private static int nextAgentID = 0;
    //this function create and return new agent, (maybe give him task?)
    public static Agent createAgent(EnigmaMachine _machine, String _code,
                                    BlockingQueue<AgentResponse> _answersToDM_Queue){
        Agent resAgent = new Agent(_machine,_code,nextAgentID,_answersToDM_Queue);
        nextAgentID ++;
        return resAgent;
    }



}
