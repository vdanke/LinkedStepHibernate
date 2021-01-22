package org.step.linked.step.listeners;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.util.concurrent.atomic.AtomicInteger;

public class HttpSessionInitializationListener implements HttpSessionListener {

    private final AtomicInteger incrementer = new AtomicInteger(0);

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        System.out.printf("Session activated: %d%n", incrementer.incrementAndGet());
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.printf("Session deactivated: %d%n", incrementer.decrementAndGet());
    }

    public int getCurrentSessionAmount() {
        return incrementer.get();
    }
}
