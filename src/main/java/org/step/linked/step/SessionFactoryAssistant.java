package org.step.linked.step;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.step.linked.step.model.Profile;
import org.step.linked.step.model.User;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class SessionFactoryAssistant {

    private static final Lock lock = new ReentrantLock();
    private static final SessionFactory SF;
    private static final String CONFIGURE_FILE_NAME = "hibernate.cfg.xml";

    static {
        lock.lock();
        SF = new Configuration()
                .configure(CONFIGURE_FILE_NAME)
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Profile.class)
                .buildSessionFactory();
        lock.unlock();
    }

    public static Session openNewSession() {
        return SF.openSession();
    }

    public static Session getCurrentSession() {
        return SF.getCurrentSession();
    }

    public static SessionFactory getSF() {
        return SF;
    }
}
