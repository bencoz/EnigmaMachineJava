package AgentModule;

import EnigmaMachineFactory.EnigmaMachine;
import EnigmaMachineFactory.Secret;

public class AgentTask {
    private Secret secret;
    private Integer length;

    public AgentTask(Secret startPoint, Integer length) {
        this.secret = startPoint;
        this.length = length;
    }
    public boolean moveToNextCode(EnigmaMachine machine){
        if (--length > 0 && secret.hasNext(machine)) {
            this.secret = secret.moveToNext(machine);
            return true;
        } else{
            return false; //TODO:: tell agent task is finished
        }
    }

    public Boolean hasNext(){
        return length > 0 ? true:false;
    }

    public Secret getSecret()
    {
        return secret;
    }

    public Integer getLength() {
        return length;
    }
}
