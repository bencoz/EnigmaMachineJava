package Logic;

import java.time.Duration;
import java.util.*;

public class StatsManager {
    private Integer totalNumOfCodedStrings = 0;
    private Duration totalTimeOfCodingStrings = Duration.ZERO;
    private Map<CodeFormat, List<CodedStrings>> listDictionary;

    public StatsManager() {
        listDictionary = new Hashtable<>();
    }

    public void addCodedString(CodeFormat format, CodedStrings data) {
        totalNumOfCodedStrings++;
        totalTimeOfCodingStrings = totalTimeOfCodingStrings.plus(data.getDuration());
        List<CodedStrings> list;
        if (listDictionary.containsKey(format))
        {
            list = listDictionary.remove(format);
        } else {
            list = new ArrayList<>();
        }
        list.add(data);
        listDictionary.put(format, list);
    }

    public Integer getTotalNumOfCodedStrings() {
        return totalNumOfCodedStrings;
    }

    public Integer getAvarageTimeForCoding() {
        return totalTimeOfCodingStrings.dividedBy(totalNumOfCodedStrings).getNano();
    }

    public String getDiconaryToString() {
        StringBuilder sb = new StringBuilder();
        Iterator<Map.Entry<CodeFormat, List<CodedStrings>>> iter = listDictionary.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry<CodeFormat, List<CodedStrings>> entry = iter.next();
            sb.append(entry.getKey()+"\n");
            int counter = 1;
            List<CodedStrings> codedStringsList = entry.getValue();
            for (CodedStrings codedStrings: codedStringsList){
                sb.append(counter).append(". ");
                sb.append(codedStrings).append("\n");
                counter++;
            }
        }
        return sb.toString();
    }
}