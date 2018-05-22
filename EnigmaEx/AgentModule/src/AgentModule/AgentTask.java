package AgentModule;

import EnigmaMachineFactory.Secret;

public class AgentTask {
    private Secret RotorsPosition;
    private Integer length;

    public AgentTask(Secret startPoint, Integer length) {
        this.RotorsPosition = startPoint;
        this.length = length;
    }
    public void moveToNextCode(){
        if (--length > 0) {
            //RotorsPosition = RotorsPosition.next();
        }
    }
    public Boolean hasNext(){
        return length > 0 ? true:false;
    }

    public Secret getSecret()
    {
        return RotorsPosition;
    }

    public Integer getLength() {
        return length;
    }
}
