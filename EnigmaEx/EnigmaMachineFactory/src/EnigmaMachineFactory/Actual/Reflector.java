package EnigmaMachineFactory.Actual;

import java.util.ArrayList;
import java.util.List;

public class Reflector {

    private List<EnigmaMachineFactory.Actual.Reflect> reflect;
    private String id;
    private int numID;

    public Reflector() {
        reflect = new ArrayList<>();
    }

    public void setID(int id) {
        this.numID = id;
        numIDToStringID();
    }

    public void setID(String id) {
        this.id = id;
        stringIDToNumID();
    }

    public int getID() {
        return numID;
    }

    public String getStringID() {
        return id;
    }
    public  void addReflect(Reflect r){
        reflect.add(r);
    }

    public void addReflectList(List<EnigmaMachineFactory.JAXBGenerated.Reflect> reflectorList) {
        for (EnigmaMachineFactory.JAXBGenerated.Reflect JAXBReflect : reflectorList) {
            Reflect ActualReflect = new Reflect(JAXBReflect.getInput()-1, JAXBReflect.getOutput()-1);
            Reflect ActualReverseReflect = new Reflect(JAXBReflect.getOutput()-1, JAXBReflect.getInput()-1);
            reflect.add(ActualReflect);
            reflect.add(ActualReverseReflect);
        }
        reflect.sort((r1,r2) -> r1.compareToByInput(r2));
    }
    private void stringIDToNumID(){
        switch (id) {
            case "I":
                numID = 1;
                break;
            case "II":
                numID = 2;
                break;
            case "III":
                numID = 3;
                break;
            case "IV":
                numID = 4;
                break;
            case "V":
                numID = 5;
                break;
        }
    }

    private void numIDToStringID(){
        switch (numID) {
            case 1:
                id = "I";
                break;
            case 2:
                id = "II";
                break;
            case 3:
                id = "III";
                break;
            case 4:
                id = "IV";
                break;
            case 5:
                id = "V";
                break;
        }
    }

    public int getReflectToByPosition(int num) {
        Reflect reflect = findReflect(num);
        return reflect.output;
    }

    private Reflect findReflect(int num) {
        return reflect.stream().
                filter(r -> r.input == num)
                .findFirst().get();
    }
}
