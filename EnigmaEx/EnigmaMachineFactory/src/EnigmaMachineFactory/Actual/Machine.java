package EnigmaMachineFactory.Actual;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Machine {
    private String abc;
    private int rotorsCount;
    private List<Rotor> rotors;
    private List<Reflector> reflectors;

    public Machine() {
        rotors = new ArrayList<>();
        reflectors = new ArrayList<>();
    }

    public String getAbc() {
        return abc;
    }

    public void setAbc(String abc) {
        this.abc = abc;
    }

    public int getRotorsCount() {
        return rotorsCount;
    }

    public void setRotorsCount(int rotorsCount) {
        this.rotorsCount = rotorsCount;
    }

    public void addRotor(Rotor r){
        rotors.add(r);
    }

    public void addReflector(Reflector r){
        reflectors.add(r);
    }

    public int getRotorsSize() { return rotors.size(); }

    public int getRotorPositionByChar(int rotorID, char rotorPosition) {
        Rotor rotor = rotors.stream().
        filter(r -> r.getID() == rotorID).
                findAny().
                get();
        return rotor.getMapFromPositionByChar(rotorPosition);
    }

    public Reflector getReflectorById(int selectedReflectorID) {
        return reflectors.stream().
                filter(r -> r.getID() == selectedReflectorID).
                findFirst().get();
    }

    public List<Rotor> getRotorsById(List<Integer> selectedRotorsInOrder) {
        /*return rotors.stream().
                filter(r -> selectedRotorsInOrder.contains(r.getID())).
                collect(Collectors.toList());*/
        List<Rotor> result = new ArrayList<>();
        for (Integer num : selectedRotorsInOrder) {
            for (Rotor r : rotors) {
                if (r.getID() == num)
                    result.add(r);
            }
        }
        return result;
    }

    public List<Rotor> getRotors() {
        return rotors;
    }


    public List<Reflector> getReflectors(){
        return reflectors;
    }


}