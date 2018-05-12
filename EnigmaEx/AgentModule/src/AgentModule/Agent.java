package AgentModule;

import EnigmaMachineFactory.*;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private EnigmaMachine machine; //copy of the machine
    private String code;
    private AgentTask currentTask;
    private List<AgentTask> tasks = new LinkedList<>();
    private Map dictionary; //copy of the dictionary


    public Agent(EnigmaMachine machine, String code){
        this.machine = machine;
        this.code = code;
    }

    public void addTaskToQueue(AgentTask i_task){
        tasks.add(i_task);
    }

    //gets code and agent-task and return all the decoding candidacies (all decoding words are in dictionary)
    public List<String> getCandidaciesForDecoding( AgentTask task){
        return null;
    } //TODO:implement

    //gets code decoding and return true if all words in the dictionary and false otherwise
    private boolean isCandidaciesForDecoding(String decoding){ return true; } //TODO:implement

    //agent wait and listen to pipe until he gets new mission from DM and start to work
    public void work(){} //TODO:implement


}
