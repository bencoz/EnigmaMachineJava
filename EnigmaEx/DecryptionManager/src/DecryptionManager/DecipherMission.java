package DecryptionManager;

import AgentModule.*;
import EnigmaMachineFactory.EnigmaMachine;
import EnigmaMachineFactory.Secret;
import EnigmaMachineFactory.SecretBuilder;


import java.util.*;

public class DecipherMission {
    private double size;
    private Integer taskSize;
    private Integer numOfAgents;
    private boolean done;
    private DifficultyLevel difficulty;
    private List<AgentTask> tasks;
    private Iterator<AgentTask> taskIterator;
    private Secret firstPos;


    //gets all mission details, and divide into sub-tasks(saved in tasks)
    //add getting of Secret firstPos
    public DecipherMission(EnigmaMachine machine, DifficultyLevel _difficulty){
        difficulty = _difficulty;
        setSize(machine.getABC().length(), machine.getRotors().size(), machine.getRotorsCount(), machine.getNumOfReflectors());
        setTasks(machine);
        taskIterator = tasks.iterator();
    }

    private void setTasks(EnigmaMachine machine) {
        tasks = new LinkedList<>();
        Secret currentSecret = machine.getSecret();
        Secret secret = currentSecret.toZero();
        SecretBuilder secretBuilder = machine.createSecret();
        double rotorsPositionsSize = Math.pow(machine.getABC().length(), machine.getRotorsCount());
        switch (difficulty){
            case Easy:
                for (int i = 0; i < rotorsPositionsSize; i += taskSize) {
                    tasks.add(new AgentTask(secret,taskSize));
                    secret = secret.advanceBy(taskSize);
                }
                break;
            case Medium:
                for (int j = 0; j < machine.getNumOfReflectors(); j++) {
                    secret.setSelectedReflector(j+1);
                    for (int i = 0; i < rotorsPositionsSize; i += taskSize) {
                        tasks.add(new AgentTask(secret, taskSize));
                        secret = secret.advanceBy(taskSize);
                    }
                }
                break;
            case Hard:
                List<List<Integer>> allPermutaions = new LinkedList<>();
                for (int k = 0; k < factorial(machine.getRotorsCount()); k++) {
                    //Secret newSecret = buildNewSecret()
                    for (int j = 0; j < machine.getNumOfReflectors(); j++) {
                        secret.setSelectedReflector(j + 1);
                        for (int i = 0; i < rotorsPositionsSize; i += taskSize) {
                            tasks.add(new AgentTask(secret, taskSize));
                            secret = secret.advanceBy(taskSize);
                        }
                    }
                }
                break;
            case Impossible:
                //size = Math.pow(abcSize, rotorsCount) * numOfRelectors * factorial(rotorsCount) * binom(rotorsSize, rotorsCount);
                break;
        }
    }

    public void init(Integer _taskSize, Integer _numOfAgents){
        taskSize = _taskSize;
        numOfAgents = _numOfAgents;
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

    private void setSize(Integer abcSize, Integer rotorsSize, Integer rotorsCount, Integer numOfRelectors) {
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
