package org.step.linked.step;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EntityManagerAssistant {

    private static final Lock lock = new ReentrantLock();
    private static final String PERSISTENCE_UNIT_NAME = "linked-step-persistence-unit";
    private static final EntityManagerFactory EMF;

    static {
        lock.lock();
        EMF = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
        lock.unlock();
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }

    public static EntityManagerFactory getEMF() {
        return EMF;
    }
}
