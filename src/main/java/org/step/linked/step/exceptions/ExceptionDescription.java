package org.step.linked.step.exceptions;

public class ExceptionDescription {

    public String exceptionType;
    public String exceptionMessage;
    public String localDateTime;
    public String path;
    public String method;

    public ExceptionDescription(String exceptionType,
                                String exceptionMessage,
                                String localDateTime,
                                String path,
                                String method) {
        this.exceptionType = exceptionType;
        this.exceptionMessage = exceptionMessage;
        this.localDateTime = localDateTime;
        this.path = path;
        this.method = method;
    }
}
