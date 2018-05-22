package AgentModule;

import EnigmaMachineFactory.Secret;

public class CandidateForDecoding {
    private String decoding;
    private Secret position;
    private Integer agentID;


    public CandidateForDecoding(String _decoding, Secret _position, Integer _agentID)
    {
        this.decoding = _decoding;
        this.position = _position;
        this.agentID = _agentID;
    }
    public String getDecoding() {
        return decoding;
    }

    public void setDecoding(String decoding) {
        this.decoding = decoding;
    }

    public Secret getPosition() {
        return position;
    }

    public void setPosition(Secret position) {
        this.position = position;
    }

    public Integer getAgentID() {
        return agentID;
    }

    public void setAgentID(Integer agentID) {
        this.agentID = agentID;
    }
}
