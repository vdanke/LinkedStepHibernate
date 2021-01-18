package org.step.linked.step.study.impl;

import org.step.linked.step.study.Insert;
import org.step.linked.step.study.SpecialService;
import org.step.linked.step.study.SpecialAnnotation;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

@SpecialAnnotation(secretMessage = "not secret", value = "specialThing")
public class SpecialThingImpl implements SpecialService {

    @Insert
    private String verySecret;

    public SpecialThingImpl() {
        System.out.println("Constructor is called");
    }

    /*
    @PostConstruct is called between before init and after init
     */
    @PostConstruct
    public void postConstruct() {
        this.verySecret = "Не тут то было";
        System.out.println("Post Construct is called");
    }

    /*
    @PreDestroy is called when context is shutting down
     */
    @PreDestroy
    public void preDestroy() {
        System.out.println("Pre Destroy is called");
    }

    @Override
    public String specialThing() {
        return "Special one " + verySecret;
    }
}
