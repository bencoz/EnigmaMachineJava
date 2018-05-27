package AgentModule;

import EnigmaMachineFactory.*;
import EnigmaMachineFactory.Actual.Rotor;
import EnigmaMachineFactory.JAXBGenerated.Decipher;

import java.util.ArrayList;
import java.util.Collections;
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
        for(int i=0; i<currentTask.getLength() ; i++) {
            machine.initFromSecret(currentTask.getSecret());
            String decoding = machine.process(code);

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
                if (!response.isEmpty()) {
                    answersToDM_Queue.put(response);
                }
                reset();
                done = !DMstatus.checkIfToContinue();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
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


    //new

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
        int numOfRemainTasks = tasks.size() - currentTaskInd;
        sb.append("Number of tasks remaining to perform").append(numOfRemainTasks).append("\n");
        sb.append("Current Task:").append(getCurrentTaskStr()).append("\n");
        return sb.toString();
    }

    private String getCurrentTaskStr(){
        StringBuilder sb = new StringBuilder();
        sb.append(getCurrentTaskWorkingRotors());
        sb.append(getCurrentTaskWorkingRotorsChar());
        sb.append(getCurrentTaskWorkingReflector());
        sb.append('<').append(currentTask.getSecret().getSelectedReflector()).append('>');
        return sb.toString();
    }

    private String getCurrentTaskWorkingReflector() {
        return machine.getEnigma().getMachine().getReflectorStringById(currentTask.getSecret().getSelectedReflector());
    }

    private String getCurrentTaskWorkingRotors() {
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        List<Integer> selectedRotors = currentTask.getSecret().getSelectedRotorsInOrder();
        List<Integer> shallowCopy = selectedRotors.subList(0, selectedRotors.size());
        Collections.reverse(shallowCopy);
        for (Integer integer : shallowCopy){
            sb.append(integer).append(',');
        }
        sb.deleteCharAt(sb.length()-1).append('>');
        return sb.toString();
    }

    private String getCurrentTaskWorkingRotorsChar() {
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        List<Character> chars = new ArrayList<>();
        List<Integer> selectedRotors = currentTask.getSecret().getSelectedRotorsInOrder();
        List<Integer> rotorsLoc = currentTask.getSecret().getSelectedRotorsPositions();
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
