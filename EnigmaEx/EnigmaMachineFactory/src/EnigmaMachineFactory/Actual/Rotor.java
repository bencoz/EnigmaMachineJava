package EnigmaMachineFactory.Actual;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.lang.StrictMath.abs;

public class Rotor {

    private List<EnigmaMachineFactory.Actual.Mapping> mapping;
    private int notch;
    private int id;
    private int position = 0;
    private int workingNotch;

    public Rotor() {
        mapping = new ArrayList<>();
    }

    public void setID(int ID) {
        this.id = ID;
    }
    public int getID(){return id; }

    public int getWorkingNotch() {
        return workingNotch;
    }

    public void setWorkingNotch(int workingNotch) {
        this.workingNotch = workingNotch;
    }

    public void addMapping(Mapping map){
        mapping.add(map);
    }

    public void addMappingList(List<EnigmaMachineFactory.JAXBGenerated.Mapping> mapList){
        for (EnigmaMachineFactory.JAXBGenerated.Mapping map : mapList ){
            Mapping ActualMap = new Mapping(map.getFrom(),map.getTo());
            mapping.add(ActualMap);
        }
    }

    public void setNotch(int notch) {
        this.notch = notch;
    }

    public int getNotch() {
        return notch;
    }

    public int getPosition() {
        return position;
    }

    public void increasePositionBy(int size) {
        for (int i = 0; i < size; i++){
            this.position--;
            if (this.position < 0){
                this.position = mapping.size()-1;
            }
        }
    }

    public int getMapFromPositionByChar(char rotorPosition) {
        int result = -1;
        for (int i = 0; i < mapping.size() ; i++){
            if (mapping.get(i).from == rotorPosition) {
                result = i;
                break;
            }
        }
        return (result + position) % mapping.size();
    }

    public int getMapToPositionByChar(char rotorPosition) {
        int result = -1;
        for (int i = 0; i < mapping.size() ; i++){
            if (mapping.get(i).to == rotorPosition) {
                result = i;
                break;
            }
        }
        return (result + position) % mapping.size();
    }

    public char getMappingCharFromPart(int position) {
        return (mapping.get(position).from);
    }

    public char getMappingCharToPart(int position) {
        return (mapping.get(position).to);
    }

    public boolean isNotchAtPosition() {
        return (getWorkingNotch() == getPosition());
    }


}