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
    private Integer taskSize;
    private Integer maxNumOfAgents;
    private EnigmaMachine machine; //copy of the machine
    private List<Agent> agentsList;
    private DecipherMission mission;
    private Integer blockSize; //block of tasks
    private List<CandidateForDecoding> candidacies;
    private DecipheringStatus status;
    private long decipheringStartTime;
    private Integer handledTasksAmount = 0;

    private BlockingQueue<AgentResponse> answersToDM_Queue;

    public DecipherManager(EnigmaMachine em, List<String> _dictionary) {
        machine = em;
        dictionary = _dictionary;
        excludeWords = em.getDecipher().getExcludeChars();
        maxNumOfAgents = em.getDecipher().getMaxNumOfAgents();
        candidacies = new ArrayList<>();
        agentsList = new ArrayList<>();
        status = new DecipheringStatus();
        this.setName("DM");
    }

    //only initialize the DM members
    public boolean initFromUser(String _code, DifficultyLevel _difficulty, Integer _taskSize, Integer _numOfAgents){
        mission = new DecipherMission(machine,_difficulty);
        mission.init(machine, _taskSize, _numOfAgents);
        numOfAgents = _numOfAgents;
        taskSize = _taskSize;
        if (mission.getSize() < _taskSize*numOfAgents)
            return false;
        this.codeToDecipher = _code;
        answersToDM_Queue = new ArrayBlockingQueue<>(numOfAgents);

        //calculate block size
        blockSize = 10;//TODO:find more appropiate function.
        return true;
    }


    @Override
    public void run()
    {
        boolean done = false;
        this.decipheringStartTime = System.currentTimeMillis();
        activateAgents();
        divideTasks();
        while (!done) {
            try {
                AgentResponse response = answersToDM_Queue.take();
                handleAgentResponse(response);
                handledTasksAmount++;
                giveAgentBlockOfTasks(agentsList.get(response.getAgentID()));
                done = mission.isDone() || !status.checkIfToContinue();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        status.stopDeciphering();
        interruptAgents();
        AgentFactory.resetFactory();
        printDecipheringResults();
    }

    private void interruptAgents() {
        for (Agent agent : agentsList){
            agent.interrupt();
        }
    }




    public boolean isValidNumOfAgents(Integer _numOfAgents)
    {
        if (_numOfAgents >= 2 && _numOfAgents <= maxNumOfAgents )
            return true;
        else
            return false;
    }

    private void handleAgentResponse(AgentResponse response) {
        if (response.isEmpty())
            return;
        for (CandidateForDecoding candidate: response.getCandidacies())
        {
            candidacies.add(candidate);
        }
    }

    private void giveAgentBlockOfTasks(Agent agent)
    {
        try {
            AgentTask task;
            for (int i = 0; i < blockSize; i++) {
                task = mission.getNextTask();
                if(task != null) {
                    agent.getTasksQueue().put(task);
                }
                else
                {
                    agent.setTasksAmount(i+1);
                    break;
                }
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
        createAgents(this.machine, this.codeToDecipher, this.status);
        for(Agent agent : this.agentsList) {
            agent.start();
        }
    }

    //this function is responsible for crate new threads(for each agent),
    //and than ask for agent factory to create new agents
    public void createAgents(EnigmaMachine _machine, String _code, DecipheringStatus status)
    {
        Agent agent;
        for(int i=0;i<numOfAgents;i++)
        {
            agent = AgentFactory.createAgent(_machine.deepCopy(),_code, this.answersToDM_Queue, status, blockSize, dictionary);
            agentsList.add(agent);
        }
    }


    public double getNumOfOptionsforDecoding(DifficultyLevel difficulty) {
        double size = DecipherMission.calcMissionSize(difficulty,machine.getABC().length(),machine.getNumOfRotors(),
                machine.getRotorsCount(),machine.getNumOfReflectors());
        return size;
    }

    public boolean isValidTaskSize(Integer taskSize, DifficultyLevel difficulty, Integer numOfAgents) {
        double MissionSize = getNumOfOptionsforDecoding(difficulty);
        if((MissionSize / taskSize) > numOfAgents)
            return true;
        else
            return false;
    }


    public void pauseDeciphering() {
        this.status.pauseDeciphering();
    }

    public void continueDeciphering() {
        this.status.continueDeciphering();
    }

    public void stopDeciphering() {
        this.status.stopDeciphering();
    }

    private void printDecipheringResults() {
        long decipheringEndTime = System.currentTimeMillis();
        System.out.println("Mission is done");
        System.out.println("Printing Results:\n--------------------------------------------");
        //display candidacies for decoding
        int numOfCandidacies = candidacies.size();
        if(numOfCandidacies > 0) {
            System.out.println("There are " + numOfCandidacies + " candidacies for decoding:");
            for (CandidateForDecoding candidate : candidacies) {
                System.out.println(candidate.toString());
                System.out.println(getSecretStr(candidate.getSecret()));
            }
        }
        else
            System.out.println("There are no candidacies for decoding.");

        System.out.println("--------------------------------------------------------------------------------");
        System.out.println("Time elapsed since the beginning of the mission:");
        System.out.println(formatDuration(decipheringEndTime - decipheringStartTime));
        System.out.println("The number of options tested in the mission: " + mission.getSize().intValue());
        //display agents details
        System.out.println("Agents details:");
        for(Agent agent : agentsList){
            System.out.println("Agent " + agent.getAgentID() + " tested " +agent.getHandledOptionsAmount() + " options");
        }
        System.out.println("Press any key to continue...");
    }

    public void printDecipheringProgressMode() {
        System.out.println("Deciphering Status:");
        //display time
        System.out.println("Time elapsed since the beginning of the mission:");
        System.out.println(formatDuration(System.currentTimeMillis() - decipheringStartTime));
        //display progress status (Percentage)
        Double progressPercentage = (100 * handledTasksAmount * taskSize) / mission.getSize();
        System.out.println("Percentage of progress: " + progressPercentage.intValue() + "%");
        //display agents details
        for(Agent agent: agentsList) {
            System.out.println(agent.getDecipheringStatus());
            System.out.print("agent current task: ");
            System.out.println(getSecretStr(agent.getCurrentTask().getSecret()));
        }
        //display candidacies for decoding
        int numOfCandidacies = candidacies.size();
        if(numOfCandidacies > 0) {
            System.out.println("There are " + numOfCandidacies + " candidacies for decoding, recently found:");
            CandidateForDecoding candidate;
            for (int i = numOfCandidacies - 1 ; i >= 0 && i > numOfCandidacies - 10 ; i--) {
                candidate = candidacies.get(i);
                System.out.println(candidate.toString());
                System.out.println(getSecretStr(candidate.getSecret()));
            }
        }
        else
            System.out.println("There are no candidacies for decoding.");

        System.out.println();
    }

    public static String formatDuration(long millis) {
        long seconds = (millis / 1000) % 60;
        long minutes = (millis / (1000 * 60));

        return String.format("%02d:%02d", minutes, seconds);
    }
    private String getSecretStr(Secret _secret){
        StringBuilder sb = new StringBuilder();
        sb.append(getWorkingRotorsStr(_secret));
        sb.append(getWorkingRotorsPositionStr(_secret));
        sb.append('<').append(getWorkingReflectorStr(_secret)).append('>');
        return sb.toString();
    }

    private String getWorkingReflectorStr(Secret _secret) {
        return machine.getEnigma().getMachine().getReflectorStringById(_secret.getSelectedReflector());
    }

    private String getWorkingRotorsStr(Secret _secret) {
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        List<Integer> selectedRotors = _secret.getSelectedRotorsInOrder();
        List<Integer> shallowCopy = selectedRotors.subList(0, selectedRotors.size());
        Collections.reverse(shallowCopy);
        for (Integer integer : shallowCopy){
            sb.append(integer).append(',');
        }
        sb.deleteCharAt(sb.length()-1).append('>');
        return sb.toString();
    }

    private String getWorkingRotorsPositionStr(Secret _secret) {
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        List<Character> chars = new ArrayList<>();
        List<Integer> selectedRotors = _secret.getSelectedRotorsInOrder();
        List<Integer> rotorsLoc = _secret.getSelectedRotorsPositions();
        for (int i = 0; i < selectedRotors.size(); i++){
            Character ch = machine.getEnigma().getMachine().getRotorPositionByNumber(selectedRotors.get(i), rotorsLoc.get(i));
            chars.add(ch);
        }
        List<Character> shallowCopy = chars.subList(0,chars.size());
        Collections.reverse(shallowCopy);
        for (Character character : shallowCopy){
            sb.append(character).append(',');
        }
        sb.deleteCharAt(sb.length()-1).append('>');
        return sb.toString();
    }
}
