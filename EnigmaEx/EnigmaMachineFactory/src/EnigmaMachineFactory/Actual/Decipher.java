package EnigmaMachineFactory.Actual;

import java.io.Serializable;
import java.util.*;


public class Decipher implements Serializable {
    protected int maxNumOfAgents;
    protected String excludeChars;
    protected List<String> dictionary = new LinkedList<>();

    public Decipher(int numOfAgents){
        maxNumOfAgents = numOfAgents;
    }

    public List<String> getDictionary() {
        return dictionary;
    }
    public String getDictionaryAsString(){
        StringBuilder sb = new StringBuilder();
        for (String word : dictionary)
            sb.append(word).append(" ");
        return sb.toString();
    }
    public void appedToDictionary(String[] words) {
        for (String word : words) {
            dictionary.add(word);
        }
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
