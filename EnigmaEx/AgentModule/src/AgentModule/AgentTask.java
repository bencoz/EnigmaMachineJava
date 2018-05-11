package AgentModule;

import EnigmaMachineFactory.Secret;

public class AgentTask {
    private Secret startPoint;
    private Integer length;

    public AgentTask(Secret startPoint, Integer length) {
        this.startPoint = startPoint;
        this.length = length;
    }
    public void moveToNextCode(){
        if (--length > 0) {
            //startPoint = startPoint.next();
        }
    }
    public Boolean hasNext(){
        return length > 0 ? true:false;
    }
}
