package EnigmaMachineFactory;

import EnigmaMachineFactory.Actual.Reflector;
import EnigmaMachineFactory.Actual.Rotor;

public class EnigmaMachineBuilder {

    private EnigmaMachineImpl machine;
    private EnigmaComponentFactory factory;

    public EnigmaMachineBuilder() {
        machine = new EnigmaMachineImpl();
        factory = EnigmaComponentFactoryImpl.INSTANCE;
    }

    public void setRotorsCount(int rotorsCount) {
        machine.getEnigma().getMachine().setRotorsCount(rotorsCount);
    }

    public void setABC(String abc) { machine.getEnigma().getMachine().setAbc(abc); }

    public void defineRotor(int id, String source, String target, int notch){ //TODO :: check if parameters are OK
        Rotor rotor = factory.createFromString(id,source,target,notch);
        machine.getEnigma().getMachine().addRotor(rotor);
    }

    public void defineReflector(int id, byte[] from, byte[] to){
        Reflector reflector = factory.createReflector(id, from, to);
        machine.getEnigma().getMachine().addReflector(reflector);
    }

    private byte[] byteUnboxing(Byte[] i_input){
        byte[] output = new byte[i_input.length];
        int i = 0;
        for(Byte b: i_input)
            output[i++] = b.byteValue();
        return output;
    }

    public EnigmaMachine create(){
        return machine;
    }
}
