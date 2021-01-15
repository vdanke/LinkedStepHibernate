package org.step.linked.step;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.step.linked.step.study.EasyBean;
import org.step.linked.step.study.HashBean;
import org.step.linked.step.study.SuperBean;

public class Runner {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        // BeanDefinitionReader - считывает все классы на наличие аннотаций
        ApplicationContext fromAnnotation = new AnnotationConfigApplicationContext("org.step.linked.step");

        System.out.printf(
                "Message from annotation %s%n",
                fromAnnotation.getBean("blablaBean", EasyBean.class).getMessage());
        System.out.printf(
                "Message from annotation super bean %s%n",
                fromAnnotation.getBean("justSuperBean", SuperBean.class).getBean().getMessage()
        );

        EasyBean easyBean = context.getBean("easyBean", EasyBean.class);
        EasyBean easyBeanSecond = context.getBean("easyBeanSecond", EasyBean.class);
        SuperBean superBean = context.getBean("superBean", SuperBean.class);

        System.out.println(easyBean.equals(easyBeanSecond));

        System.out.println(easyBeanSecond.getMessage());
        System.out.println(easyBean.getMessage());
        System.out.println(superBean.getBean().getMessage());

        HashBean first = context.getBean("publicHashBean", HashBean.class);
        HashBean second = context.getBean("publicHashBean", HashBean.class);
        System.out.printf("Result: %b%n", first.equals(second));
    }
}