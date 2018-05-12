package DecryptionManager;

public class DecipherMission {
    private Integer size;
    private Integer taskSize; //will be log of Mission Size
    private Integer numOfAgents;
    private List<AgentTask> tasks;
    private Integer currentTask; //index
    private Secret firstPos;


    //gets all mission details, and divide into sub-tasks(saved in tasks)
    public DecipherMission(Integet _size,Integer _taskSize, Integer _numOfAgents, Secret firstPos){} //TODO:implement

    //return the next Aagent tasks
    public AgentTask getNextTask(){return null;} //TODO:implement
}
