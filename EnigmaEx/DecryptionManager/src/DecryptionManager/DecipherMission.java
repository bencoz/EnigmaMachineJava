package DecryptionManager;

import AgentModule.*;
import java.util.*;

public class DecipherMission {
    private String code;
    private Integer size;
    private Integer taskSize;
    private Integer numOfAgents;
    private List<AgentTask> tasks;
    private Integer currentTask; //index
    //private Secret firstPos;


    //gets all mission details, and divide into sub-tasks(saved in tasks)
    //add getting of Secret firstPos
    public DecipherMission(DifficultyLevel difficulty, Integer _taskSize, Integer _numOfAgents){} //TODO:implement

    //return the next Agent tasks
    public AgentTask getNextTask(){return null;} //TODO:implement

    public Integer getNumOfAgent(){
        return numOfAgents;
    }

    public String getCode()
    {
        return code;
    }
}
