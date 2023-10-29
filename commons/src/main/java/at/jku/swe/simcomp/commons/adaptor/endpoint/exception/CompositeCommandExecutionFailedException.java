package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

import lombok.Getter;

@Getter
public class CompositeCommandExecutionFailedException extends Exception {
    private final Exception originalException;
    private String reports;
    public CompositeCommandExecutionFailedException(String message, Exception originalException, String reports) {
        super(message);
        this.originalException=originalException;
        this.reports=reports;
    }

    public void prependReport(String report){
        this.reports=report+this.reports;
    }

    public String getMessageWithReports(){
       return getMessage() + "\n" + reports;
    }
}
