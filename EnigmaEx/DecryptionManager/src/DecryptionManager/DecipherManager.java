package DecryptionManager;

import EnigmaMachineFactory.*;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import AgentModule.*;

public class DecipherManager extends Thread{
    private String codeToDecipher;
    private List<String> dictionary;
    private String excludeWords;
    private Integer numOfAgents;
    private Integer maxNumOfAgents;
    private EnigmaMachine machine; //copy of the machine
    private List<Agent> agentsList;
    private DecipherMission mission;
    private Integer blockSize; //block of tasks
    private List<CandidateForDecoding> candidacies;

    private BlockingQueue<AgentResponse> answersToDM_Queue;

    public DecipherManager(EnigmaMachine em) {
        machine = em;
        dictionary = new LinkedList<>();
        dictionary = em.getDecipher().getDictionary();
        excludeWords = em.getDecipher().getExcludeChars();
        maxNumOfAgents = em.getDecipher().getMaxNumOfAgents();
        candidacies = new ArrayList<>();
        agentsList = new ArrayList<>();
    }

    //only initialize the DM members
    public void initFromUser(String _code, DifficultyLevel _difficulty, Integer _taskSize, Integer _numOfAgents){
        mission = new DecipherMission(machine, _difficulty);
        mission.init(_taskSize,_numOfAgents);
        this.codeToDecipher = _code;
        answersToDM_Queue = new ArrayBlockingQueue<>(numOfAgents);
        //calculate block size
        blockSize = 10;
    }


    @Override
    public void run()
    {
        activateAgents();
        divideTasks();
        while (!mission.isDone()) {
            try {
                AgentResponse response = answersToDM_Queue.take();
                handleAgentResponse(response);
                giveAgentBlockOfTasks(agentsList.get(response.getAgentID()));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
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
        createAgents(machine, codeToDecipher);
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
            agent = AgentFactory.createAgent(_machine.deepCopy(), _code, answersToDM_Queue);
            agentsList.add(agent);
        }
    } //TODO:implement



}
