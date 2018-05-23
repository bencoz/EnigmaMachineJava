package EnigmaMachineFactory;

import java.util.List;

public class SecretBuilder {
    private SecretImpl secret;
    private EnigmaMachineImpl machine;

    public SecretBuilder(EnigmaMachineImpl i_machine) {
        secret = new SecretImpl();
        machine = i_machine;
		secret.setAbcSize(machine.getEnigma().getMachine().getAbc().length());
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
        secret.setAbcSize(machine.getABC().length());
        return secret;
    }

    public Secret getSecret(){
        return secret;
    }

	public Secret moveNext() {
        Secret newSecret = new SecretImpl();
        newSecret.setSelectedReflector(secret.getSelectedReflector());
        Boolean needUpdate = true;
        int i, RotorPos;
        List<Integer> rotorIds = secret.getSelectedRotorsInOrder();
        List<Integer> rotorPos = secret.getSelectedRotorsPositions();
        for (i = RotorPos = 0; i < rotorIds.size(); i++) {
            RotorPos = rotorPos.get(i);
            if (needUpdate) {
                RotorPos = (RotorPos + 1) % machine.getABC().length();
                needUpdate = (RotorPos == 0);
            }
            newSecret.addRotor(rotorIds.get(i), RotorPos);
        }
        machine.setSecret(newSecret);
        return newSecret;
    }
}
