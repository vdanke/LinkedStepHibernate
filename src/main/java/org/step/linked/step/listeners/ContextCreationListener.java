package org.step.linked.step.listeners;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.concurrent.atomic.AtomicInteger;

public class ContextCreationListener implements ServletContextListener {

    private final AtomicInteger incrementer = new AtomicInteger(0);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.printf("Context created: %d%n", incrementer.incrementAndGet());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.printf("Context destroyed: %d%n", incrementer.decrementAndGet());
    }

    public int getCurrentContextAmount() {
        return incrementer.get();
    }
}
