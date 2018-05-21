package DecryptionManager;

import EnigmaMachineFactory.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import AgentModule.*;

public class Decipher extends Thread{
    private String code;
    private Map dictionary = new HashMap();
    //private String excludeWords;
    private Integer numOfAgents;
    private EnigmaMachine machine; //copy of the machine
    private List <Agent> agentsList = new ArrayList<>();
    private DecipherMission mission;
    private Integer blockSize; //block of tasks
    private List<CandidateForDecoding> candidacies;

    private BlockingQueue<AgentResponse> answersToDM_Queue;

    //only initialize the DM members
    public Decipher(String _code, DifficultyLevel _difficulty, Integer _taskSize, Integer _numOfAgents,
                    Map _dictionary, EnigmaMachine _machine){
        this.code = _code;
        this.dictionary = _dictionary;
        this.machine = _machine;
        //...

        mission = MissionFactory.createMission(_difficulty,_taskSize,_numOfAgents);
        answersToDM_Queue = new ArrayBlockingQueue<>(numOfAgents);
        candidacies = new ArrayList<>();

        //calculate block size
        blockSize =10;
    } //TODO:implement

    @Override
    public void run()
    {
        activateAgents();
        divideTasks();  
        try {
            AgentResponse response = answersToDM_Queue.take();
            handleAgentResponse(response);
            giveAgentBlockOfTasks(agentsList.get(response.getAgentID()));
        }catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void handleAgentResponse(AgentResponse response) {
        for( CandidateForDecoding candidate: response.getCandidacies())
        {
            candidacies.add(candidate);
        }
    }

    private void giveAgentBlockOfTasks(Agent agent)
    {
        try {
            for (int i = 0; i < blockSize; i++) {
                agent.getTasksQueue().put(mission.getNextTask());
            }
        }catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void divideTasks()
    {
        for(Agent agent : agentsList)
            giveAgentBlockOfTasks(agent);
    }


    private void activateAgents()
    {
        createAgents(machine,code);
        for(Agent agent : agentsList) {
            agent.start();
        }
    }

    //this function is responsible for crate new threads(for each agent),
    //and than ask for agent factory to create new agents
    public void createAgents(EnigmaMachine _machine, String _code)
    {
        Agent agent;
        for(int i=0;i<numOfAgents;i++)
        {
            agent = AgentFactory.createAgent(_machine,_code, answersToDM_Queue);
            agentsList.add(agent);
        }
    } //TODO:implement



}
