package EnigmaMachineFactory.Actual;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Decipher implements Serializable {
    protected int maxNumOfAgents;
    protected String excludeChars;
    protected String dictionary = "";

    public Decipher(int numOfAgents){
        maxNumOfAgents = numOfAgents;
    }

    public String getDictionary() {
        return dictionary;
    }
    public void appedToDictionary(String words) {
        StringBuilder sb = new StringBuilder(dictionary);
        sb.append(words);
        dictionary = sb.toString();
    }
    public int getMaxNumOfAgents() {
        return maxNumOfAgents;
    }


    public String getExcludeChars() {
        return excludeChars;
    }

    public void setExcludeChars(String excludeChars) {
        this.excludeChars = excludeChars;
    }


}
