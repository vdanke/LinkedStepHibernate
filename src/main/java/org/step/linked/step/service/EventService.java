package org.step.linked.step.service;

public interface EventService {

    void sendEventToEventLogger(String event);

    void sendFileEventToLoggerService();
}
