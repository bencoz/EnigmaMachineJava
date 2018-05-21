package DecryptionManager;

import AgentModule.*;
import java.util.*;

public class DecipherMission {
    private double size;
    private Integer taskSize;
    private Integer numOfAgents;
    private boolean done;
    private List<AgentTask> tasks;
    private Iterator<AgentTask> taskIterator;
    //private Secret firstPos;


    //gets all mission details, and divide into sub-tasks(saved in tasks)
    //add getting of Secret firstPos
    public DecipherMission(DifficultyLevel difficulty, Integer abcSize, Integer rotorsSize, Integer rotorsCount, Integer reflectorsSize){
        setSize(difficulty, abcSize, rotorsSize, rotorsCount, reflectorsSize);
    }
    public void init(Integer _taskSize, Integer _numOfAgents){
        taskSize = _taskSize;
        numOfAgents = _numOfAgents;
        taskIterator = tasks.iterator();
    }
    //return the next Agent tasks
    public AgentTask getNextTask() {
        if (taskIterator.hasNext()) {
            return taskIterator.next();
        } else {
            if (size - tasks.size()*taskSize > 0) {
                //make more tasks
            } else
                done = true;
            return null; //TODO:: check if need more missions if not change done to true
        }
    }

    public Integer getNumOfAgent(){
        return numOfAgents;
    }

    public Double getSize() {
        return size;
    }

    public boolean isDone() {
        return done;
    }

    private void setSize(DifficultyLevel difficulty, Integer abcSize, Integer rotorsSize, Integer rotorsCount, Integer numOfRelectors) {
        switch (difficulty){
            case Easy:
                size = Math.pow(abcSize, rotorsCount);
                break;
            case Medium:
                size = Math.pow(abcSize, rotorsCount) * numOfRelectors;
                break;
            case Hard:
                size = Math.pow(abcSize, rotorsCount) * numOfRelectors * factorial(rotorsCount);
                break;
            case Impossible:
                size = Math.pow(abcSize, rotorsCount) * numOfRelectors * factorial(rotorsCount) * binom(rotorsSize, rotorsCount);
                break;
        }
    }

    private double binom(Integer n, Integer k) {
        if (n  < k) return 0;
        if (0 == n) return 0;
        if (0 == k) return 1;
        if (n == k) return 1;
        if (1 == k) return n;
        return factorial(n)/(factorial(n-k)*factorial(k));
    }

    private double factorial(Integer number) {
        double result = 1;
        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }
}
