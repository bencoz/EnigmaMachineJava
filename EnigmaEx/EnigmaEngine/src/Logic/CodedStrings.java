package Logic;

import java.time.Duration;
import java.time.Instant;

public class CodedStrings {
    private String input;
    private String output;
    private Duration duration;

    public void setDuration(Instant start, Instant end){
        duration = Duration.between(start, end);
    }
    public void setInput(String input){
        this.input = input;
    }
    public void setOutput(String output){
        this.output = output;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append('<');
        sb.append(input);
        sb.append('>');
        sb.append(" --> ");
        sb.append('<');
        sb.append(output);
        sb.append(">  (");
        sb.append(duration.getNano());
        sb.append(')');
        return sb.toString();
    }

    public Duration getDuration() {
        return duration;
    }
}
