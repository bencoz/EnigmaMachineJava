package AgentModule;

import EnigmaMachineFactory.Secret;

public class CandidateForDecoding {
    private String decoding;
    private Secret secret;
    private Integer agentID;


    public CandidateForDecoding(String _decoding, Secret _position, Integer _agentID)
    {
        this.decoding = _decoding;
        this.secret = _position;
        this.agentID = _agentID;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append("Agent :").append(agentID).append(" Decoding :").append(decoding);
        sb.append(" Secret :").append(secret);
        return sb.toString();
    }

    public String getDecoding() {
        return decoding;
    }

    public void setDecoding(String decoding) {
        this.decoding = decoding;
    }

    public Secret getSecret() {
        return secret;
    }

    public void setSecret(Secret secret) {
        this.secret = secret;
    }

    public Integer getAgentID() {
        return agentID;
    }

    public void setAgentID(Integer agentID) {
        this.agentID = agentID;
    }

}
