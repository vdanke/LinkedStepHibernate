package org.step.linked.step;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerAssistant {

    private static final String PERSISTENCE_UNIT_NAME = "linked-step-persistence-unit";
    private static final EntityManagerFactory EMF;

    static {
        EMF = Persistence.createEntityManagerFactory(PERSISTENCE_UNIT_NAME);
    }

    public static EntityManager getEntityManager() {
        return EMF.createEntityManager();
    }
}
