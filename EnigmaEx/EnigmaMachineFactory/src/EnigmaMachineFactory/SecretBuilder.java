package EnigmaMachineFactory;

public class SecretBuilder {
    private SecretImpl secret;
    private EnigmaMachineImpl machine;

    public SecretBuilder(EnigmaMachineImpl i_machine) {
        secret = new SecretImpl();
        machine = i_machine;
    }

    public SecretBuilder selectRotor(int rotorID, int rotorPosition){ //rotorPosition received from user is 1-based
        secret.addRotor(rotorID,rotorPosition-1);
        return this;
    }

    public SecretBuilder selectRotor(int rotorID, char rotorPosition){
        int position = machine.getEnigma().getMachine().getRotorPositionByChar(rotorID, rotorPosition); //Position return 0-based
        secret.addRotor(rotorID,position);
        return this;
    }

    public SecretBuilder selectReflector(int reflectorID){
        secret.setSelectedReflector(reflectorID);
        return this;
    }

public Secret create(){
        machine.setSecret(secret);
        return secret;
    }
}
