package DecryptionManager;

import AgentModule.*;
import EnigmaMachineFactory.EnigmaMachine;
import EnigmaMachineFactory.Secret;
import EnigmaMachineFactory.SecretBuilder;


import java.util.*;
import java.util.stream.Collectors;

public class DecipherMission {
    private double size;
    private Integer taskSize;
    private Integer numOfAgents;
    private boolean done;
    private DifficultyLevel difficulty;
    private List<AgentTask> tasks;
    private Iterator<AgentTask> taskIterator;


    //gets all mission details, and divide into sub-tasks(saved in tasks)
    public DecipherMission(EnigmaMachine machine, DifficultyLevel _difficulty){
        difficulty = _difficulty;
        setSize(difficulty, machine.getABC().length(), machine.getRotors().size(), machine.getRotorsCount(), machine.getNumOfReflectors());
    }

    private void setTasks(EnigmaMachine machine) {
        tasks = new LinkedList<>();
        Secret secret = machine.getSecret().toZero();
        Double rotorsPositionsSize = Math.pow(machine.getABC().length(), machine.getRotorsCount());
        List<List<Integer>> allPermutations;
        switch (difficulty){
            case Easy:
                setEasyTasks(rotorsPositionsSize, secret);
                break;
            case Medium:
                setMediumTasks(machine, rotorsPositionsSize, secret);
                break;
            case Hard:
                allPermutations = permute(secret.getSelectedRotorsInOrder());
                setHardTasks(machine, allPermutations, rotorsPositionsSize, secret);
                break;
            case Impossible:
                List<Integer> rotorIDs = getAllRotorIDs(machine);
                List<Set<Integer>> allSubsets = getSubsets(rotorIDs, machine.getRotorsCount());
                for (int l = 0; l < allSubsets.size(); l++) {
                    List<Integer> list = new ArrayList<>(allSubsets.get(l));
                    allPermutations = permute(list);
                    setHardTasks(machine, allPermutations, rotorsPositionsSize, secret);
                }
                break;
        }
    }

    private void setHardTasks(EnigmaMachine machine, List<List<Integer>> allPermutations, Double rotorsPositionsSize, Secret secret) {
        for (int k = 0; k < allPermutations.size(); k++) {
            secret.setRotorsIDs(allPermutations.get(k));
            setMediumTasks(machine, rotorsPositionsSize, secret);
        }
    }

    private void setMediumTasks(EnigmaMachine machine, Double rotorsPositionsSize, Secret secret) {
        for (int j = 0; j < machine.getNumOfReflectors(); j++) {
            secret.setSelectedReflector(j + 1);
            setEasyTasks(rotorsPositionsSize, secret);
        }
    }

    private void setEasyTasks(Double rotorsPositionsSize, Secret secret) {
        for (int i = 0; i < rotorsPositionsSize; i += taskSize) {
            tasks.add(new AgentTask(secret,taskSize));
            secret = secret.advanceBy(taskSize);
            if (i + taskSize >= rotorsPositionsSize){//only in last iteration
                secret = secret.advanceBy(taskSize);
                tasks.add(new AgentTask(secret,rotorsPositionsSize.intValue() - i));
            }
        }
    }

    private List<Integer> getAllRotorIDs(EnigmaMachine machine) {
        List<Integer> res = new LinkedList<>();
        int[] array = machine.getRotors().stream().mapToInt(rotor -> rotor.getID()).toArray();
        for (int i : array)
        {
            res.add(i);
        }
        return res;
    }

    public void init(EnigmaMachine machine, Integer _taskSize, Integer _numOfAgents){
        taskSize = _taskSize;
        numOfAgents = _numOfAgents;
        setTasks(machine);
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
        size = DecipherMission.calcMissionSize(difficulty, abcSize, rotorsSize, rotorsCount, numOfRelectors);
    }

    public static double calcMissionSize(DifficultyLevel difficulty,Integer abcSize, Integer rotorsSize, Integer rotorsCount, Integer numOfRelectors){
    double missionSize = 0;
        switch (difficulty){
            case Easy:
                missionSize = Math.pow(abcSize, rotorsCount);
                break;
            case Medium:
                missionSize = Math.pow(abcSize, rotorsCount) * numOfRelectors;
                break;
            case Hard:
                missionSize = Math.pow(abcSize, rotorsCount) * numOfRelectors * factorial(rotorsCount);
                break;
            case Impossible:
                missionSize = Math.pow(abcSize, rotorsCount) * numOfRelectors * factorial(rotorsCount) * binom(rotorsSize, rotorsCount);
                break;
        }
        return missionSize;
    }

    private static double binom(Integer n, Integer k) {
        if (n  < k) return 0;
        if (0 == n) return 0;
        if (0 == k) return 1;
        if (n == k) return 1;
        if (1 == k) return n;
        return factorial(n)/(factorial(n-k)*factorial(k));
    }

    private static double factorial(Integer number) {
        double result = 1;
        for (int factor = 2; factor <= number; factor++) {
            result *= factor;
        }

        return result;
    }

    private List<List<Integer>> permute(List<Integer> arr){
        List<List<Integer>> res = new ArrayList<>();
        permuteHelper(arr, 0, res);
        return res;
    }

    private void permuteHelper(List<Integer> arr, int index, List<List<Integer>> res){
        if(index >= arr.size() - 1){ //If we are at the last element - nothing left to permute
            List<Integer> addToRes = arr.stream().collect(Collectors.toList());
            res.add(addToRes);
            return;
        }

        for(int i = index; i < arr.size(); i++){ //For each index in the sub array arr[index...end]

            //Swap the elements at indices index and i
            int t = arr.get(index);
            arr.set(index ,arr.get(i));
            arr.set(i, t);

            //Recurse on the sub array arr[index+1...end]
            permuteHelper(arr, index+1, res);

            //Swap the elements back
            t = arr.get(index);
            arr.set(index, arr.get(i));
            arr.set(i,t);
        }
    }

    private void getSubsets(List<Integer> superSet, int k, int idx, Set<Integer> current, List<Set<Integer>> solution) {
        //successful stop clause
        if (current.size() == k) {
            solution.add(new HashSet<>(current));
            return;
        }
        //unseccessful stop clause
        if (idx == superSet.size()) return;
        Integer x = superSet.get(idx);
        current.add(x);
        //"guess" x is in the subset
        getSubsets(superSet, k, idx+1, current, solution);
        current.remove(x);
        //"guess" x is not in the subset
        getSubsets(superSet, k, idx+1, current, solution);
    }

    private List<Set<Integer>> getSubsets(List<Integer> superSet, int k) {
        List<Set<Integer>> res = new ArrayList<>();
        getSubsets(superSet, k, 0, new HashSet<Integer>(), res);
        return res;
    }
}
