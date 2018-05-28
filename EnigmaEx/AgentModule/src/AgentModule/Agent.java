package AgentModule;

import EnigmaMachineFactory.*;
import EnigmaMachineFactory.JAXBGenerated.Decipher;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Agent extends Thread{
    private Integer agentID ;
    private EnigmaMachine machine; //copy of the machine
    private String code;
    private List<AgentTask> tasks ;
    private AgentTask currentTask;
    private Integer currentTaskInd = 0;
    private AgentResponse response;
    private DecipheringStatus DMstatus;
    private Integer handledOptionsAmount = 0;

    private Integer tasksAmount; //num of tasks that received each time ( == BlockSize)
    private BlockingQueue<AgentTask> tasksFromDM_Queue;
    private BlockingQueue<AgentResponse> answersToDM_Queue;
    private List<String> dictionary; //copy of the dictionary


    public Agent(EnigmaMachine _machine, String _code, Integer _ID,
                 BlockingQueue<AgentResponse> _answersToDM_Queue, DecipheringStatus _DMstatus, Integer blockSize,
                 List<String> _dictionary){
        this.machine = _machine;
        this.code = _code;
        this.agentID = _ID;
        this.answersToDM_Queue = _answersToDM_Queue;
        this.tasksFromDM_Queue = new ArrayBlockingQueue<>(blockSize);
        this.tasksAmount = blockSize;
        this.response = new AgentResponse(agentID);
        this.DMstatus = _DMstatus;
        this.setName("Agent "+agentID);
        this.dictionary = _dictionary;
    }

    private void addTaskToQueue(AgentTask i_task){
        tasks.add(i_task);
    }

    //work on block of task that got from DM, solves each one, update the agent response (add the Candidacies For Decoding to it)
    private void doTasks() throws InterruptedException {
        for(int i=0;i<tasks.size();i++)
        {
            this.currentTask = tasks.get(i);
            currentTaskInd = i;
            doCurrentTask();
        }
    }

    //work on current task update the agent response (add the Candidacies For Decoding to it)
    private void doCurrentTask(){
        int firstSize = currentTask.getLength();
        for(int i=0; i < firstSize; i++) {
            machine.initFromSecret(currentTask.getSecret());
            String decoding = machine.process(code);
            handledOptionsAmount++;

            if(isCandidaciesForDecoding(decoding)) {
                CandidateForDecoding candidate = new CandidateForDecoding(decoding, currentTask.getSecret(), agentID);
                response.addDecoding(candidate);
            }
            if(currentTask.hasNext()){
                if (!currentTask.moveToNextCode(machine)) // moveToNextCode returns false when no more codes configurations left.
                    break;
            }
            else
                break;
        }
    }

    //gets code decoding and return true if all words in the dictionary and false otherwise
    private boolean isCandidaciesForDecoding(String decoding){
        boolean found;
        String[] words = decoding.split(" ");
        for (String word : words){
            found = false;
            for (String permittedWord : dictionary){
                if (permittedWord.equals(word))
                    found = true;
            }
            if (!found)
                return false;
        }
        return true;
    }


    public void setID(Integer _ID){
        this.agentID = _ID;
    }

    public BlockingQueue<AgentTask> getTasksQueue()
    {
        return tasksFromDM_Queue;
    }


    //agent wait and listen to pipe until he gets new mission from DM and start to work
    @Override
    public void run() {
        boolean done = false;
        try {
            while (!done) {
                AgentTask task;
                tasks = new ArrayList<>();
                for (int i = 0; i < tasksAmount; i++) {
                    task = tasksFromDM_Queue.take();
                    tasks.add(task);
                }
                doTasks();
                answersToDM_Queue.put(response);
                reset();
                done = !DMstatus.checkIfToContinue();
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            //TODO:: HANDLE STUFF AFTER INTERRUPT
            return;
        }
    }

    private void reset()
    {
        tasks = null;
        currentTask = null;
        currentTaskInd = 0;
        response = new AgentResponse(this.agentID);
        //response.reset();
        //tasksAmount = 0;
    }

    public void setTasksAmount(Integer _tasksAmount) {
        this.tasksAmount = _tasksAmount;
    }

    public Integer getAgentID()
    {
        return agentID;
    }

    public AgentTask getCurrentTask() {
        return currentTask;
    }

    public String getDecipheringStatus(){
        StringBuilder sb = new StringBuilder();
        sb.append("Agent ID:").append(agentID).append("\n");
        if(tasks == null){
            sb.append("didn't get tasks yet").append("\n");
        }
        else {
            int numOfRemainTasks = tasks.size() - currentTaskInd;
            sb.append("Number of tasks remaining to perform").append(numOfRemainTasks).append("\n");
        }
        return sb.toString();
    }

    public Integer getHandledOptionsAmount() {
        return handledOptionsAmount;
    }


}
