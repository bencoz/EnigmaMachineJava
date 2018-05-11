package AgentModule;

import EnigmaMachineFactory.*;

import java.util.LinkedList;
import java.util.List;

public class Agent {
    private EnigmaMachine machine; //copy of the machine
    private AgentTask currentTask;
    private List<AgentTask> tasks = new LinkedList<>();


    public Agent(EnigmaMachine machine) {
        this.machine = machine;
    }
    public void addTaskToQueue(AgentTask i_task){
        tasks.add(i_task);
    }


    public List<String> getCandidaciesForDecoding(String code, AgentTask task)
    {
        return null;
    }
}
