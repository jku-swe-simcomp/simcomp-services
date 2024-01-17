package at.jku.swe.simcomp.commons.adaptor.endpoint.exception;

import lombok.Getter;

/**
 * This exception is thrown when the execution of a composite command failed.
 */
@Getter
public class CompositeCommandExecutionFailedException extends Exception {
    /**
     * The original exception that caused the failure.
     */
    private final Exception originalException;
    /**
     * The reports that are aggregated when the exception is passed up the stack.
     */
    private String reports;

    /**
     * Constructor.
     * @param message The message of the exception.
     * @param originalException The original exception that caused the failure.
     * @param reports The reports from the layer where the original exception occurred.
     */
    public CompositeCommandExecutionFailedException(String message, Exception originalException, String reports) {
        super(message);
        this.originalException=originalException;
        this.reports=reports;
    }

    /**
     * Prepends a report to the reports.
     * @param report The report to be prepended.
     */
    public void prependReport(String report){
        this.reports=report+this.reports;
    }

    /**
     * Returns the message of the exception and the reports.
     * @return The message of the exception and the reports.
     */
    public String getMessageWithReports(){
       return getMessage() + "\n" + reports;
    }
}
