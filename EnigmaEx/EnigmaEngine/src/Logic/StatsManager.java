package Logic;

import java.time.Duration;
import java.util.Dictionary;
import java.util.List;

public class StatsManager {
    private Integer totalNumOfCodedStrings = 0;
    private Duration totalTimeOfCodingStrings;
    private Dictionary<CodeFormat,List<CodedStrings>> listDictionary;

    public void addCodedString(CodeFormat format, CodedStrings data){
        totalNumOfCodedStrings++;
        totalTimeOfCodingStrings = totalTimeOfCodingStrings.plus(data.getDuration());
        List<CodedStrings> list = listDictionary.remove(format);
        list.add(data);
        listDictionary.put(format,list);
    }
}
