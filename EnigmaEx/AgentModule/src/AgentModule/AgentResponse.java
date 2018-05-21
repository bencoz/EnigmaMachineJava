package AgentModule;


import java.util.ArrayList;
import java.util.List;

public class AgentResponse {
    private Integer agentID;
    private List<CandidateForDecoding> candidacies;
    private Integer numOfCandidacies = 0;

    public AgentResponse(Integer _agentID) {
        this.agentID = _agentID;
    }

    public void addDecoding(CandidateForDecoding candidate) {
        if(numOfCandidacies == 0)
            candidacies = new ArrayList<>();
        candidacies.add(candidate);
        numOfCandidacies++;
    }

    public void reset() {
        candidacies = null;
        numOfCandidacies = 0;
    }

    public Integer getAgentID() {
        return agentID;
    }

    public List<CandidateForDecoding> getCandidacies() {
        return candidacies;
    }

}
